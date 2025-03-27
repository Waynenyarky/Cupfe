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

class OrderAdapter(private val orderList: List<OrderItem>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coffeeName: TextView = view.findViewById(R.id.name)
        val selectedSize: TextView = view.findViewById(R.id.size)
        val quantity: TextView = view.findViewById(R.id.quantity)
        val userComment: TextView = view.findViewById(R.id.comment)
        val price: TextView = view.findViewById(R.id.price)
        val imageView: ImageView = view.findViewById(R.id.image)
        val tvMinus: TextView = view.findViewById(R.id.tvMinus)
        val tvAdd: TextView = view.findViewById(R.id.tvAdd)
        val subTotal: TextView = view.findViewById(R.id.subTotal)
        val Total: TextView = view.findViewById(R.id.Total) // Ensure this is only in the last item
        val btnDineIn: TextView = view.findViewById(R.id.btnDineIn)
        val btnTakeOut: TextView = view.findViewById(R.id.btnTakeOut)
        val PayNow: TextView = view.findViewById(R.id.PayNow)
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

        // Calculate subtotal
        val subTotalValue = order.price * order.quantity
        holder.subTotal.text = "₱${String.format(Locale.US, "%.2f", subTotalValue)}"

        // Update total price
        updateTotal(holder.Total)

        // Decrease quantity
        holder.tvMinus.setOnClickListener {
            if (order.quantity > 1) {
                order.quantity--
                holder.quantity.text = order.quantity.toString()
                val newSubTotal = order.price * order.quantity
                holder.subTotal.text = "₱${String.format(Locale.US, "%.2f", newSubTotal)}"
                updateTotal(holder.Total)
            }
        }

        // Increase quantity
        holder.tvAdd.setOnClickListener {
            order.quantity++
            holder.quantity.text = order.quantity.toString()
            val newSubTotal = order.price * order.quantity
            holder.subTotal.text = "₱${String.format(Locale.US, "%.2f", newSubTotal)}"
            updateTotal(holder.Total)
        }

        // Set default selection to Dine-In
        holder.btnDineIn.isSelected = true
        holder.btnTakeOut.isSelected = false
        holder.btnDineIn.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        holder.btnTakeOut.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))

        // Handle Dine-In and Take-Out button clicks
        holder.btnDineIn.setOnClickListener {
            holder.btnDineIn.isSelected = true
            holder.btnTakeOut.isSelected = false
            holder.btnDineIn.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.btnTakeOut.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }

        holder.btnTakeOut.setOnClickListener {
            holder.btnDineIn.isSelected = false
            holder.btnTakeOut.isSelected = true
            holder.btnDineIn.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.btnTakeOut.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }

        holder.PayNow.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Receipts::class.java)

            // Generate Transaction ID
            val transactionId = generateTransactionId()

            // Get current date and time
            val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

            // Determine order type
            val orderType = if (holder.btnDineIn.isSelected) "Dine-In" else "Take-Out"

            // Get order details
            val coffeeId = order.id
            val coffeeName = order.coffeeName
            val size = order.selectedSize
            val quantity = order.quantity
            val price = order.price
            val comment = order.userComment
            val subTotal = order.price * order.quantity
            val total = orderList.sumOf { it.price * it.quantity }

            // Pass data to PaymentActivity
            intent.apply {
                putExtra("transactionId", transactionId)
                putExtra("date", currentDate)
                putExtra("time", currentTime)
                putExtra("orderType", orderType)
                putExtra("coffeeId", coffeeId)
                putExtra("coffeeName", coffeeName)
                putExtra("size", size)
                putExtra("quantity", quantity)
                putExtra("price", price)
                putExtra("comment", comment)
                putExtra("subTotal", subTotal)
                putExtra("total", total)
            }

            // Start PaymentActivity
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = orderList.size

    private fun updateTotal(totalView: TextView) {
        val totalPrice = orderList.sumOf { it.price * it.quantity }
        totalView.text = "₱${String.format(Locale.US, "%.2f", totalPrice)}"
    }

    private fun generateTransactionId(): String {
        val random = Random()
        val letter = ('A'..'Z').random() // Random uppercase letter
        val numbers = (1..9).map { random.nextInt(10) }.joinToString("") // 9 random numbers
        return "$letter$numbers"
    }
}