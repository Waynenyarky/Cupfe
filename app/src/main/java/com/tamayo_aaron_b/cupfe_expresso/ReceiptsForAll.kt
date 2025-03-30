package com.tamayo_aaron_b.cupfe_expresso

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.tamayo_aaron_b.cupfe_expresso.receiptsAllKind.AllTransactionsAdapter
import com.tamayo_aaron_b.cupfe_expresso.receiptsAllKind.AllTransactionsConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptsForAll : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllTransactionsAdapter
    private lateinit var search: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receipts_for_all)

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        search = findViewById(R.id.search)
        ivBack.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerView) // Replace with your actual RecyclerView ID
        adapter = AllTransactionsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // Set layout manager

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
                search.text.clear()
                fetchReceipts()
                swipeRefreshLayout.isRefreshing = false
        }

        fetchReceipts() // Fetch data from API

        //SEARCH ITEM NAME (not case sensitive)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchReceipts(query)
                } else {
                    fetchReceipts()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchReceipts() {
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient.instance.getReceipts().enqueue(object : Callback<List<AllTransactionsConnection>> {
            override fun onResponse(call: Call<List<AllTransactionsConnection>>, response: Response<List<AllTransactionsConnection>>) {
                if (response.isSuccessful) {
                    response.body()?.let { adapter.setData(it) }
                }
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<List<AllTransactionsConnection>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Log.e("API_ERROR", "Failed to fetch receipts", t)
            }
        })
    }

    private fun searchReceipts(referenceNumber: String) {
        RetrofitClient.instance.searchReceipts(referenceNumber).enqueue(object : Callback<List<AllTransactionsConnection>> {
            override fun onResponse(call: Call<List<AllTransactionsConnection>>, response: Response<List<AllTransactionsConnection>>) {
                if (response.isSuccessful) {
                    response.body()?.let { adapter.setData(it) }
                }
            }

            override fun onFailure(call: Call<List<AllTransactionsConnection>>, t: Throwable) {
                Log.e("API_ERROR", "Failed to search receipts", t)
            }
        })
    }
}