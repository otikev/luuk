package com.elmenture.luuk.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseActivityView
import com.elmenture.luuk.ui.main.MainActivityView


abstract class BaseFragment : Fragment() {

    open fun onResumeFromBackstack() {
    }
}