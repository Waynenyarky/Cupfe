package com.tamayo_aaron_b.cupfe_expresso.notificationFolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tamayo_aaron_b.cupfe_expresso.NotificationResponse
import com.tamayo_aaron_b.cupfe_expresso.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationAdapter(notifications: List<NotificationResponse>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private val sortedNotifications = notifications.sortedByDescending { it.created_at }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val email: TextView = itemView.findViewById(R.id.tvTitle)
        val message: TextView = itemView.findViewById(R.id.tvMessage)
        val created_at: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notif_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = sortedNotifications[position]
        holder.email.text = notification.email
        holder.message.text = notification.message
        holder.created_at.text =formatDate(notification.created_at)
    }

    override fun getItemCount(): Int = sortedNotifications.size

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