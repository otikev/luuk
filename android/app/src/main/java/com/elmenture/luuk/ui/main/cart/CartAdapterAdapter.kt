package com.elmenture.luuk.ui.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmenture.luuk.databinding.ItemCartBinding
import models.Spot
import utils.MiscUtils

class CartAdapterAdapter(var list: ArrayList<Spot>, var cartActionListener: CartActionListener) :
    RecyclerView.Adapter<CartAdapterAdapter.ViewHolder>() {

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

        fun bind(item: Spot, actionListener: CartActionListener) {
            val context = view.root.context
            view.tvDescription.text = item.description
            view.tvSize.text = "Size ${item.sizeNumber}"
            view.tvPrice.text ="ksh ${MiscUtils.getFormattedAmount(item.priceCents.toDouble()/100)}"
            Glide.with(context).load(item.url).into(view.ivItemImage)

            view.ivDiscard.setOnClickListener { actionListener.onDiscardClicked(item) }
            view.btnSaveForLater.setOnClickListener { actionListener.onSaveForLaterClicked(item) }
        }
    }

    interface CartActionListener{
       fun onSaveForLaterClicked(spot: Spot)
        fun onDiscardClicked(spot: Spot)
    }

}