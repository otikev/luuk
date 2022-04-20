package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentMySizesBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.BodyMeasurements
import models.ClothingSizes
import models.ActualMeasurements
import models.enums.InternationalSizes

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditMySizesFragment : BaseFragment() {
    lateinit var binding: FragmentMySizesBinding
    lateinit var mySizesViewModel: MySizesViewModel
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    val spinnerArray = ArrayList<String>()

    companion object {
        fun newInstance() = EditMySizesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMySizesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpEventListeners()
        setUpSizesSpinner()
        observeLivedata()
    }


    private fun initView() {
        mySizesViewModel = ViewModelProvider(this).get(MySizesViewModel::class.java);
    }

    private fun updateFields(actualMeasurements: ActualMeasurements?) {
        actualMeasurements.let { userMeasurements ->
            userMeasurements?.bodyMeasurements?.let {
                binding.etChest.setText(it.chest.toString())
                binding.etWaist.setText(it.waist.toString())
                binding.etHips.setText(it.hips.toString())
            }
            userMeasurements?.clothingSizes?.let { clothingSize ->
                binding.rbSize.isChecked = true

                clothingSize.international?.let {
                    binding.rbInt.isChecked = true
                    val pos = spinnerArray.indexOf(it)
                    binding.spnInternational.setSelection(pos)
                }
                clothingSize.us?.let {
                    if (it > 0) {
                        binding.etEnterSize.setText(clothingSize.us.toString())
                        binding.rbUs.isChecked = true
                    }
                }
                clothingSize.uk?.let {

                    if (it > 0) {
                        binding.etEnterSize.setText(it.toString())
                        binding.rbUk.isChecked = true
                    }
                }
                clothingSize.eu?.let {
                    if (it > 0) {
                        binding.etEnterSize.setText(it.toString())
                        binding.rbEu.isChecked = true
                    }
                }
            }
        }
    }

    private fun observeLivedata() {
        mySizesViewModel.updateUserMeasurementsApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    activityView.showMessage("Item Created Successfully")
                } else {
                    activityView.showMessage("Failed To Create Item", false)
                }
            }
        }

        mySizesViewModel.userMeasurements.observe(viewLifecycleOwner) {
            updateFields(it)
        }

    }

    private fun setUpEventListeners() {
        binding.rgOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_size -> {
                    binding.llSizes.visibility = View.VISIBLE
                    binding.llDimensions.visibility = View.GONE
                }
                R.id.rb_dimensions -> {
                    binding.llSizes.visibility = View.GONE
                    binding.llDimensions.visibility = View.VISIBLE
                }
            }
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


        binding.btnAccept.setOnClickListener {
            getMeasurementsData()?.let {
                mySizesViewModel.updateUserMeasurements(it)
            }
        }

        binding.toolBar.setNavClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getMeasurementsData(): ActualMeasurements? {
        if (binding.rbDimensions.isChecked) {
            return ActualMeasurements(
                bodyMeasurements = BodyMeasurements(
                    chest = getIntegerFromString(binding.etChest.text.toString()),
                    waist = getIntegerFromString(binding.etWaist.text.toString()),
                    hips = getIntegerFromString(binding.etHips.text.toString())
                )
            )
        }
        if (binding.rbInt.isChecked) {
            return ActualMeasurements(clothingSizes = ClothingSizes(international = binding.spnInternational.selectedItem.toString()))
        }
        when (binding.rgSizes.checkedRadioButtonId) {
            R.id.rb_us -> {
                return ActualMeasurements(
                    clothingSizes = ClothingSizes(
                        us = binding.etEnterSize.text.toString().toInt()
                    )
                )
            }
            R.id.rb_uk -> {
                return ActualMeasurements(
                    clothingSizes = ClothingSizes(
                        uk = binding.etEnterSize.text.toString().toInt()
                    )
                )
            }
            R.id.rb_eu -> {
                return ActualMeasurements(
                    clothingSizes = ClothingSizes(
                        eu = binding.etEnterSize.text.toString().toInt()
                    )
                )
            }
        }
        return null
    }

    private fun getIntegerFromString(value: String?): Int {
        return try {
            value!!.toInt()
        } catch (e: Exception) {
            0
        }
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
}