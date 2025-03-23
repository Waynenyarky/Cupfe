package com.tamayo_aaron_b.cupfe_expresso.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tamayo_aaron_b.cupfe_expresso.R

class CoffeeAdapter(private val coffees: List<Coffee>) :
    RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coffee, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val product = coffees[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = coffees.size

    class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val subcategory: TextView = itemView.findViewById(R.id.subcategory)

        fun bind(coffee: Coffee) {
            Glide.with(itemView.context)
                .load(coffee.imageUrl)
                .into(image)
            name.text = coffee.name
            price.text = "$${coffee.price}"
            subcategory.text = coffee.subcategory
        }
    }
}