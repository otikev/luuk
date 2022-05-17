package com.elmenture.luuk.ui.main.accountmanagement.orderhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentOrderHistoryBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OrderHistoryFragment : BaseFragment(), Type.ProfileSettings {
    lateinit var binding: FragmentOrderHistoryBinding
    lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var orderList = arrayListOf<Order>()
    lateinit var orderAdapter: OrderHistoryAdapter

    companion object {
        fun newInstance() = OrderHistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setEventListeners()
        orderHistoryViewModel.fetchAllOrders()
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener{requireActivity().onBackPressed()}
    }

    private fun observeLiveData() {
        orderHistoryViewModel.ordersApiState.observe(viewLifecycleOwner){
            it?.let {
                if(it.isSuccessful){
                    orderList.clear()
                    orderList.addAll(it.data as List<Order>)
                    orderAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun initView() {
        orderHistoryViewModel = ViewModelProvider(this).get(OrderHistoryViewModel::class.java)
        orderAdapter = OrderHistoryAdapter(orderList)
        binding.rvOrders.layoutManager = LinearLayoutManager(context)
        binding.rvOrders.adapter = orderAdapter
    }
}