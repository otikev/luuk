package com.elmenture.luuk.ui.main.cart.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentCheckoutFailureBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.cart.CartViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CheckoutFailureFragment : BaseFragment(), Type.Cart {
    lateinit var binding: FragmentCheckoutFailureBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private lateinit var viewModel: CartViewModel

    companion object {
        fun newInstance() = CheckoutFailureFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutFailureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpEventListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {

    }


    private fun initView() {
 }

    private fun setUpEventListeners() {
        binding.btnAccept.setOnClickListener { requireActivity().onBackPressed() }
    }


}