package com.elmenture.luuk.ui.main.accountmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elmenture.luuk.BuildConfig
import com.elmenture.luuk.databinding.FragmentAccountManagementBinding

import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.ui.main.MainActivityView
import userdata.User

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AccountManagementFragment : BaseFragment(), Type.ProfileSettings {
    lateinit var binding: FragmentAccountManagementBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }

    companion object {
        fun newInstance() = AccountManagementFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountManagementBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!User.getCurrent().userDetails.isStaff){
            binding.itemInventoryManagement.visibility = GONE
        }

        binding.appVersion.text = "Version ${BuildConfig.VERSION_NAME}"
        setUpEventListeners()
    }

    private fun setUpEventListeners() {
        binding.itemMySizes.setOnClickListener {
            if(User.getCurrent().userDetails.actualMeasurements==null){
                activityView.startEditMySizesFragment()
            }else{
                activityView.startViewMySizesFragment()
            }
        }

        binding.itemLogout.setOnClickListener{
            activityView.logout()
        }

        binding.itemInventoryManagement.setOnClickListener{
            activityView.startInventoryManagementFragment()
        }

        binding.itemProfileSettings.setOnClickListener{
            activityView.startProfileSettingsFragment()
        }

        binding.itemHelp.setOnClickListener {
            activityView.startHelpFragment()
        }
        binding.itemOrderHistory.setOnClickListener {
            activityView.startOrderHistoryFragment()
        }
    }

}