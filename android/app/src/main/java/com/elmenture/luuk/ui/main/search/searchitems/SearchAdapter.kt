package com.elmenture.luuk.ui.main.search.searchitems

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elmenture.luuk.databinding.SearchItemBinding
import models.TagProperty

class SearchAdapter(
    private var mainlist: ArrayList<TagProperty>,
    var itemActionListener: CartActionListener
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private var filterlist = ArrayList<TagProperty>()
    init {
        filterlist.addAll(mainlist)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tagProperty = filterlist[position]
        holder.bind(tagProperty, itemActionListener)
    }

    override fun getItemCount(): Int {
        return filterlist.size
    }

    class ViewHolder(private val view: SearchItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(tagProperty: TagProperty, itemActionListener: CartActionListener) {
            view.tvDescription.text = tagProperty.value
            view.root.setOnClickListener { itemActionListener.onItemClicked(tagProperty) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        filterlist.clear()

        for (tagProperty: TagProperty in mainlist) {
            if (tagProperty.value!!.contains(query)) filterlist.add(tagProperty)
        }
        notifyDataSetChanged()
    }

    interface CartActionListener {
        fun onItemClicked(tagProperty: TagProperty)
    }

}