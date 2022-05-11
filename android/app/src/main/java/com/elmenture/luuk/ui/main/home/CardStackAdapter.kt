package com.elmenture.luuk.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmenture.luuk.R
import com.elmenture.luuk.data.ItemQueue
import models.Spot

class CardStackAdapter : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private var itemClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    fun setItemClickListener(listener:View.OnClickListener){
        this.itemClickListener = listener
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = ItemQueue.spots[position]
        val description: String = spot.description

        holder.description.text = description
        Glide.with(holder.image)
            .load(spot.url)
            .into(holder.image)

        this.itemClickListener?.let {
            holder.itemView.setOnClickListener(it)
        }
    }

    override fun getItemCount(): Int {
        return ItemQueue.spots.size
    }

    fun getSpots(): List<Spot> {
        return ItemQueue.spots
    }

    fun getItem(position:Int): Spot {
        return ItemQueue.spots[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var description: TextView = view.findViewById(R.id.item_price)
        var image: ImageView = view.findViewById(R.id.item_image)
    }

}
