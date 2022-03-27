package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            bodyMeasurements?.let {
                updateView(it)
            }
        }
    }

    private fun updateView(bodyMeasurements: BodyMeasurements) {
        binding.tvOverallSize.text = "Overall Size: ${bodyMeasurements.sizeNumber}"
        binding.tvInternationalSize.text =
            "International Size: ${bodyMeasurements.sizeInternational}"
        binding.tvChest.text = "Chest: ${bodyMeasurements.chest} \""
        binding.tvWaist.text = "Waist: ${bodyMeasurements.waist} \""
        binding.tvHips.text = "Hips: ${bodyMeasurements.hips} \""
    }

    private fun initView() {
        activityView = requireActivity() as MainActivityView
        mySizesViewModel = ViewModelProvider(requireActivity()).get(MySizesViewModel::class.java);
    }

    private fun setUpEventListeners() {
        binding.btnUpdateMeasurements.setOnClickListener {
            showUpdateMeasurementsDialog()
        }

        binding.toolBar.setNavClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun showUpdateMeasurementsDialog() {
        val dialog = UpdateMeasurementsDialog.newInstance()
        dialog.show(requireActivity().supportFragmentManager)
    }
}