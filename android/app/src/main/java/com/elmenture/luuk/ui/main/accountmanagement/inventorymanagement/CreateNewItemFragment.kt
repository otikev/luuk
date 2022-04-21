package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.Glide
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentCreateNewItemBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.google.android.material.chip.Chip
import models.Item
import models.TagProperty
import models.enums.InternationalSizes
import userdata.User
import views.CustomProgressBar
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateNewItemFragment : BaseFragment() {
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    lateinit var binding: FragmentCreateNewItemBinding
    lateinit var createNewItemViewModel: CreateNewItemViewModel
    lateinit var progressBar: CustomProgressBar
    val item = Item()
    val spinnerArray = ArrayList<String>()
    private var creds: BasicAWSCredentials =
        BasicAWSCredentials(
            User.getCurrent().userDetails.s3AccessKeyId,
            User.getCurrent().userDetails.s3SecretKeyId
        )
    private var s3Client: AmazonS3Client = AmazonS3Client(creds)
    private val PICK_IMAGE_REQUEST = 100
    private lateinit var filePath: Uri
    private lateinit var file: File
    private var editableItem: Item? = null

    companion object {
        fun newInstance(item: Item?): CreateNewItemFragment {
            val frag = CreateNewItemFragment()
            frag.editableItem = item
            return frag
        }

        val BUCKET_NAME =
            "luukatme-dev" //TODO: set dev or prod bucket depending on the APK build type
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewItemBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpEventListeners()
        observeLivedata()
    }

    private fun observeLivedata() {
        createNewItemViewModel.createNewItemApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    activityView.showMessage("Item Created Successfully")
                } else {
                    activityView.showMessage("Failed To Create Item", false)
                }
                progressBar.dismissAllowingStateLoss()
            }
        }

    }


    private fun initView() {
        createNewItemViewModel = ViewModelProvider(this).get(CreateNewItemViewModel::class.java)
        setUpSizesSpinner()
        binding.spnInternational.setSelection(4)

        binding.tagsMultiAutoCompleteTextView.setTokenizer(CommaTokenizer())
        binding.tagsMultiAutoCompleteTextView.threshold = 1

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line, User.getCurrent().tags
        )

        binding.tagsMultiAutoCompleteTextView.setAdapter( adapter)

        editableItem?.let {
            setUpEditableView(it)
        }
    }

    private fun addChipToGroup(tagProperty: TagProperty) {
        val chip = Chip(context)
        chip.text = tagProperty.value
        chip.chipIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
        chip.isChipIconVisible = false
        chip.isCloseIconVisible = true
        // necessary to get single selection working
        chip.isClickable = true
        chip.isCheckable = false
        chip.tag = tagProperty

        binding.chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(chip as View)
        }
    }

    private fun setUpEditableView(it: Item) {
        val size = if (it.sizeNumber == null) it.sizeInternational else it.sizeNumber.toString()

        binding.etDescription.setText(it.description)
        binding.etItemPrice.setText((it.price!! / 100).toString())
        binding.etEnterSize.setText(size)
        when (it.sizeType) {
            "US" -> binding.rbUs.isChecked = true
            "UK" -> binding.rbUk.isChecked = true
            "EU" -> binding.rbEu.isChecked = true
            "INT" -> binding.rbInt.isChecked = true
        }
        Glide.with(requireContext()).load(it.imageUrl).into(binding.ivS3image)
    }

    private fun setUpSizesSpinner() {
        for (size in InternationalSizes.values()) {
            spinnerArray.add(size.sizeName)
        }

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerArray
        )

        binding.spnInternational.adapter = spinnerAdapter
    }

    private fun setUpEventListeners() {
        binding.tagsMultiAutoCompleteTextView.setOnItemClickListener { adapterView, _, i, _ ->
            val tag: String = adapterView.getItemAtPosition(i) as String
            addChipToGroup(User.getCurrent().getTagProperty(tag))
            binding.tagsMultiAutoCompleteTextView.setText("")
        }

        binding.rgSizes.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_int -> {
                    binding.llInternationalContainer.visibility = View.VISIBLE
                    binding.tiEnterSize.visibility = View.GONE

                }
                else -> {
                    binding.llInternationalContainer.visibility = View.GONE
                    binding.tiEnterSize.visibility = View.VISIBLE
                }
            }
        }

        val editListener = View.OnClickListener {
            item.imageUrl = editableItem?.imageUrl
            item.id = editableItem?.id
            createNewItemViewModel.updateItem(getItemDetails())
        }

        val newItemListener = View.OnClickListener {
            var success = verifyFields(binding.etDescription, binding.etItemPrice)

            when (binding.rgSizes.checkedRadioButtonId) {
                R.id.rb_us, R.id.rb_uk, R.id.rb_eu -> {
                    if (binding.etEnterSize.text.isNullOrEmpty()) {
                        success = false
                        activityView.showMessage("Please enter a size")
                    }
                }
            }

            if (success) {
                uploadData()
            }
        }

        val listener = if (editableItem == null) newItemListener else editListener

        binding.btnAccept.setOnClickListener(listener)

        binding.toolBar.setNavClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnUpload.setOnClickListener { selectImage() }
    }

    private fun getItemDetails(): Item {
        item.sizeInternational = null
        item.sizeNumber = null

        when (binding.rgSizes.checkedRadioButtonId) {
            R.id.rb_us -> {
                item.sizeType = "US"
                item.sizeNumber = binding.etEnterSize.text.toString().toInt()
            }
            R.id.rb_uk -> {
                item.sizeType = "UK"
                item.sizeNumber = binding.etEnterSize.text.toString().toInt()
            }
            R.id.rb_eu -> {
                item.sizeType = "EU"
                item.sizeNumber = binding.etEnterSize.text.toString().toInt()
            }
            R.id.rb_int -> {
                item.sizeType = "INT"
                item.sizeInternational = binding.spnInternational.selectedItem.toString()
            }
        }

        item.description = binding.etDescription.text.toString()
        item.target = "f" //TODO: Only handling female clothing for now
        val ksh = binding.etItemPrice.text.toString().toLong()
        item.price = ksh * 100 //convert to cents for transmission to the server

        var tagProperties: MutableList<Long> = ArrayList()
        binding.chipGroup.children.forEach {
            val tagProp = (it as Chip).tag as TagProperty
            tagProp.id?.let {
                tagProperties.add(it)
            }
        }

        item.tagProperties = tagProperties
        return item
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!!.data != null) {
            // Get the Uri of data
            filePath = data.data!!
            binding.ivS3image.setImageURI(filePath)

            val inputStream: InputStream? =
                requireContext().contentResolver.openInputStream(filePath) //to read a file from content path
            file = File.createTempFile("img", ".jpg")
            val outStream: OutputStream =
                FileOutputStream(file)//creating stream pipeline to the file
            outStream.write(inputStream?.readBytes())//passing bytes of data to the fi
        }
    }

    private fun uploadData() {
        progressBar = CustomProgressBar.newInstance()
        progressBar.show(requireActivity().supportFragmentManager, "CustomProgressBar")
        val trans = TransferUtility.builder().context(requireContext()).s3Client(s3Client).build()
        val observer: TransferObserver = trans.upload(BUCKET_NAME, file.name, file)
        observer.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    val url = s3Client.getResourceUrl(BUCKET_NAME, file.name)

                    item.imageUrl = url
                    Log.d("S3-TAG", "success")
                    Log.d("S3-TAG", url)
                    postItemData()
                } else if (state == TransferState.FAILED) {
                    Log.d("S3-TAG", "fail")
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.d("S3-TAG", "Uploading $bytesCurrent / $bytesTotal")
                val progress = (bytesCurrent / bytesTotal) * 100
                progressBar.setProgress(progress.toInt())
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("error", ex.toString())
            }
        })
    }

    private fun postItemData() {
        try {
            createNewItemViewModel.createNewItem(getItemDetails())
        } catch (ex: java.lang.Exception) {
            activityView.showMessage("Please set all required fields")
        }
    }

    private fun verifyFields(vararg editTexts: EditText): Boolean {
        for (edit in editTexts) {
            if (edit.text.isNullOrEmpty()) {
                val hint = edit.hint.toString()
                activityView.showMessage("Please add $hint")
                return false
            }
        }
        return true
    }
}