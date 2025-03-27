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

class FoodAdapter(private val foodList: List<Coffee>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]
        holder.bind(foodItem)
    }

    override fun getItemCount(): Int = foodList.size

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val name: TextView = itemView.findViewById(R.id.coffeename)
        private val price: TextView = itemView.findViewById(R.id.coffeePrice)
        private val subcategory: TextView = itemView.findViewById(R.id.coffeeSubcategory)
        private val isAvailable: TextView = itemView.findViewById(R.id.unavailableText)

        fun bind(food: Coffee) {
            Glide.with(itemView.context).load(food.imageUrl).into(image)
            name.text = food.name
            price.text = "₱${food.priceSmall}"
            subcategory.text = food.subcategory


            if (food.isAvailable == 0) {
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
                    intent.putExtra("coffee", food)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}