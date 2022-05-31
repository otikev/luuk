package com.elmenture.luuk.ui.main.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmenture.luuk.databinding.ItemCartBinding
import models.Spot
import utils.MiscUtils

class CartAdapter(var list: ArrayList<Spot>, var cartActionListener: CartActionListener? = null) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, cartActionListener)
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }

    class ViewHolder(private val view: ItemCartBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(item: Spot, actionListener: CartActionListener?) {
            val context = view.root.context
            view.tvDescription.text = item.description
            view.tvPrice.text =
                "ksh ${MiscUtils.getFormattedAmount(item.priceCents.toDouble() / 100)}"
            if (item.sizeType.equals("INT", true)) {
                view.tvSize.text = "Size ${item.sizeInternational?.uppercase()}"
            } else {
                view.tvSize.text = "Size ${item.sizeNumber.toString()}(${item.sizeType})"
            }
            Glide.with(context).load(item.url).into(view.ivItemImage)

            actionListener?.let {
                view.ivDiscard.visibility = View.VISIBLE
                view.btnSaveForLater.visibility = View.VISIBLE
                view.ivDiscard.setOnClickListener { actionListener.onDiscardClicked(item) }
                view.btnSaveForLater.setOnClickListener { actionListener.onSaveForLaterClicked(item) }
            }

        }
    }

    interface CartActionListener {
        fun onSaveForLaterClicked(spot: Spot)
        fun onDiscardClicked(spot: Spot)
    }

}