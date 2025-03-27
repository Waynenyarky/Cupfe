package com.tamayo_aaron_b.cupfe_expresso.favorite

import FavoriteItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tamayo_aaron_b.cupfe_expresso.R

class FavoriteAdapter(private val favoriteList: List<FavoriteItem>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coffeeName: TextView = view.findViewById(R.id.name1)
        val selectedSize: TextView = view.findViewById(R.id.size1)
        val quantity: TextView = view.findViewById(R.id.quantity1)
        val userComment: TextView = view.findViewById(R.id.comment1)
        val price: TextView = view.findViewById(R.id.price1)
        val imageView: ImageView = view.findViewById(R.id.image1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorate_layout, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val order = favoriteList[position]
        holder.coffeeName.text = order.coffeeName
        holder.selectedSize.text = "Size: ${order.selectedSize}"
        holder.quantity.text = "${order.quantity}"
        holder.userComment.text = "`` ${order.userComment}"
        holder.price.text = "₱${order.price}.00"

        // Load image using Glide (or Picasso)
        Glide.with(holder.imageView.context)
            .load(order.imageUrl)
            .into(holder.imageView)

        // Decrease quantity

    }



    override fun getItemCount() = favoriteList.size
}
