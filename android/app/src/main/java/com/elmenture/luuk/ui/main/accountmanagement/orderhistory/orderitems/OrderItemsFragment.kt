package com.elmenture.luuk.ui.main.accountmanagement.orderhistory.orderitems

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentOrderItemsBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.accountmanagement.orderhistory.OrderHistoryViewModel
import com.elmenture.luuk.ui.main.cart.CartAdapter
import models.Item
import models.Spot

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class OrderItemsFragment : BaseFragment(), Type.ProfileSettings {
    lateinit var binding: FragmentOrderItemsBinding
    lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var orderItemList = arrayListOf<Spot>()
    lateinit var orderItemAdapter: CartAdapter
    var orderId: Int = 0

    companion object {
        fun newInstance(orderId: Int): OrderItemsFragment {
            val frag = OrderItemsFragment()
            frag.orderId = orderId
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderItemsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setEventListeners()
        orderHistoryViewModel.fetchOrdersItems(orderId)
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        orderHistoryViewModel.ordersItemsApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    val spots: ArrayList<Spot> = arrayListOf()
                    (it.getData<List<Item>>()).map { item -> spots.add(createSpot(item)) }
                    orderItemList.addAll(spots)
                    orderItemAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun initView() {
        orderHistoryViewModel = ViewModelProvider(this).get(OrderHistoryViewModel::class.java)
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