package com.elmenture.luuk.ui.main.search

import android.provider.Settings.Secure.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amazonaws.util.StringUtils
import com.bumptech.glide.Glide
import com.elmenture.luuk.R
import com.elmenture.luuk.databinding.ItemViewSearchBinding
import models.Item
import utils.MiscUtils

class ViewSearchedItemsAdapter(
    var list: ArrayList<Item>,
    var itemActionListener: CartActionListener
) :
    RecyclerView.Adapter<ViewSearchedItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemViewSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, itemActionListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(private val view: ItemViewSearchBinding) :
        RecyclerView.ViewHolder(view.root) {
        val context = view.root.context
        fun bind(item: Item, itemActionListener: CartActionListener) {
            view.tvDescription.text = item.description
            Glide.with(context).load(item.imageUrl)
                .into(view.ivImage)


            if (item.sizeType.equals("INT", true)) {
                view.tvSize.text = MiscUtils.getSpannedText(
                    context.getString(
                        R.string.contrast_text, "Size",
                        StringUtils.upperCase(item.sizeInternational)
                    )
                )
            } else {
                view.tvSize.text = MiscUtils.getSpannedText(
                    context.getString(
                        R.string.contrast_text,
                        "Size",
                        item.sizeNumber.toString() + "(" + item.sizeType + ")"
                    )
                )
            }
            view.tvPrice.text = "Ksh ${MiscUtils.getFormattedAmount(item.price!!.toDouble() / 100)}"
        }

    }

    interface CartActionListener {
        fun onItemClicked(item: Item)
    }
}