package com.elmenture.luuk.ui.main.accountmanagement.profilesettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.databinding.FragmentProfileSettingsBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement.ProfileSettingsViewModel
import com.elmenture.luuk.ui.main.accountmanagement.mysizes.MySizesViewModel
import com.kokonetworks.kokosasa.base.BaseFragment
import utils.MiscUtils


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProfileSettingsFragment : BaseFragment() {
    private lateinit var profileSettingsViewModel: ProfileSettingsViewModel
    lateinit var binding: FragmentProfileSettingsBinding
    companion object {
        fun newInstance() = ProfileSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presetFields()
    }

    private fun presetFields() {
        profileSettingsViewModel.userDetails.value?.let { details ->
            binding.etName.setText(details.name)
            binding.etAddress.setText(details.physicalAddress)
            binding.etEmail.setText(details.email)
            binding.etContactPhone.setText(details.contactPhoneNumber)
            binding.etGender.setText(details.gender)
            binding.tvProfileInitials.text = MiscUtils.getUserNameInitials(details.name)
        }
    }

    private fun initView() {
        profileSettingsViewModel = ViewModelProvider(requireActivity()).get(ProfileSettingsViewModel::class.java);
    }
}