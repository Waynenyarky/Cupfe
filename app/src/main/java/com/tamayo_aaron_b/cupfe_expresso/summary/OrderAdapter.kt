package com.tamayo_aaron_b.cupfe_expresso.summary

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tamayo_aaron_b.cupfe_expresso.R
import com.tamayo_aaron_b.cupfe_expresso.Receipts
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class OrderAdapter(
    private val orderList: List<OrderItem>,
    private val onQuantityChanged: (List<OrderItem>) -> Unit // Callback to update UI
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coffeeName: TextView = view.findViewById(R.id.name)
        val selectedSize: TextView = view.findViewById(R.id.size)
        val quantity: TextView = view.findViewById(R.id.quantity)
        val userComment: TextView = view.findViewById(R.id.comment)
        val price: TextView = view.findViewById(R.id.price)
        val imageView: ImageView = view.findViewById(R.id.image)
        val tvMinus: TextView = view.findViewById(R.id.tvMinus)
        val tvAdd: TextView = view.findViewById(R.id.tvAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_summary_layout, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.coffeeName.text = order.coffeeName
        holder.selectedSize.text = "Size: ${order.selectedSize}"
        holder.quantity.text = "${order.quantity}"
        holder.userComment.text = "`` ${order.userComment}"
        holder.price.text = "₱${String.format(Locale.US, "%.2f", order.price)}"

        // Load image using Glide
        Glide.with(holder.imageView.context)
            .load(order.imageUrl)
            .into(holder.imageView)



        // Decrease quantity
        holder.tvMinus.setOnClickListener {
            if (order.quantity > 1) {
                order.quantity--
                holder.quantity.text = order.quantity.toString()
                onQuantityChanged(orderList) // Update UI in Activity
            }
        }

        // Increase quantity
        holder.tvAdd.setOnClickListener {
            order.quantity++
            holder.quantity.text = order.quantity.toString()
            onQuantityChanged(orderList) // Update UI in Activity
        }
    }

    override fun getItemCount() = orderList.size

}