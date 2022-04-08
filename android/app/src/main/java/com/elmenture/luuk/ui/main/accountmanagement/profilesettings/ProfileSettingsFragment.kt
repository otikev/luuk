package com.elmenture.luuk.ui.main.accountmanagement.profilesettings

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.databinding.FragmentProfileSettingsBinding
import com.kokonetworks.kokosasa.base.BaseFragment
import models.UpdateUserDetailsRequest
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
        setEventListeners()
        presetFields()
    }

    private fun setEventListeners() {
        binding.toolBar.setHelperTextClickListener {
            getUserDetails().let {
                profileSettingsViewModel.updateUserData(it)
            }
        }
    }

    private fun getUserDetails(): UpdateUserDetailsRequest {
        val userDetailsRequest: UpdateUserDetailsRequest = UpdateUserDetailsRequest()
        binding.etName.text?.let { userDetailsRequest.name = it.getStringValue() }
        binding.etAddress.text?.let { userDetailsRequest.physicalAddress = it.getStringValue() }
        binding.etEmail.text?.let { userDetailsRequest.email = it.getStringValue() }
        binding.etContactPhone.text?.let { userDetailsRequest.contactPhoneNumber = it.getStringValue() }

        when (binding.rgGender.checkedRadioButtonId) {
            binding.rbMale.id -> userDetailsRequest.gender = "M"
            binding.rbFemale.id -> userDetailsRequest.gender = "F"
        }
        return userDetailsRequest
    }

    private fun presetFields() {
        profileSettingsViewModel.userDetails.value?.let { details ->
            binding.etName.setText(details.name)
            binding.etAddress.setText(details.physicalAddress)
            binding.etEmail.setText(details.email)
            binding.etContactPhone.setText(details.contactPhoneNumber)
            binding.tvProfileInitials.text = MiscUtils.getUserNameInitials(details.name)
            when(details.gender){
                "F"-> binding.rbFemale.isChecked = true
                "M"-> binding.rbMale.isChecked = true
                else-> binding.rbAll.isChecked = true
            }
        }
    }

    private fun initView() {
        profileSettingsViewModel =
            ViewModelProvider(requireActivity()).get(ProfileSettingsViewModel::class.java);
    }
    private fun Editable?.getStringValue(): String?{
        return if (this.isNullOrBlank()) null else this.toString()
    }
}

