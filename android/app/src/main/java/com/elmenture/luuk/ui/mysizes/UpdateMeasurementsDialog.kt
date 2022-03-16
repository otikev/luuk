package com.elmenture.luuk.ui.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.databinding.DialogBodyMeasurementsBinding
import com.luuk.common.models.BodyMeasurements

class UpdateMeasurementsDialog : DialogFragment() {
    lateinit var binding: DialogBodyMeasurementsBinding
    lateinit var mySizesViewModel: MySizesViewModel

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
            updateView(bodyMeasurements)
        }

        mySizesViewModel.postBodyMeasurementsApiState.observe(viewLifecycleOwner) { apiState ->
            apiState?.let {
                if (apiState.isSuccessful) {
                    dialog?.dismiss()
                }
            }
        }
    }

    private fun updateView(bodyMeasurements: BodyMeasurements) {
        binding.etNeck.setText(bodyMeasurements.neck.toString())
        binding.etShoulder.setText(bodyMeasurements.shoulder.toString())
        binding.etChest.setText(bodyMeasurements.chest.toString())
        binding.etWaist.setText(bodyMeasurements.waist.toString())
        binding.etThigh.setText(bodyMeasurements.thigh.toString())
        binding.etLeg.setText(bodyMeasurements.leg.toString())
    }

    private fun registerEventListeners() {
        binding.btnAccept.setOnClickListener {
            constructMeasurements()
        }
    }

    private fun constructMeasurements() {
        val bodyMeasurements = BodyMeasurements().apply {
            neck = binding.etNeck.text.toString().toInt()
            shoulder = binding.etShoulder.text.toString().toInt()
            chest = binding.etChest.text.toString().toInt()
            waist = binding.etWaist.text.toString().toInt()
            thigh = binding.etThigh.text.toString().toInt()
            leg = binding.etLeg.text.toString().toInt()
        }
        mySizesViewModel.updateBodyMeasurements(bodyMeasurements)
    }

    fun show(manager: FragmentManager) {
        show(manager, "UpdateMeasurementsDialog")
    }
}