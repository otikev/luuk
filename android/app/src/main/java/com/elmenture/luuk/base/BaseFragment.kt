package com.elmenture.luuk.base

import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

    open fun onResumeFromBackstack() {
    }
}