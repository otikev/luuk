package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.databinding.DialogBodyMeasurementsBinding
import models.BodyMeasurements

class UpdateMeasurementsDialog : DialogFragment() {
    lateinit var binding: DialogBodyMeasurementsBinding
    lateinit var mySizesViewModel: MySizesViewModel
    val spinnerArray = ArrayList<String>()

    companion object {
        fun newInstance(): UpdateMeasurementsDialog {
            return UpdateMeasurementsDialog()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBodyMeasurementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        registerEventListeners()
        observeViewModelLiveData()
    }

    private fun initView() {
        mySizesViewModel = ViewModelProvider(requireActivity()).get(MySizesViewModel::class.java);
    }

    private fun observeViewModelLiveData() {
        mySizesViewModel.bodyMeasurementsLiveData.observe(viewLifecycleOwner) { bodyMeasurements ->
            bodyMeasurements?.let {
                updateView(bodyMeasurements)
            }
        }

        mySizesViewModel.postBodyMeasurementsApiState.observe(viewLifecycleOwner) { apiState ->
            apiState?.let {
                if (apiState.isSuccessful) {
                    dialog?.dismiss()
                }
            }
        }
    }

    private fun setUpSizesSpinner() {
        for (size in InternationalSizes.values()) {
            spinnerArray.add(size.sizeName)
        }

        val spinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.simple_list_item_1,
            spinnerArray
        )

        binding.spnInternational.adapter = spinnerAdapter
    }

    private fun updateView(bodyMeasurements: BodyMeasurements) {
        setUpSizesSpinner()
        binding.etSizeNumber.setText(bodyMeasurements.sizeNumber.toString())
        binding.etChest.setText(bodyMeasurements.chest.toString())
        binding.etWaist.setText(bodyMeasurements.waist.toString())
        binding.etHips.setText(bodyMeasurements.hips.toString())
    }

    private fun registerEventListeners() {
        binding.btnAccept.setOnClickListener {
            updateMeasurements()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun updateMeasurements() {
        val bodyMeasurements = BodyMeasurements().apply {
            sizeInternational = binding.spnInternational.selectedItem.toString()
            sizeNumber = binding.etSizeNumber.text.toString().toInt()
            chest = binding.etChest.text.toString().toInt()
            waist = binding.etWaist.text.toString().toInt()
            hips = binding.etHips.text.toString().toInt()
        }
        mySizesViewModel.updateBodyMeasurements(bodyMeasurements)
    }

    fun show(manager: FragmentManager) {
        show(manager, "UpdateMeasurementsDialog")
    }
}