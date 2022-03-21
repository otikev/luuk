package com.elmenture.luuk.ui.main.accountmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elmenture.luuk.databinding.FragmentAccountManagementBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.kokonetworks.kokosasa.base.BaseFragment

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AccountManagementFragment : BaseFragment() {
    lateinit var binding: FragmentAccountManagementBinding
    private val activityView by lazy { activity as MainActivityView }

    companion object {
        fun newInstance() = AccountManagementFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountManagementBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEventListeners()
    }

    private fun setUpEventListeners() {
        binding.itemMySizes.setOnClickListener {
            activityView.startMySizesFragment()
        }
    }

}