package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamayo_aaron_b.cupfe_expresso.cartFolder.CartAdapter
import com.tamayo_aaron_b.cupfe_expresso.cartFolder.CartItem
import com.tamayo_aaron_b.cupfe_expresso.summary.OrderAdapter
import com.tamayo_aaron_b.cupfe_expresso.summary.OrderItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class Order_Summary : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private var orderList: MutableList<OrderItem> = mutableListOf()
    private lateinit var subTotalView: TextView
    private lateinit var totalView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_summary)


        val btnDineIn: TextView = findViewById(R.id.btnDineIn)
        val btnTakeOut: TextView = findViewById(R.id.btnTakeOut)
        val payNow: TextView = findViewById(R.id.PayNow)
        subTotalView = findViewById(R.id.subTotal)
        totalView = findViewById(R.id.Total)
        val orderItem: OrderItem? = intent.getParcelableExtra("orderItem")

        recyclerView = findViewById(R.id.orderSummary)
        recyclerView.layoutManager = LinearLayoutManager(this)

        orderItem?.let {
            val orderList = listOf(it) // Wrap in a list
            orderAdapter = OrderAdapter(orderList) { updatedOrderList ->
                updatePriceViews(updatedOrderList) // Update UI when quantity changes
            }
            recyclerView.adapter = orderAdapter
        }


        orderItem?.let {
            val price = it.price
            val quantity = it.quantity
            val subTotal = price * quantity // Calculate Subtotal
            val total = price * quantity // Calculate Total (if you have multiple items in the list)

            subTotalView.text = "₱${String.format("%.2f", subTotal)}" // Display Subtotal
            totalView.text = "₱${String.format("%.2f", total)}" // Display Total
        }

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        // Back Home
        ivBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }


        // Set default selection to Dine-In
        btnDineIn.isSelected = true
        btnTakeOut.isSelected = false
        btnDineIn.setTextColor(ContextCompat.getColor(this, R.color.white))
        btnTakeOut.setTextColor(ContextCompat.getColor(this, R.color.black))

        // Handle Dine-In and Take-Out button clicks
        btnDineIn.setOnClickListener {
            btnDineIn.isSelected = true
            btnTakeOut.isSelected = false
            btnDineIn.setTextColor(ContextCompat.getColor(this, R.color.white))
            btnTakeOut.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        btnTakeOut.setOnClickListener {
            btnDineIn.isSelected = false
            btnTakeOut.isSelected = true
            btnDineIn.setTextColor(ContextCompat.getColor(this, R.color.black))
            btnTakeOut.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        payNow.setOnClickListener {
            val context = this@Order_Summary // Use this context
            val intent = Intent(context, Receipts::class.java)

            // Generate Transaction ID
            val transactionId = generateTransactionId()

            // Get current date and time
            val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

            // Determine order type based on button selection
            val orderType = if (btnDineIn.isSelected) "Dine-In" else "Take-Out"

            // Collect order details from the list
            val coffeeId = orderItem?.id ?: 0
            val coffeeName = orderItem?.coffeeName ?: ""
            val size = orderItem?.selectedSize ?: ""
            val quantity = orderItem?.quantity ?: 0
            val price = orderItem?.price ?: 0.0
            val comment = orderItem?.userComment ?: ""
            val subTotal = price * quantity
            val total = price * quantity

            // Pass data to the Receipts activity
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

            // Start Receipts activity
            context.startActivity(intent)
        }

    }

    private fun generateTransactionId(): String {
        val random = Random()
        val numbers = (1..7).map { random.nextInt(10) }.joinToString("") // 7 random numbers
        return "ORD$numbers"
    }

    private fun updatePriceViews(updatedOrderList: List<OrderItem>) {
        // Calculate the new subtotal and total based on updated quantities
        val subTotal = updatedOrderList.sumOf { it.price * it.quantity }
        val total = updatedOrderList.sumOf { it.price * it.quantity } // Adjust if needed

        subTotalView.text = "₱${String.format("%.2f", subTotal)}"
        totalView.text = "₱${String.format("%.2f", total)}"
    }
}