package com.tamayo_aaron_b.cupfe_expresso.receiptsAllKind


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tamayo_aaron_b.cupfe_expresso.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AllTransactionsAdapter : RecyclerView.Adapter<AllTransactionsAdapter.ReceiptViewHolder>() {

    private val receipts = mutableListOf<AllTransactionsConnection>()

    inner class ReceiptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reference_number: TextView = itemView.findViewById(R.id.txtReference)
        private val email: TextView = itemView.findViewById(R.id.email)
        private val receipt_text: TextView = itemView.findViewById(R.id.txtMessage)
        private val receipt_for: TextView = itemView.findViewById(R.id.txtStatus)
        private val created_at: TextView = itemView.findViewById(R.id.Created)

        fun bind(receipt: AllTransactionsConnection) {
            reference_number.text = "Ref No. ${receipt.reference_number}"
            email.text = receipt.email
            receipt_text.text = receipt.receipt_text
            receipt_for.text = "Your receipt is for ${receipt.receipt_for}"
            created_at.text = formatDate(receipt.created_at)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.receipts_layout, parent, false)
        return ReceiptViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        holder.bind(receipts[holder.adapterPosition])
    }

    override fun getItemCount(): Int = receipts.size

    fun setData(newReceipts: List<AllTransactionsConnection>) {
        receipts.clear()
        receipts.addAll(newReceipts)
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


