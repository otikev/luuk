package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmenture.luuk.databinding.ItemInventoryBinding
import models.Item
import utils.MiscUtils
import java.util.*
import kotlin.collections.ArrayList

class InventoryAdapter(var list: ArrayList<Item>, var cartActionListener: CartActionListener) :
    RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemInventoryBinding.inflate(
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

    class ViewHolder(private val view: ItemInventoryBinding) :
        RecyclerView.ViewHolder(view.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Item, actionListener: CartActionListener) {
            val size = if(item.sizeNumber == null) item.sizeInternational else item.sizeNumber.toString()
            val context = view.root.context
            view.tvDescription.text = item.description
            view.tvSize.text = "${item.sizeType?.uppercase(Locale.getDefault())} $size"
            view.tvPrice.text ="ksh ${MiscUtils.getFormattedAmount(item.price?.toDouble()?.div(100))}"
            Glide.with(context).load(item.imageUrl).into(view.ivItemImage)

            view.ivDiscard.setOnClickListener { actionListener.onDiscardClicked(item) }
            view.btnEditItem.setOnClickListener { actionListener.onEditClicked(item) }
        }
    }

    interface CartActionListener{
       fun onEditClicked(item: Item)
        fun onDiscardClicked(item: Item)
    }

}