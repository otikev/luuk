package com.elmenture.luuk.ui.main.accountmanagement.fulfillment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentFulfillmentBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Order
import utils.OrderState

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FulfillmentFragment : BaseFragment(), Type.ProfileSettings,
    FUlfillmentAdapter.CartActionListener {
    lateinit var binding: FragmentFulfillmentBinding
    lateinit var fulfillmentViewModel: FulfillmentViewModel
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var orderList = arrayListOf<Order>()
    lateinit var orderAdapter: FUlfillmentAdapter
    val spinnerArray = ArrayList<String>()

    companion object {
        fun newInstance() = FulfillmentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFulfillmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun setupStatesSpinner() {
        for (state in OrderState.values()) {
            spinnerArray.add(state.name)
        }

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_1,
            spinnerArray
        )

        binding.spnState.adapter = spinnerAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupStatesSpinner()
        observeLiveData()
        setEventListeners()
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
        binding.spnState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                fetchItems()
            }
        }
    }

    private fun fetchItems() {
        val state = binding.spnState.selectedItem.toString()
        fulfillmentViewModel.fetchOrders(state)
    }

    private fun observeLiveData() {
        fulfillmentViewModel.ordersApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    orderList.clear()
                    orderList.addAll(it.data as List<Order>)
                    orderAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun initView() {
        fulfillmentViewModel = ViewModelProvider(this).get(FulfillmentViewModel::class.java)
        orderAdapter = FUlfillmentAdapter(orderList, this)
        binding.rvOrders.layoutManager = LinearLayoutManager(context)
        binding.rvOrders.adapter = orderAdapter
    }

    override fun onNextClicked(order: Order) {
        activityView.startOrderItemsFragment(order.id!!)
    }
}