package com.tamayo_aaron_b.cupfe_expresso.tracking


import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tamayo_aaron_b.cupfe_expresso.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class TrackAdapter(private var order: TrackConnection?) :
    RecyclerView.Adapter<TrackAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reference_number: TextView = view.findViewById(R.id.txtReference)
        val email: TextView = view.findViewById(R.id.email)
        val username: TextView = view.findViewById(R.id.username)
        val total_amount: TextView = view.findViewById(R.id.totalAmount)
        val status: TextView = view.findViewById(R.id.status)
        val promo_code: TextView = view.findViewById(R.id.txtMessage)
        val order_type: TextView = view.findViewById(R.id.orderType)
        val payment_status: TextView = view.findViewById(R.id.txtStatus)
        val payment_method: TextView = view.findViewById(R.id.paymentMethod)
        val created_at: TextView = view.findViewById(R.id.Created)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_track_layout, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        order?.let {
            holder.reference_number.text = "Ref No. ${it.reference_number}"
            holder.username.text =  Html.fromHtml("<b>Name: </b>${it.username}", Html.FROM_HTML_MODE_LEGACY)
            holder.email.text = Html.fromHtml("<b>Email: </b>${it.email}", Html.FROM_HTML_MODE_LEGACY)
            holder.total_amount.text = Html.fromHtml("<b>Total Amount: </b> ₱${it.total_amount}", Html.FROM_HTML_MODE_LEGACY)
            holder.promo_code.text = Html.fromHtml("<b>Promo Code: </b>${it.promo_code}", Html.FROM_HTML_MODE_LEGACY)
            holder.order_type.text = Html.fromHtml("<b>Order Type: </b>${it.order_type}", Html.FROM_HTML_MODE_LEGACY)
            holder.payment_method.text = Html.fromHtml("<b>Payment Method: </b>${it.payment_method}", Html.FROM_HTML_MODE_LEGACY)
            holder.payment_status.text = Html.fromHtml("<b>Payment Status: </b> ${it.payment_status}", Html.FROM_HTML_MODE_LEGACY)
            holder.created_at.text = formatDate(it.created_at)

            // Set text color based on status
            when (it.status.lowercase()) {
                "pending" -> holder.status.setTextColor(Color.parseColor("#Ff8c00")) // Orange
                "preparing" -> holder.status.setTextColor(Color.parseColor("#B58B00")) // Yellow
                "serving" -> holder.status.setTextColor(Color.parseColor("#0000FF")) // Blue
                "completed" -> holder.status.setTextColor(Color.parseColor("#00b300")) // Green
                "canceled" -> holder.status.setTextColor(Color.parseColor("#8b0000")) // Red
                else -> holder.status.setTextColor(Color.BLACK) // Default
            }

            holder.status.text = Html.fromHtml("<b>Status: </b>${it.status}", Html.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun getItemCount(): Int = if (order != null) 1 else 0

    fun updateOrder(newOrder: TrackConnection?) {
        order = newOrder
        notifyDataSetChanged()
    }

    private fun formatDate(dateString: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            val date = sdf.parse(dateString) ?: return dateString
            val now = Date()

            val diffInMillis = now.time - date.time
            when (val daysAgo = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()) {
                0 -> "Today"
                1 -> "Yesterday"
                else -> "$daysAgo Days ago"
            }
        } catch (e: Exception) {
            dateString // Return original date if parsing fails
        }
    }
}