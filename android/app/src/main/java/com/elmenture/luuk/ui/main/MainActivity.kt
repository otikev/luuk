package com.elmenture.luuk.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.AuthenticatedActivity
import com.elmenture.luuk.R
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.navigation.NavigationBarView
import models.Item
import models.Spot


class MainActivity : AuthenticatedActivity(),
    NavigationBarView.OnItemSelectedListener, MainActivityView {
    private lateinit var placesClient: PlacesClient
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
        clearBackStack()
        replaceFragment(HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
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
        addFragment(CheckoutSuccessFragment.newInstance(), CheckoutSuccessFragment::class.java.canonicalName)
    }

    override fun startCheckoutFailureFragment() {
        addFragment(CheckoutFailureFragment.newInstance(), CheckoutFailureFragment::class.java.canonicalName)
    }

    override fun startInventoryManagementFragment() {
        addFragment(InventoryManagementFragment.newInstance(),InventoryManagementFragment::class.java.canonicalName)
    }

    override fun startOrderHistoryFragment() {
        addFragment(OrderHistoryFragment.newInstance(),OrderHistoryFragment::class.java.canonicalName)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navHome -> {
                if (!item.isChecked)
                    startHomeFragment()
                true
            }
            R.id.navSearch -> {
                // Respond to navigation item 2 click
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

    override fun showMessage(message: String?, isSuccessful: Boolean) {
        super.showMessage(binding.root, isSuccessful, message)
    }
}