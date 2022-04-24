package com.elmenture.luuk.ui.main.accountmanagement.profilesettings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentProfileSettingsBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.SignInResponse
import models.UpdateUserDetailsRequest
import models.enums.Tags
import utils.MiscUtils


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProfileSettingsFragment : BaseFragment() {
    private lateinit var profileSettingsViewModel: ProfileSettingsViewModel
    lateinit var binding: FragmentProfileSettingsBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }

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
        observeLiveData()
        setEventListeners()
    }

    private fun observeLiveData() {
        profileSettingsViewModel.updateUserApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    activityView.showMessage("Details Updated Successfully")
                } else {
                    activityView.showMessage("Failed To Update Details", false)
                }
            }
        }
        profileSettingsViewModel.userDetails.observe(viewLifecycleOwner) {
            updateFields(it)
        }

        profileSettingsViewModel.isChangeSavable.observe(viewLifecycleOwner) {
            binding.toolBar.setHelperTextVisible(it)
            if (it) binding.btnUndoChanges.visibility =
                View.VISIBLE else binding.btnUndoChanges.visibility = View.GONE
        }
    }

    private fun setEventListeners() {
        binding.toolBar.setHelperTextClickListener {
            profileSettingsViewModel.updateUserData(
                getUserDetails()
            )
        }

        binding.btnUndoChanges.setOnClickListener {
            profileSettingsViewModel.userDetails.value?.let {
                updateFields(it)
            }
        }

        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
    }


    private fun getUserDetails(): UpdateUserDetailsRequest {
        var targets = ""
        val userDetailsRequest = UpdateUserDetailsRequest()
        binding.etName.text?.let { userDetailsRequest.name = it.getStringValue() }
        binding.etAddress.text?.let { userDetailsRequest.physicalAddress = it.getStringValue() }
        binding.etEmail.text?.let { userDetailsRequest.email = it.getStringValue() }
        binding.etContactPhone.text?.let {
            userDetailsRequest.contactPhoneNumber = it.getStringValue()
        }

        if (binding.rbMale.isChecked)
            targets += "m,"

        if (binding.rbFemale.isChecked)
            targets += "f,"

        if (binding.rbKids.isChecked)
            targets += "c,"
        targets = targets.trim(',')

        userDetailsRequest.targets = targets

        return userDetailsRequest
    }

    private fun updateFields(details: SignInResponse?) {
        details?.let {
            binding.etName.setText(details.name)
            binding.etAddress.setText(details.physicalAddress)
            binding.tiEmail.visibility = if (details.email.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding.etEmail.setText(details.email)
            binding.etContactPhone.setText(details.contactPhoneNumber)
            binding.tvProfileInitials.text = MiscUtils.getUserNameInitials(details.name)

            details.clothingRecommendations?.let {
                binding.rbMale.isChecked = it.contains(Tags.m.name)
                binding.rbFemale.isChecked = it.contains(Tags.f.name)
                binding.rbKids.isChecked = it.contains(Tags.c.name)
            }
        }

        setDataChangeListeners()
    }

    private fun setDataChangeListeners() {

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                profileSettingsViewModel.onCurrentDetailsChanged(getUserDetails())
            }
        }
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
            profileSettingsViewModel.onCurrentDetailsChanged(getUserDetails())
        }

        binding.etContactPhone.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)
        binding.etName.addTextChangedListener(textWatcher)
        binding.rbFemale.setOnCheckedChangeListener(checkedChangeListener)
        binding.rbMale.setOnCheckedChangeListener(checkedChangeListener)
        binding.rbKids.setOnCheckedChangeListener(checkedChangeListener)
    }

    private fun initView() {
        profileSettingsViewModel =
            ViewModelProvider(this).get(ProfileSettingsViewModel::class.java);
    }

    private fun Editable?.getStringValue(): String? {
        return if (this.isNullOrEmpty()) null else this.toString()
    }
}

