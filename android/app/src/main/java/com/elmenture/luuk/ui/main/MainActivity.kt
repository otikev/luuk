package com.elmenture.luuk.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.AuthenticatedActivity
import com.elmenture.luuk.R
import com.elmenture.luuk.databinding.ActivityMainBinding
import com.elmenture.luuk.ui.main.accountmanagement.AccountManagementFragment
import com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement.CreateNewItemFragment
import com.elmenture.luuk.ui.main.accountmanagement.profilesettings.ProfileSettingsFragment
import com.elmenture.luuk.ui.main.accountmanagement.mysizes.EditMySizesFragment
import com.elmenture.luuk.ui.main.accountmanagement.mysizes.ViewMySizesFragment
import com.elmenture.luuk.ui.main.home.HomeFragment
import com.elmenture.luuk.ui.main.home.ViewItemFragment
import com.google.android.material.navigation.NavigationBarView
import models.Spot

class MainActivity : AuthenticatedActivity(),
    NavigationBarView.OnItemSelectedListener, MainActivityView {
    lateinit var binding: ActivityMainBinding
    lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        observeLiveData()
        setupBottomNavView()
        startHomeFragment()
        binding.bottomNavigation.itemIconTintList = null
    }

    private fun observeLiveData() {
        mainActivityViewModel.swipeRecordsLiveData.observe(this) { swipeRecords ->
            swipeRecords.likes.let {
                if (it.isNotEmpty()) {
                    binding.bottomNavigation.menu.getItem(2).icon = ContextCompat.getDrawable(this, R.drawable.ic_cart_selector_full)
                }else{
                    binding.bottomNavigation.menu.getItem(2).icon = ContextCompat.getDrawable(this, R.drawable.ic_cart_selector_no_item)
                }
            }
        }
    }

    private fun initView() {
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java);
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
        addFragment(EditMySizesFragment.newInstance(), EditMySizesFragment::class.java.canonicalName)
    }

    override fun startViewItemFragment(activeSpot: Spot?)  {
        addFragment(ViewItemFragment.newInstance(activeSpot), ViewItemFragment::class.java.canonicalName)
    }

    override fun startViewMySizesFragment()  {
        addFragment(ViewMySizesFragment.newInstance(), ViewMySizesFragment::class.java.canonicalName)
    }


    override fun startCreateItemFragment() {
        addFragment(CreateNewItemFragment.newInstance(), CreateNewItemFragment::class.java.canonicalName)
    }


    override fun startProfileSettingsFragment() {
        addFragment(ProfileSettingsFragment.newInstance(), ProfileSettingsFragment::class.java.canonicalName)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navHome -> {
                if(!item.isChecked)
                    startHomeFragment()
                true
            }
            R.id.navSearch -> {
                // Respond to navigation item 2 click
                true
            }
            R.id.navCart -> {
                // Respond to navigation item 2 click
                true
            }
            R.id.navProfile -> {
                if(!item.isChecked)
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
        binding.bottomNavigation.menu.findItem(R.id.navHome).isChecked =  true
    }

    override fun showMessage(message: String?, isSuccessful: Boolean) {
        super.showMessage(binding.root, isSuccessful,message)
    }

}