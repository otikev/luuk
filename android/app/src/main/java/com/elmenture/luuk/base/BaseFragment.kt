package com.kokonetworks.kokosasa.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseActivityView

abstract class BaseFragment : Fragment() {
    private lateinit var activity: BaseActivityView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getBaseActivityView()
    }

    protected fun getBaseActivityView(): BaseActivityView {
        return getActivity() as BaseActivityView
    }


    open fun onResumeFromBackstack() {
    }
}