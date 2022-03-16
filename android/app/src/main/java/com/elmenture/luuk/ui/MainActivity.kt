package com.elmenture.luuk.ui

import android.os.Bundle
import android.view.MenuItem
import com.elmenture.luuk.AuthenticatedActivity
import com.elmenture.luuk.R
import com.elmenture.luuk.ui.mysizes.MySizesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AuthenticatedActivity(),
    NavigationBarView.OnItemSelectedListener, MainActivityView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dummy)
        setupBottomNavView()
    }

    override fun onResume() {
        super.onResume()
        startHomeFragment()
    }

    override fun startHomeFragment() {
        replaceFragment(HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
    }

    override fun startAccountManagementFragment() {
        replaceFragment(AccountManagementFragment.newInstance(), AccountManagementFragment::class.java.simpleName)
    }

    override fun startMySizesFragment() {
        replaceFragment(MySizesFragment.newInstance(), MySizesFragment::class.java.simpleName)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navHome -> {
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
                startAccountManagementFragment()
                true
            }
            R.id.navLogout -> {
                logout()
                true
            }
            else -> false
        }
    }

    private fun setupBottomNavView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener(this)
    }
}