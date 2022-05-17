package com.elmenture.luuk.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.AuthenticatedActivity
import com.elmenture.luuk.R
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.ActivityMainBinding
import com.elmenture.luuk.ui.main.accountmanagement.AccountManagementFragment
import com.elmenture.luuk.ui.main.accountmanagement.help.HelpFragment
import com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement.InventoryManagementFragment
import com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement.createnewitem.CreateNewItemFragment
import com.elmenture.luuk.ui.main.accountmanagement.mysizes.EditMySizesFragment
import com.elmenture.luuk.ui.main.accountmanagement.mysizes.ViewMySizesFragment
import com.elmenture.luuk.ui.main.accountmanagement.orderhistory.OrderHistoryFragment
import com.elmenture.luuk.ui.main.accountmanagement.profilesettings.ProfileSettingsFragment
import com.elmenture.luuk.ui.main.cart.ViewCartFragment
import com.elmenture.luuk.ui.main.cart.checkout.CheckoutFailureFragment
import com.elmenture.luuk.ui.main.cart.checkout.CheckoutFragment
import com.elmenture.luuk.ui.main.cart.checkout.CheckoutSuccessFragment
import com.elmenture.luuk.ui.main.home.HomeFragment
import com.elmenture.luuk.ui.main.home.ViewItemFragment
import com.elmenture.luuk.ui.main.search.searchitems.SearchItemsFragment
import com.elmenture.luuk.ui.main.search.viewsearchitems.ViewSearchedItemsFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.navigation.NavigationBarView
import models.Item
import models.Spot


class MainActivity : AuthenticatedActivity(),
    NavigationBarView.OnItemSelectedListener, MainActivityView {
    lateinit var binding: ActivityMainBinding
    lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpPlaces()
        initView()
        observeLiveData()
        setupBottomNavView()
        startHomeFragment()
        binding.bottomNavigation.itemIconTintList = null
    }

    private fun observeLiveData() {
        mainActivityViewModel.cartItemsLiveData.observe(this) { swipeRecords ->
            swipeRecords.let {
                if (it.isNotEmpty()) {
                    binding.bottomNavigation.menu.getItem(2).icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_cart_selector_full)
                } else {
                    binding.bottomNavigation.menu.getItem(2).icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_cart_selector_no_item)
                }
            }
        }

        mainActivityViewModel.profileDetailsLiveData.observe(this){
            it?.let {
                if(it.email.isNullOrEmpty()|| it.contactPhoneNumber.isNullOrEmpty() || it.physicalAddress.isNullOrEmpty()){
                    binding.bottomNavigation.menu.getItem(3).icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_profile_selector_profile_incomplete)
                }else{
                    binding.bottomNavigation.menu.getItem(3).icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_profile_selector_profile_complete)
                }
            }
        }
    }

    private fun initView() {
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java);
    }

    private fun setUpPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.maps_api_key));
        }
        Places.createClient(this)
    }

    override fun startHomeFragment() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            addFragment(HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
        } else {
            replaceFragment(HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
        }
    }

    override fun startAccountManagementFragment() {
        addFragment(
            AccountManagementFragment.newInstance(),
            AccountManagementFragment::class.java.canonicalName
        )
    }

    override fun logout() {
        super.logout()
    }

    override fun startEditMySizesFragment() {
        addFragment(
            EditMySizesFragment.newInstance(),
            EditMySizesFragment::class.java.canonicalName
        )
    }

    override fun startViewItemFragment(activeSpot: Spot?) {
        addFragment(
            ViewItemFragment.newInstance(activeSpot),
            ViewItemFragment::class.java.canonicalName
        )
    }

    override fun startViewMySizesFragment() {
        addFragment(
            ViewMySizesFragment.newInstance(),
            ViewMySizesFragment::class.java.canonicalName
        )
    }


    override fun startCreateItemFragment(item: Item?) {
        addFragment(
            CreateNewItemFragment.newInstance(item),
            CreateNewItemFragment::class.java.canonicalName
        )
    }

    override fun startHelpFragment() {
        addFragment(HelpFragment.newInstance(), HelpFragment::class.java.canonicalName)
    }


    override fun startProfileSettingsFragment() {
        addFragment(
            ProfileSettingsFragment.newInstance(),
            ProfileSettingsFragment::class.java.canonicalName
        )
    }

    override fun startViewCartFragment() {
        addFragment(ViewCartFragment.newInstance(), ViewCartFragment::class.java.canonicalName)
    }

    override fun startViewCheckoutFragment() {
        addFragment(CheckoutFragment.newInstance(), CheckoutFragment::class.java.canonicalName)
    }

    override fun startCheckoutSuccessFragment() {
        addFragment(
            CheckoutSuccessFragment.newInstance(),
            CheckoutSuccessFragment::class.java.canonicalName
        )
    }

    override fun startCheckoutFailureFragment() {
        addFragment(
            CheckoutFailureFragment.newInstance(),
            CheckoutFailureFragment::class.java.canonicalName
        )
    }

    override fun startInventoryManagementFragment() {
        addFragment(
            InventoryManagementFragment.newInstance(),
            InventoryManagementFragment::class.java.canonicalName
        )
    }

    override fun startOrderHistoryFragment() {
        addFragment(
            OrderHistoryFragment.newInstance(),
            OrderHistoryFragment::class.java.canonicalName
        )
    }

    override fun startSearchItemsFragment() {
        addFragment(
            SearchItemsFragment.newInstance(),
            SearchItemsFragment::class.java.canonicalName
        )
    }

    override fun startViewSearchedItemsFragment(tagPropertyId: Long) {
        addFragment(
            ViewSearchedItemsFragment.newInstance(tagPropertyId),
            ViewSearchedItemsFragment::class.java.canonicalName
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navHome -> {
                if (!item.isChecked)
                    startHomeFragment()
                true
            }
            R.id.navSearch -> {
                if (!item.isChecked)
                    startSearchItemsFragment()
                true
            }
            R.id.navCart -> {
                if (!item.isChecked)
                    startViewCartFragment()
                true
            }
            R.id.navProfile -> {
                if (!item.isChecked)
                    startAccountManagementFragment()
                true
            }

            else -> false
        }
    }


    private fun setupBottomNavView() {
        binding.bottomNavigation.setOnItemSelectedListener(this)
    }

    override fun resetBottomNavigation() {
        binding.bottomNavigation.menu.findItem(R.id.navHome).isChecked = true
    }

    override fun addFragment(fragment: Fragment, tag: String) {
        super.addFragment(fragment, tag)
        handleBottomNav(fragment)
    }

    override fun replaceFragment(fragment: Fragment, tag: String) {
        super.replaceFragment(fragment, tag)
        handleBottomNav(fragment)
    }


    override fun handleBottomNav(type: Fragment) {
        when (type) {
            is Type.Search -> binding.bottomNavigation.menu.findItem(R.id.navSearch).isChecked =true
            is Type.Home -> binding.bottomNavigation.menu.findItem(R.id.navHome).isChecked = true
            is Type.ProfileSettings -> binding.bottomNavigation.menu.findItem(R.id.navProfile).isChecked =true
            is Type.Cart -> binding.bottomNavigation.menu.findItem(R.id.navCart).isChecked = true
        }
    }

    override fun showMessage(message: String?, isSuccessful: Boolean) {
        super.showMessage(binding.root, isSuccessful, message)
    }
}