package com.tamayo_aaron_b.cupfe_expresso

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamayo_aaron_b.cupfe_expresso.receiptsAllKind.AllTransactionsAdapter
import com.tamayo_aaron_b.cupfe_expresso.receiptsAllKind.AllTransactionsConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptsForAll : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllTransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receipts_for_all)

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        ivBack.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView) // Replace with your actual RecyclerView ID
        adapter = AllTransactionsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // Set layout manager

        fetchReceipts() // Fetch data from API
    }

    private fun fetchReceipts() {
        RetrofitClient.instance.getReceipts().enqueue(object : Callback<List<AllTransactionsConnection>> {
            override fun onResponse(call: Call<List<AllTransactionsConnection>>, response: Response<List<AllTransactionsConnection>>) {
                if (response.isSuccessful) {
                    response.body()?.let { adapter.setData(it) }
                }
            }

            override fun onFailure(call: Call<List<AllTransactionsConnection>>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch receipts", t)
            }
        })
    }
}