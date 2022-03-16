package com.elmenture.luuk.base

import androidx.fragment.app.Fragment

interface BaseActivityView {
    fun addFragment(fragment: Fragment, tag: String)
    fun replaceFragment(fragment: Fragment, tag: String)
}