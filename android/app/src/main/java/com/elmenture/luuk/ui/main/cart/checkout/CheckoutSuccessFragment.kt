package com.elmenture.luuk.ui.main.cart.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentCheckoutSuccessBinding
import com.elmenture.luuk.ui.main.MainActivityView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CheckoutSuccessFragment : BaseFragment(), Type.Cart {
    lateinit var binding: FragmentCheckoutSuccessBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }

    companion object {
        fun newInstance() = CheckoutSuccessFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEventListeners()
    }

    private fun setUpEventListeners() {
        binding.tvTrackOrder.setOnClickListener{
            activityView.startAccountManagementFragment()
        }
    }

}