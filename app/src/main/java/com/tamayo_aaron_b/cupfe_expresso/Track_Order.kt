package com.tamayo_aaron_b.cupfe_expresso

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firestore.v1.StructuredQuery
import com.tamayo_aaron_b.cupfe_expresso.summary.OrderAdapter
import com.tamayo_aaron_b.cupfe_expresso.tracking.TrackAdapter
import com.tamayo_aaron_b.cupfe_expresso.tracking.TrackConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Track_Order : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var recyclerViewTracking: RecyclerView
    private lateinit var adapter: TrackAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track_order)

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {
            finish()
        }

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        recyclerViewTracking = findViewById(R.id.recyclerViewTracking)

        adapter = TrackAdapter(null)
        recyclerViewTracking.layoutManager = LinearLayoutManager(this)
        recyclerViewTracking.adapter = adapter

        searchButton.setOnClickListener {
            val refNumber = searchEditText.text.toString().trim()
            if (refNumber.isNotEmpty()) {
                searchOrder(refNumber)
            } else {
                Toast.makeText(this, "Enter a reference number", Toast.LENGTH_SHORT).show()
            }
        }
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            val refNumber = searchEditText.text.toString().trim()
            if (refNumber.isNotEmpty()) {
                searchOrder(refNumber)
            } else {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this, "Enter a reference number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchOrder(reference_number: String) {
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient.instance.searchOrderByReference(reference_number)
            .enqueue(object : Callback<TrackConnection> {
                override fun onResponse(
                    call: Call<TrackConnection>,
                    response: Response<TrackConnection>
                ) {
                    swipeRefreshLayout.isRefreshing = false
                    if (response.isSuccessful && response.body() != null) {
                        adapter.updateOrder(response.body())
                    } else {
                        Toast.makeText(this@Track_Order, "Order not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TrackConnection>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this@Track_Order, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}