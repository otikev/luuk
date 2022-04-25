package com.elmenture.luuk.ui.main.accountmanagement.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentHelpBinding
import com.elmenture.luuk.databinding.FragmentMySizesBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.BodyMeasurements
import models.ClothingSizes
import models.ActualMeasurements
import models.enums.InternationalSizes

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HelpFragment : BaseFragment() {
    lateinit var binding: FragmentHelpBinding

    companion object {
        fun newInstance() = HelpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        binding.toolBar.setNavClickListener{ requireActivity().onBackPressed()}
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.domStorageEnabled = true;

        binding.webView.loadUrl("https://luukatme.notion.site/Help-Center-a72368080d754a10844094e65f249bff")
    }
}