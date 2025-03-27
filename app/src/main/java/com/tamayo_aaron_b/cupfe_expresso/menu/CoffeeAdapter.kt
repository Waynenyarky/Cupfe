package com.tamayo_aaron_b.cupfe_expresso.menu

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tamayo_aaron_b.cupfe_expresso.R
import com.tamayo_aaron_b.cupfe_expresso.details_food1

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
        private val isAvailable: TextView = itemView.findViewById(R.id.unavailableText)

        fun bind(coffee: Coffee) {
            Glide.with(itemView.context)
                .load(coffee.imageUrl)
                .into(image)
            name.text = coffee.name
            price.text = "₱${coffee.priceSmall}"
            subcategory.text = coffee.subcategory

            if (coffee.isAvailable == 0) {
                // Mark as unavailable
                isAvailable.visibility = View.VISIBLE
                itemView.isClickable = false
                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "Item is unavailable", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Mark as available
                isAvailable.visibility = View.GONE
                itemView.isClickable = true
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, details_food1::class.java)
                intent.putExtra("coffee", coffee)  // Passing Parcelable object
                itemView.context.startActivity(intent)
//                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
                }
            }
        }
    }
}