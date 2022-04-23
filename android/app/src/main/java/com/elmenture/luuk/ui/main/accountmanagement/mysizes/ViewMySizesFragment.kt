package com.elmenture.luuk.ui.main.accountmanagement.mysizes

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    private fun updateFields(measurements: ActualMeasurements?) {
        var size = ""
        val dressSize = User.getCurrent().userDetails.femaleSize?.dress

        measurements?.let {

            measurements.clothingSizes?.international.let {
                var size = ""
                if (it != null) {
                    size = "Int : $it"
                    binding.tvSizeInt.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                } else {
                    size = "Int : ${dressSize?.international}"
                }
                binding.tvSizeInt.text = size
            }


            measurements.clothingSizes?.uk.let {
                var size = ""
                if (it != null) {
                    size = "UK : $it"
                    binding.tvSizeUk.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                } else {
                    size = "UK : ${dressSize?.uk}"
                }
                binding.tvSizeUk.text = size
            }

            measurements.clothingSizes?.us.let {
                var size = ""
                if (it != null) {
                    size = "US : $it"
                    binding.tvSizeUs.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                } else {
                    size = "US : ${dressSize?.us}"
                }
                binding.tvSizeUs.text = size
            }

            measurements.clothingSizes?.eu.let {
                var size = ""
                if (it != null) {
                    size = "EU : $it"
                    binding.tvSizeEu.setBackgroundResource(R.drawable.rectangle_circled_outlined)
                } else {
                    size = "EU : ${dressSize?.eu}"
                }
                binding.tvSizeEu.text = size
            }
        }

        measurements?.bodyMeasurements?.let {
            var chest: String?
            var waist: String?
            var hips: String?

            if (it.chest == null) {
                chest = dressSize?.chest
            } else {
                chest = it.chest.toString()
                binding.tvChest.setBackgroundResource(R.drawable.rectangle_circled_outlined)
            }

            if (it.waist == null) {
                waist = dressSize?.waist
            } else {
                waist = it.waist.toString()
                binding.tvWaist.setBackgroundResource(R.drawable.rectangle_circled_outlined)
            }

            if (it.hips == null) {
                hips = dressSize?.hips
            } else {
                hips = it.hips.toString()
                binding.tvHips.setBackgroundResource(R.drawable.rectangle_circled_outlined)
            }

            binding.tvChest.text = "Chest : $chest CM"
            binding.tvWaist.text = "Waist : $waist CM"
            binding.tvHips.text = "Hips : $hips  CM"
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