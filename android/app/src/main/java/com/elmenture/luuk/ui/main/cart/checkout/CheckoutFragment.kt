package com.elmenture.luuk.ui.main.cart.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.databinding.FragmentCheckoutBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.cart.CartViewModel
import models.Item
import models.Spot
import utils.MiscUtils

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CheckoutFragment : BaseFragment(), Type.Cart {
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

        viewModel.paymentStatus.observe(viewLifecycleOwner) { paymentStatus ->
            when (paymentStatus) {
                CartViewModel.PaymentStatus.RequestSentState -> {
                    binding.btnPayNow.text = "Confirm Payment"
                }
                CartViewModel.PaymentStatus.RequestFailedState -> {
                    activityView.startCheckoutFailureFragment()
                }
                CartViewModel.PaymentStatus.ItemsSoldState -> {
                    handleItemsSoldState()
                }
            }
        }

        viewModel.profileDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is CartViewModel.ProfileStatus.Complete -> {
                    binding.tvChange.text = MiscUtils.getSpannedText(
                        getString(
                            R.string.update_details,
                            "Change Details"
                        )
                    )
                    binding.btnPayNow.isEnabled = true
                    binding.tvChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                is CartViewModel.ProfileStatus.Incomplete -> {
                    binding.tvChange.text = MiscUtils.getSpannedText(
                        getString(
                            R.string.update_details,
                            "Update Details To Proceed"
                        )
                    )
                    binding.btnPayNow.isEnabled = false
                    binding.tvChange.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_alert_triangle,
                        0
                    );

                }
            }
        }

        viewModel.orderConfirmationApiState.observe(viewLifecycleOwner) { orderState ->
            orderState?.let {
                if (orderState.isSuccessful) {
                    activityView.startCheckoutSuccessFragment()
                    viewModel.clearCartData();
                } else {
                    activityView.startCheckoutFailureFragment()
                }
            }
        }

    }

    private fun handleItemsSoldState() {
        Toast.makeText(requireContext(),"Removing already sold items from cart",Toast.LENGTH_LONG).show();
        activityView.startViewCartFragment()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView(set: MutableSet<Spot>) {
        val userData = LocalRepository.userDetailsLiveData.value
        binding.tvLocation.text = userData?.physicalAddress
        binding.tvName.text = userData?.name
        binding.tvAmount.text = "Ksh ${MiscUtils.getFormattedAmount(calculateSubTotal(set))}"
        binding.tvSubtotalAmount.text =
            "Ksh ${MiscUtils.getFormattedAmount(calculateSubTotal(set))}"
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
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
        binding.tvChangeOrder.setOnClickListener { requireActivity().onBackPressed() }
        binding.tvChange.setOnClickListener { activityView.startProfileSettingsFragment() }

        binding.btnPayNow.setOnClickListener {
            if (viewModel.paymentStatus.value == CartViewModel.PaymentStatus.RequestSentState) {
                viewModel.confirmOrder()
            } else {
                viewModel.validateCartItems()
            }
        }
    }

}