package com.elmenture.luuk.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.get
import com.elmenture.luuk.AuthenticatedActivity
import com.elmenture.luuk.R
import com.elmenture.luuk.databinding.ActivityMainBinding
import com.elmenture.luuk.ui.main.accountmanagement.AccountManagementFragment
import com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement.CreateNewItemFragment
import com.elmenture.luuk.ui.main.accountmanagement.mysizes.MySizesFragment
import com.elmenture.luuk.ui.main.home.HomeFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AuthenticatedActivity(),
    NavigationBarView.OnItemSelectedListener, MainActivityView {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavView()
        startHomeFragment()
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

    override fun startMySizesFragment() {
        addFragment(MySizesFragment.newInstance(), MySizesFragment::class.java.canonicalName)
    }


    override fun startCreateItemFragment() {
        addFragment(CreateNewItemFragment.newInstance(), CreateNewItemFragment::class.java.canonicalName)
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
}