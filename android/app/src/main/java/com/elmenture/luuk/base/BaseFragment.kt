package com.elmenture.luuk.base

import androidx.fragment.app.Fragment
import com.elmenture.luuk.ui.main.MainActivity
import com.elmenture.luuk.ui.main.MainActivityView
import utils.LogUtils


abstract class BaseFragment : Fragment() {

    @JvmField
    protected var logUtils = LogUtils(this.javaClass)

    open fun onResumeFromBackstack() {
        (requireActivity() as MainActivityView).handleBottomNav(this)
    }

    fun showProgress(){
        val activity:BaseActivity = activity as BaseActivity
        activity.showProgressDialog()
    }

    fun hideProgress(){
        val activity:BaseActivity = activity as BaseActivity
        activity.hideProgressDialog()
    }
}