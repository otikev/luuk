package com.elmenture.luuk.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentViewItemBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Spot
import utils.MiscUtils


class ViewItemFragment : BaseFragment() {

    private var activeSpot: Spot? = null
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private lateinit var binding: FragmentViewItemBinding

    companion object {
        fun newInstance(activeSpot: Spot?): ViewItemFragment {
            val instance = ViewItemFragment()
            instance.activeSpot = activeSpot
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewItemBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setEventListeners()
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
    }

    private fun initView() {
        activeSpot?.let {
            binding.tvDescription.text = it.description
            binding.tvPrice.text ="Ksh ${MiscUtils.getFormattedAmount(it.priceCents.toDouble() / 100)}"
            binding.tvSize.text = MiscUtils.getSpannedText(getString(R.string.contrast_text,"Size : ", it.sizeNumber.toString()))//"Size : ${it.sizeNumber.toString()}"
            Glide.with(requireContext()).load(it.url).into(binding.ivImage)
        }
    }
}