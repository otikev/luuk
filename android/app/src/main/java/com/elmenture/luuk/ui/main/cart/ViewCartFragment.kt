package com.elmenture.luuk.ui.main.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentViewCartBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Spot
import utils.MiscUtils

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ViewCartFragment : BaseFragment(), CartAdapter.CartActionListener {
    lateinit var binding: FragmentViewCartBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var cartList: ArrayList<Spot> = ArrayList()
    lateinit var cartAdapter: CartAdapter
    private lateinit var viewModel: CartViewModel


    companion object {
        fun newInstance() = ViewCartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewCartBinding.inflate(inflater, container, false)
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
                cartList.clear()
                cartList.addAll(it)
                cartAdapter.notifyDataSetChanged()

                binding.btnCheckout.text = "Checkout ${it.size} Items"

                if(it.isNotEmpty()){
                    binding.llSubtotal.visibility = View.VISIBLE
                    binding.tvSubtotal.text = "Ksh ${MiscUtils.getFormattedAmount(calculateSubTotal(it))}"
                }else{
                    binding.llSubtotal.visibility = View.GONE
                }
            }
        }

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
        cartAdapter = CartAdapter(cartList,this)
        binding.rvCart.layoutManager = LinearLayoutManager(context)
        binding.rvCart.adapter = cartAdapter
    }

    private fun setUpEventListeners() {

    }

    override fun onSaveForLaterClicked(spot: Spot) {
        val cart = viewModel.cartItemsLiveData.value
        cart?.remove(spot)
         viewModel.updateCart(cart)
    }

    override fun onDiscardClicked(spot: Spot) {
        val cart = viewModel.cartItemsLiveData.value
        cart?.remove(spot)
        viewModel.updateCart(cart)
    }

}