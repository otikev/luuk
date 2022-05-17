package com.elmenture.luuk.ui.main

import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseActivityView
import models.Item
import models.Spot
import models.TagProperty

interface MainActivityView : BaseActivityView {
    fun startHomeFragment()
    fun startAccountManagementFragment()
    fun startEditMySizesFragment()
    fun logout()
    fun resetBottomNavigation()
    fun startCreateItemFragment(item: Item? = null)
    fun startProfileSettingsFragment()
    fun showMessage(message: String? = null, isSuccessful: Boolean = true)
    fun startViewItemFragment(activeSpot: Spot?)
    fun startViewMySizesFragment()
    fun startViewCartFragment()
    fun startInventoryManagementFragment()
    fun startHelpFragment()
    fun startViewCheckoutFragment()
    fun startCheckoutSuccessFragment()
    fun startCheckoutFailureFragment()
    fun startOrderHistoryFragment()
    fun startSearchItemsFragment()
    fun startViewSearchedItemsFragment(tagPropertyId: Long)
    fun handleBottomNav(type: Fragment)
}