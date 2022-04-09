package com.elmenture.luuk.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elmenture.luuk.R
import models.Spot

class CardStackAdapter(
    private var spots: ArrayList<Spot> = arrayListOf()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    lateinit var spot: Spot

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        spot = spots[position]
        val description: String = spot.description

        holder.description.text = description
        Glide.with(holder.image)
            .load(spot.url)
            .into(holder.image)
        holder.itemView.setOnClickListener { v ->
            Toast.makeText(v.context, description, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return spots.size
    }

    fun setSpots(spots: List<Spot>) {
        this.spots = spots as ArrayList<Spot>
    }

    fun getSpots(): List<Spot> {
        return spots
    }

    fun getTopItem(): Spot {
        return spot
    }

    fun updateContent(spots: List<Spot>) {
        this.spots.clear()
        this.spots.addAll(spots)
        this.notifyItemRangeChanged(0, spots.count())
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var description: TextView = view.findViewById(R.id.item_price)
        var image: ImageView = view.findViewById(R.id.item_image)
    }

}
