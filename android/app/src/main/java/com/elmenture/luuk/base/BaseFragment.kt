package com.elmenture.luuk.base

import androidx.fragment.app.Fragment
import utils.LogUtils


abstract class BaseFragment : Fragment() {

    @JvmField
    protected var logUtils = LogUtils(this.javaClass)

    open fun onResumeFromBackstack() {
    }
}