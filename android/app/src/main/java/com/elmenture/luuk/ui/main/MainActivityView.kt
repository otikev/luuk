package com.elmenture.luuk.ui.main

import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseActivityView

interface MainActivityView: BaseActivityView {
    fun startHomeFragment()
    fun startAccountManagementFragment()
    fun startMySizesFragment()
    fun logout()
    fun resetBottomNavigation()
    fun startCreateItemFragment()
    fun startProfileSettingsFragment()
    fun showMessage(message: String? = null, isSuccessful: Boolean = true)
}