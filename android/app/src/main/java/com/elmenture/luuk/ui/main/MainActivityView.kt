package com.elmenture.luuk.ui.main

import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseActivityView
import models.Spot

interface MainActivityView: BaseActivityView {
    fun startHomeFragment()
    fun startAccountManagementFragment()
    fun startEditMySizesFragment()
    fun logout()
    fun resetBottomNavigation()
    fun startCreateItemFragment()
    fun startProfileSettingsFragment()
    fun showMessage(message: String? = null, isSuccessful: Boolean = true)
    fun startViewItemFragment(activeSpot: Spot?)
    fun startViewMySizesFragment()
    fun startViewCartFragment()
    fun startInventoryManagementFragment()
}