package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.elmenture.luuk.databinding.FragmentCreateNewItemBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.kokonetworks.kokosasa.base.BaseFragment
import models.Item
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateNewItemFragment : BaseFragment() {
    lateinit var binding: FragmentCreateNewItemBinding
    lateinit var activityView: MainActivityView
    lateinit var createNewItemViewModel: CreateNewItemViewModel
    val spinnerArray = ArrayList<String>()

    private var creds: BasicAWSCredentials =
        BasicAWSCredentials(ACCESS_ID, SECRET_KEY)
    private var s3Client: AmazonS3Client = AmazonS3Client(creds)
    private val PICK_IMAGE_REQUEST = 100
    private lateinit var filePath: Uri
    private lateinit var file: File

    companion object {
        fun newInstance() = CreateNewItemFragment()


        val ACCESS_ID = "AKIAVKGR4LJBKJLX3Z6N"
        val SECRET_KEY = "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"
        val BUCKET_NAME = "luukatme-dev"
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
                    Toast.makeText(requireContext(), "Item Created Successfully", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Failed To Create Item ", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }


    private fun initView() {
        activityView = requireActivity() as MainActivityView
        createNewItemViewModel = ViewModelProvider(this).get(CreateNewItemViewModel::class.java);
    }

    private fun setUpEventListeners() {
        binding.btnAccept.setOnClickListener {
//            try {
//                createNewItemViewModel.createNewItem(getItemDetails())
//            } catch (ex: NumberFormatException) {
//                Toast.makeText(
//                    requireContext(),
//                    "Please set all the required fields",
//                    Toast.LENGTH_LONG
//                ).show()
//
//            }

            uploadImage()
        }

        binding.toolBar.setNavClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnUpload.setOnClickListener { selectImage() }
    }

    private fun getItemDetails(): Item {
        return Item().apply {
            sizeInternational = binding.etSizeInternational.text.toString()
            sizeNumber = binding.etSizeNumber.text.toString().toInt()
            description = binding.etDescription.text.toString()
            imageUrl = binding.etImageUrl.text.toString()
            price = binding.etItemPrice.text.toString().toLong()
        }
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

            file = File.createTempFile("image", filePath.lastPathSegment)
            val outStream: OutputStream =
                FileOutputStream(file)//creating stream pipeline to the file
            outStream.write(inputStream?.readBytes())//passing bytes of data to the fi
        }
    }

    private fun uploadImage() {

        val trans = TransferUtility.builder().context(requireContext()).s3Client(s3Client).build()
        val observer: TransferObserver = trans.upload(BUCKET_NAME, filePath.lastPathSegment, file)//manual storage permission
        observer.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    val url = s3Client.generatePresignedUrl(GeneratePresignedUrlRequest(BUCKET_NAME, observer.key)).toString()

                    Log.d("S3-TAG", "success")
                    Log.d("S3-TAG", url)
                } else if (state == TransferState.FAILED) {
                    Log.d("S3-TAG", "fail")
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.d("S3-TAG", "Uploading $bytesCurrent / $bytesTotal")

                if (bytesCurrent == bytesTotal) {
                    //imageView!!.setImageResource(R.drawable.upload_image_with_round)
                }
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("error", ex.toString())
            }
        })
    }

}