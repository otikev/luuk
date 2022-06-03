package com.elmenture.luuk.ui.main.accountmanagement.fulfillment

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FulfillmentOrderItemsBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.cart.CartAdapter
import models.Item
import models.Order
import models.OrderStateUpdate
import models.Spot
import utils.OrderState

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FulfillmentOrderItemsFragment : BaseFragment(), Type.ProfileSettings {
    lateinit var binding: FulfillmentOrderItemsBinding
    lateinit var fulfillmentViewModel: FulfillmentViewModel
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var orderItemList = arrayListOf<Spot>()
    lateinit var orderItemAdapter: CartAdapter
    lateinit var order: Order
    val spinnerArray = ArrayList<String>()

    companion object {
        fun newInstance(order: Order): FulfillmentOrderItemsFragment {
            val frag = FulfillmentOrderItemsFragment()
            frag.order = order
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FulfillmentOrderItemsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupStatesSpinner()
        observeLiveData()
        setEventListeners()
        fulfillmentViewModel.fetchOrderItems(order.id!!)
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

        binding.spnState.setSelection(OrderState.valueOf(order.state!!.uppercase()).ordinal)
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
                val newState = binding.spnState.selectedItem.toString()
                if (newState.lowercase() == order.state!!.lowercase()) {
                    binding.btnUpdateState.visibility = GONE
                }else{
                    binding.btnUpdateState.visibility = VISIBLE
                }
            }
        }
        binding.btnUpdateState.setOnClickListener{
            val newState = binding.spnState.selectedItem.toString()
            val orderState = OrderStateUpdate()
            orderState.orderId = order.id!!.toLong()
            orderState.newState = newState
            fulfillmentViewModel.updateOrderState(orderState)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        fulfillmentViewModel.ordersItemsApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    orderItemList.clear()
                    val spots: ArrayList<Spot> = arrayListOf()
                    (it.data as List<Item>).map { item -> spots.add(createSpot(item)) }
                    orderItemList.addAll(spots)
                    orderItemAdapter.notifyDataSetChanged()
                }
            }
        }

        fulfillmentViewModel.updateOrderStateApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    showDialog("Order state updated")
                }else{
                    showDialog("Could not update the order state")
                }
            }
        }
    }


    private fun initView() {
        fulfillmentViewModel = ViewModelProvider(this).get(FulfillmentViewModel::class.java)
        orderItemAdapter = CartAdapter(orderItemList)
        binding.rvOrderItems.layoutManager = LinearLayoutManager(context)
        binding.rvOrderItems.adapter = orderItemAdapter
    }

    private fun createSpot(item: Item): Spot {
        return Spot(
            url = item.imageUrl!!,
            priceCents = item.price!!,
            sizeType = item.sizeType!!,
            sizeInternational = item.sizeInternational,
            sizeNumber = item.sizeNumber,
            itemId = item.id!!,
            description = item.description!!,
            tagProperties = item.tagProperties!!
        )
    }

}