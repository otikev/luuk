package com.elmenture.luuk.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmenture.luuk.databinding.SearchItemBinding
import com.elmenture.luuk.ui.main.cart.CartAdapter
import models.Item

class SearchAdapter(var list: ArrayList<Item>, var itemActionListener: CartActionListener) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchItemBinding.inflate(
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

    class ViewHolder(private val view: SearchItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: Item, itemActionListener: CartActionListener) {
            view.tvDescription.text = item.description
            view.searchContainer.setOnClickListener { itemActionListener.onItemClicked(item) }
        }
    }

    interface CartActionListener{
        fun onItemClicked(item: Item)
    }

}