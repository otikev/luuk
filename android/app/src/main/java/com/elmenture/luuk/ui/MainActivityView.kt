package com.elmenture.luuk.ui

import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseActivityView

interface MainActivityView: BaseActivityView {
    fun startHomeFragment()
    fun startAccountManagementFragment()
    fun startMySizesFragment()
}