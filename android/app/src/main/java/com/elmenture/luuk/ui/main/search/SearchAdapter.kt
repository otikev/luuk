package com.elmenture.luuk.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmenture.luuk.databinding.SearchItemBinding
import models.TagProperty

class SearchAdapter(var list: ArrayList<TagProperty>, var itemActionListener: CartActionListener) :
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
        val tagProperty = list[position]
        holder.bind(tagProperty, itemActionListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(private val view: SearchItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(tagProperty: TagProperty, itemActionListener: CartActionListener) {
            view.tvDescription.text = tagProperty.value
            view.searchContainer.setOnClickListener { itemActionListener.onItemClicked(tagProperty) }
        }
    }

    interface CartActionListener{
        fun onItemClicked(tagProperty: TagProperty)
    }

}