package com.elmenture.luuk.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.util.StringUtils
import com.bumptech.glide.Glide
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentViewItemBinding
import com.google.android.material.chip.Chip
import models.Spot
import models.TagProperty
import userdata.User
import utils.MiscUtils


class ViewItemFragment : BaseFragment(), Type.Home {

    private var activeSpot: Spot? = null
    private lateinit var binding: FragmentViewItemBinding
    private lateinit var homeViewModel: HomeViewModel

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
        observeLiveData()
    }

    @SuppressLint("ResourceAsColor")
    private fun observeLiveData() {
        homeViewModel.cartItemsLiveData.observe(viewLifecycleOwner) {
            val helperText = binding.toolBar.getHelperText()
            helperText?.text = if (it.contains(activeSpot)) "Item In Cart" else "Add To Cart"
        }
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
        binding.toolBar.setHelperTextClickListener {
            homeViewModel.updateSwipesData(
                like = activeSpot
            )
        }

    }

    private fun initView() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java);

        activeSpot?.let {
            binding.tvDescription.text = it.description
            binding.tvPrice.text =
                "Ksh ${MiscUtils.getFormattedAmount(it.priceCents.toDouble() / 100)}"

            if (it.sizeType.equals("INT", true)) {
                binding.tvSize.text = MiscUtils.getSpannedText(
                    getString(
                        R.string.contrast_text, "Size",
                        StringUtils.upperCase(it.sizeInternational)
                    )
                )
            } else {
                binding.tvSize.text = MiscUtils.getSpannedText(
                    getString(
                        R.string.contrast_text,
                        "Size",
                        it.sizeNumber.toString() + "(" + it.sizeType + ")"
                    )
                )
            }

            if (!it.tagProperties.isNullOrEmpty()) {
                for (id in it.tagProperties!!) {
                    addChipToGroup(User.getCurrent().getTagProperty(id))
                }
            }
            Glide.with(requireContext()).load(it.url).into(binding.ivImage)
        }
    }

    private fun addChipToGroup(tagProperty: TagProperty) {
        val chip = Chip(context)
        chip.text = tagProperty.value
        chip.chipIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)
        chip.isChipIconVisible = false
        chip.isCloseIconVisible = false
        // necessary to get single selection working
        chip.isClickable = false
        chip.isCheckable = false
        chip.tag = tagProperty

        binding.chipGroup.addView(chip as View)
        chip.setOnCloseIconClickListener {
            binding.chipGroup.removeView(chip as View)
        }
    }
}