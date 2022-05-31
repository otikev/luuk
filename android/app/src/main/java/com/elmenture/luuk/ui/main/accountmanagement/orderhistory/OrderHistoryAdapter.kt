package com.elmenture.luuk.ui.main.accountmanagement.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmenture.luuk.databinding.OrderHistoryItemBinding
import models.Order
import utils.MiscUtils

class OrderHistoryAdapter(var list: ArrayList<Order>, var cardActionListener: CartActionListener) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = OrderHistoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, cardActionListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(private val view: OrderHistoryItemBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(order: Order, cardActionListener: CartActionListener) {

            var amount = 0L;

            for (orderItem in order.orderItems!!) {
                amount += orderItem?.item?.price!!
            }
            val context = view.root.context
            view.tvStatus.text = order.state
            view.tvPrice.text = "KES ${MiscUtils.getFormattedAmount(amount.toDouble() / 100)}"
            Glide.with(context).load(order.orderItems?.getOrNull(0)?.item?.imageUrl)
                .into(view.ivItemImage)
            view.tvCount.text = "${order.orderItems?.count()} Items"

            view.ivNext.setOnClickListener { cardActionListener.onNextClicked(order)}
        }

    }

    interface CartActionListener {
        fun onNextClicked(order: Order)
    }
}


