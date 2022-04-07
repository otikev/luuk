package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.R
import com.elmenture.luuk.databinding.FragmentMySizesBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.kokonetworks.kokosasa.base.BaseFragment
import models.BodyMeasurements
import models.ClothingSizes
import models.UserMeasurements

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MySizesFragment : BaseFragment() {
    lateinit var binding: FragmentMySizesBinding
    lateinit var activityView: MainActivityView
    lateinit var mySizesViewModel: MySizesViewModel
    val spinnerArray = ArrayList<String>()

    companion object {
        fun newInstance() = MySizesFragment()
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
        prepopulateFields()
    }


    private fun initView() {
        activityView = requireActivity() as MainActivityView
        mySizesViewModel = ViewModelProvider(requireActivity()).get(MySizesViewModel::class.java);
    }

    private fun prepopulateFields() {
        mySizesViewModel.userMeasurements.let { userMeasurements ->
            userMeasurements.value?.bodyMeasurements?.let {
                binding.etChest.setText(it.chest.toString())
                binding.etWaist.setText(it.waist.toString())
                binding.etHips.setText(it.hips.toString())
            }
            userMeasurements.value?.clothingSizes?.let { clothingSize ->
                binding.rbSize.isChecked = true

                clothingSize.international?.let {
                    binding.etEnterSize.setText(it)
                    binding.rbInt.isChecked = true
                }
                clothingSize.us?.let {
                    binding.etEnterSize.setText(it.toString())
                    binding.rbUs.isChecked = true
                }
                clothingSize.uk?.let {
                    binding.etEnterSize.setText(it.toString())
                    binding.rbUk.isChecked = true
                }
                clothingSize.eu?.let {
                    binding.etEnterSize.setText(it.toString())
                    binding.rbEu.isChecked = true
                }
            }
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

    private fun getMeasurementsData(): UserMeasurements? {
        if (binding.rbDimensions.isChecked) {
            return UserMeasurements(
                bodyMeasurements = BodyMeasurements(
                    chest = getIntegerFromString(binding.etChest.text.toString()),
                    waist = getIntegerFromString(binding.etWaist.text.toString()),
                    hips = getIntegerFromString(binding.etHips.text.toString())
                )
            )
        }
        if (binding.rbInt.isChecked) {
            return UserMeasurements(clothingSizes = ClothingSizes(international = binding.spnInternational.selectedItem.toString()))
        }
        when (binding.rgSizes.checkedRadioButtonId) {
            R.id.rb_us -> {
                return UserMeasurements(
                    clothingSizes = ClothingSizes(
                        us = binding.etEnterSize.text.toString().toInt()
                    )
                )
            }
            R.id.rb_uk -> {
                return UserMeasurements(
                    clothingSizes = ClothingSizes(
                        uk = binding.etEnterSize.text.toString().toInt()
                    )
                )
            }
            R.id.rb_eu -> {
                return UserMeasurements(
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