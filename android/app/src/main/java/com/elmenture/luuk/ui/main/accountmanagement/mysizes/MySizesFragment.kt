package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.databinding.FragmentMySizesBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.kokonetworks.kokosasa.base.BaseFragment
import models.BodyMeasurements

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
        observeViewModelLiveData()
    }

    private fun observeViewModelLiveData() {
        mySizesViewModel.bodyMeasurementsLiveData.observe(viewLifecycleOwner) { bodyMeasurements ->
            updateView(bodyMeasurements)
        }
    }

    private fun updateView(bodyMeasurements: BodyMeasurements) {
        binding.etOverallSize.setText(bodyMeasurements.sizeNumber.toString())

        val internationalSize = InternationalSizes.fromString(bodyMeasurements.sizeInternational!!)

        binding.tvChest.text = "Chest: ${internationalSize!!.sizeMeasurement.bust_chest} \""
        binding.tvWaist.text = "Waist: ${internationalSize!!.sizeMeasurement.waist} \""
        binding.tvHips.text = "Hips: ${internationalSize!!.sizeMeasurement.hips} \""
    }

    private fun initView() {
        activityView = requireActivity() as MainActivityView
        mySizesViewModel = ViewModelProvider(requireActivity()).get(MySizesViewModel::class.java);
        setUpSizesSpinner()
        mySizesViewModel.getUserBodyMeasurements()
    }

    private fun setUpSizesSpinner() {
        for (size in InternationalSizes.values()) {
            spinnerArray.add(size.sizeName)
        }

        val spinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            spinnerArray
        )

        binding.spnInternational.adapter = spinnerAdapter
    }

    private fun setUpEventListeners() {
        binding.spnInternational.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    updateLivedata()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        binding.btnUpdateMeasurements.setOnClickListener {
            updateLivedata()
            mySizesViewModel.updateBodyMeasurements()
        }

        binding.toolBar.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun updateLivedata() {
        val bodyMeasurements = BodyMeasurements().apply {
            sizeNumber = binding.etOverallSize.text.toString().toInt()
            sizeInternational = binding.spnInternational.selectedItem.toString()
        }
        mySizesViewModel.bodyMeasurementsLiveData.value = bodyMeasurements
    }

}