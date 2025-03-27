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
import com.tamayo_aaron_b.cupfe_expresso.summary.OrderAdapter
import com.tamayo_aaron_b.cupfe_expresso.summary.OrderItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class Order_Summary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_summary)

        val orderItem: OrderItem? = intent.getParcelableExtra("orderItem")

        val recyclerView: RecyclerView = findViewById(R.id.orderSummary)
        recyclerView.layoutManager = LinearLayoutManager(this)

        orderItem?.let {
            val orderList = listOf(it) // Wrap in a list
            recyclerView.adapter = OrderAdapter(orderList)
        }

        val ivBack = findViewById<ImageView>(R.id.ivBack)


        // Back Home
        ivBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }



    }
}