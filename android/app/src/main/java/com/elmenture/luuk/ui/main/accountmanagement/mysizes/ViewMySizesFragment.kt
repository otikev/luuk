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

            measurements.clothingSizes?.international?.let {
                size =
                    if (dressSize?.international == null) "Int : $it" else "Int : ${dressSize.international}"
                if (it.isNotEmpty())
                    binding.tvSizeInt.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                binding.tvSizeInt.text = size
            }


            measurements.clothingSizes?.uk?.let {
                size = if (dressSize?.uk == null) "UK : $it" else "UK : ${dressSize.uk}"
                if (it > 0)
                    binding.tvSizeUk.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                binding.tvSizeUk.text = size
            }

            measurements.clothingSizes?.us?.let {
                size = if (dressSize?.eu == null) "US : $it" else "US : ${dressSize.eu}"
                if (it > 0)
                    binding.tvSizeUs.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                binding.tvSizeUs.text = size
            }

            measurements.clothingSizes?.eu?.let {
                size = if (dressSize?.eu == null) "EU : $it" else "EU : ${dressSize.eu}"
                if (it > 0)
                    binding.tvSizeEu.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                binding.tvSizeEu.text = size
            }
        }

        measurements?.bodyMeasurements?.let {
            val chest = if (dressSize?.chest == null) it.chest else dressSize.chest
            val waist = if (dressSize?.waist == null) it.waist else dressSize.waist
            val hips = if (dressSize?.hips == null) it.hips else dressSize.hips

            if (it.chest > 0)
                binding.tvChest.setBackgroundResource(R.drawable.rectangle_circled_outlined)
            if (it.chest > 0)
                binding.tvWaist.setBackgroundResource(R.drawable.rectangle_circled_outlined)
            if (it.chest > 0)
                binding.tvHips.setBackgroundResource(R.drawable.rectangle_circled_outlined)

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