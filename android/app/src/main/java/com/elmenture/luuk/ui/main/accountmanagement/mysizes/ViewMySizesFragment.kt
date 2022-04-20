package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentViewSizesBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.ActualMeasurements
import userdata.User


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
        var size = ""
        val dressSize = User.getCurrent().userDetails.femaleSize?.dress

        measurements?.let {
            if (!measurements.clothingSizes?.international.isNullOrEmpty()) {
                size = "Int : ${measurements.clothingSizes?.international}"
                binding.tvSizeInt.setBackgroundResource(R.drawable.rectangle_circled)
                binding.tvSizeInt.text = size
                binding.tvSizeEu.text = "EU : ${dressSize?.eu}"
                binding.tvSizeUs.text = "US : ${dressSize?.us}"
                binding.tvSizeUk.text = "UK : ${dressSize?.uk}"
            }
            if (measurements.clothingSizes?.uk!! > 0) {
                size = "UK : ${measurements.clothingSizes?.uk}"
                binding.tvSizeUk.setBackgroundResource(R.drawable.rectangle_circled)
                binding.tvSizeUk.text = size
                binding.tvSizeEu.text = "Int : ${dressSize?.eu}"
                binding.tvSizeUs.text = "US : ${dressSize?.us}"
                binding.tvSizeInt.text = "EU : ${dressSize?.international}"

            }
            if (measurements.clothingSizes?.us!! > 0) {
                size = "US : ${measurements.clothingSizes?.us}"
                binding.tvSizeUs.setBackgroundResource(R.drawable.rectangle_circled)
                binding.tvSizeUs.text = size
                binding.tvSizeEu.text = "EU : ${dressSize?.eu}"
                binding.tvSizeUk.text = "UK : ${dressSize?.uk}"
                binding.tvSizeInt.text = "Int : ${dressSize?.international}"

            }

            if (measurements.clothingSizes?.uk!! > 0) {
                size = "EU : ${measurements.clothingSizes?.eu}"
                binding.tvSizeEu.setBackgroundResource(R.drawable.rectangle_circled)
                binding.tvSizeEu.text = size
                binding.tvSizeUk.text = "UK : ${dressSize?.uk}"
                binding.tvSizeUs.text = "US : ${dressSize?.us}"
                binding.tvSizeInt.text = "Int : ${dressSize?.international}"

            }

        }

        measurements?.bodyMeasurements?.let {
            val chest = if(it.chest==0) dressSize?.chest else it.chest
            val waist = if(it.waist == 0) dressSize?.waist else it.waist
            val hips = if(it.hips == 0)  dressSize?.hips else it.hips

            binding.tvChest.text = "Chest : ${chest} CM"
            binding.tvWaist.text = "Waist : ${waist} CM"
            binding.tvHips.text = "Hips : ${hips}  CM"
        }
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
        binding.tvEdit.setOnClickListener { activityView.startEditMySizesFragment() }
    }

    private fun initView() {
        mySizesViewModel = ViewModelProvider(this).get(MySizesViewModel::class.java);
    }
}