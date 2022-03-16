package com.elmenture.luuk.ui.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.databinding.FragmentMySizesBinding
import com.elmenture.luuk.ui.MainActivityView
import com.kokonetworks.kokosasa.base.BaseFragment
import com.luuk.common.models.BodyMeasurements

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MySizesFragment : BaseFragment() {
    lateinit var binding: FragmentMySizesBinding
    lateinit var activityView: MainActivityView
    lateinit var mySizesViewModel: MySizesViewModel

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
        mySizesViewModel.bodyMeasurements.observe(viewLifecycleOwner) { bodyMeasurements ->
            updateView(bodyMeasurements)
        }
    }

    private fun updateView(bodyMeasurements: BodyMeasurements) {
        binding.tvNeck.text = "Neck: ${bodyMeasurements.neck} CM"
        binding.tvShoulder.text = "Shoulder: ${bodyMeasurements.shoulder} CM"
        binding.tvChest.text = "Chest: ${bodyMeasurements.chest} CM"
        binding.tvWaist.text = "Waist: ${bodyMeasurements.waist} CM"
        binding.tvThigh.text = "Thigh: ${bodyMeasurements.thigh} CM"
        binding.tvLeg.text = "Leg: ${bodyMeasurements.leg} CM"
    }

    private fun initView() {
        activityView = requireActivity() as MainActivityView
        mySizesViewModel = ViewModelProvider(requireActivity()).get(MySizesViewModel::class.java);
    }

    private fun setUpEventListeners() {
        binding.btnUpdateMeasurements.setOnClickListener {
            showUpdateMeasurementsDialog()
        }
    }

    private fun showUpdateMeasurementsDialog() {
        val dialog = UpdateMeasurementsDialog.newInstance()
        dialog.show(requireActivity().supportFragmentManager)
    }

}