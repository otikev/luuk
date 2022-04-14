package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentViewSizesBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.ActualMeasurements


class ViewMySizesFragment : BaseFragment() {

    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private lateinit var binding: FragmentViewSizesBinding
    lateinit var mySizesViewModel: MySizesViewModel


    companion object {
        fun newInstance() = ViewMySizesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewSizesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setEventListeners()
        observeLiveData()
    }

    private fun observeLiveData() {
        mySizesViewModel.userMeasurements.observe(viewLifecycleOwner) {
            updateFields(it)
        }
    }

    private fun updateFields(measurements: ActualMeasurements?) {
        measurements?.let {
            if (!measurements.clothingSizes?.international.isNullOrEmpty())
                binding.tvSize.text =
                    "Size International : ${measurements.clothingSizes?.international}"
            if (measurements.clothingSizes?.uk!! > 0)
                binding.tvSize.text = "Size UK : ${measurements.clothingSizes?.uk}"
            if (measurements.clothingSizes?.us!! > 0)
                binding.tvSize.text =
                    "Size US : ${measurements.clothingSizes?.us}"
            if (measurements.clothingSizes?.uk!! > 0)
                binding.tvSize.text =
                    "Size EU : ${measurements.clothingSizes?.eu}"
        }

        measurements?.bodyMeasurements?.let {
            binding.tvChest.text = "Chest : ${it.chest} CM"
            binding.tvWaist.text = "Waist : ${it.waist} CM"
            binding.tvHips.text = "Hips : ${it.hips}  CM"
        }
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
        binding.cvSizes.setOnClickListener{ activityView.startEditMySizesFragment()}
    }

    private fun initView() {
        mySizesViewModel = ViewModelProvider(this).get(MySizesViewModel::class.java);

//        activeSpot?.let {
//            binding.tvDescription.text = it.description
//            binding.tvPrice.text ="Ksh ${MiscUtils.getFormattedAmount(it.priceCents.toDouble() / 100)}"
//            binding.tvSize.text = MiscUtils.getSpannedText(getString(R.string.contrast_text,"Size : ", it.sizeNumber.toString()))//"Size : ${it.sizeNumber.toString()}"
//            Glide.with(requireContext()).load(it.url).into(binding.ivImage)
//        }
    }
}