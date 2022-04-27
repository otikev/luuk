package com.elmenture.luuk.ui.main.cart.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.databinding.FragmentCheckoutBinding
import com.elmenture.luuk.databinding.FragmentViewCartBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.cart.CartViewModel
import models.Spot
import utils.MiscUtils

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CheckoutFragment : BaseFragment(){
    lateinit var binding: FragmentCheckoutBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private lateinit var viewModel: CartViewModel

    companion object {
        fun newInstance() = CheckoutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpEventListeners()
        observeLiveData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        viewModel.cartItemsLiveData.observe(viewLifecycleOwner) { swipeRecords ->
            swipeRecords?.let {
                updateView(it)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateView(set: MutableSet<Spot>) {
        val userData = LocalRepository.userDetailsLiveData.value
        binding.tvLocation.text = userData?.physicalAddress
        binding.tvAmount.text = "Ksh ${MiscUtils.getFormattedAmount(calculateSubTotal(set))}"
        binding.tvItemCount.text = set.size.toString()
        binding.tvPhoneNumber.text = userData?.contactPhoneNumber
    }

    private fun calculateSubTotal(cart: MutableSet<Spot>): Double {
        var sum = 0
        for (spot: Spot in cart) {
            sum += spot.priceCents.toInt() / 100
        }
        return sum.toDouble()
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
    }

    private fun setUpEventListeners() {
        binding.toolBar.setNavClickListener{ requireActivity().onBackPressed()}
        binding.tvChangeOrder.setOnClickListener{ requireActivity().onBackPressed()}
        binding.tvChangeLocation.setOnClickListener{ activityView.startProfileSettingsFragment()}
        binding.btnPayNow.setOnClickListener { viewModel.validateCartItems() }
    }

}