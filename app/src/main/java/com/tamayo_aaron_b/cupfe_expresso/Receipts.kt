package com.tamayo_aaron_b.cupfe_expresso

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tamayo_aaron_b.cupfe_expresso.menu.Coffee
import retrofit2.Call

class Receipts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receipts)

        val confirmBtn = findViewById<Button>(R.id.confirmBtn)
        val cancelBtn = findViewById<Button>(R.id.cancelBtn)
        val tvOrderType = findViewById<TextView>(R.id.tvOrderType)
        val tvCoffeeName = findViewById<TextView>(R.id.tvCoffeeName)
        val tvCoffeeQuantity = findViewById<TextView>(R.id.tvCoffeeQuantity)
        val tvSubTotal = findViewById<TextView>(R.id.tvSubTotal)
        val total_amount = findViewById<TextView>(R.id.tvTotal)
        val etComment1 = findViewById<TextView>(R.id.etComment1)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvTime = findViewById<TextView>(R.id.tvTime)
        val reference_number = findViewById<TextView>(R.id.tvTransactionId)
        val payment_method = findViewById<TextView>(R.id.payment_method)
        payment_method.text = "Cash"
        val tvSize = findViewById<TextView>(R.id.tvSize)
        val tvPrize = findViewById<TextView>(R.id.tvPrize)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null) ?: ""
        val username = sharedPreferences.getString("USERNAME", null) ?: "Hello User!"
        Log.d("DEBUG", "Email Saved in Receipts: $email")
        Log.d("DEBUG", "Name Saved in Receipts: $username")




        // Retrieve data from intent
        val transactionId = intent.getStringExtra("transactionId")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val orderType = intent.getStringExtra("orderType")
        val coffeeName = intent.getStringExtra("coffeeName")
        val size = intent.getStringExtra("size")
        val quantity = intent.getIntExtra("quantity", 0)
        val price = intent.getDoubleExtra("price", 0.0)
        val comment = intent.getStringExtra("comment")
        val subTotal = intent.getDoubleExtra("subTotal", 0.0)
        val total = intent.getDoubleExtra("total", 0.0)

        // Update UI
        reference_number.text = "$transactionId"
        tvDate.text = "$date"
        tvTime.text = "$time"
        tvOrderType.text = "$orderType"
        tvCoffeeName.text = "$coffeeName"
        tvSize.text = "Size: $size"
        tvCoffeeQuantity.text = "x$quantity"
        tvPrize.text = "₱${String.format("%.2f", price)}"
        etComment1.text = "``$comment"
        tvSubTotal.text = "₱${String.format("%.2f", subTotal)}"
        total_amount.text = "₱${String.format("%.2f", total)}"



        confirmBtn.setOnClickListener { showSuccessDialog(transactionId, orderType,coffeeName,size,quantity,price,comment,total) }

        cancelBtn.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }


    }


    private fun showSuccessDialog(
        transactionId: String?,
        orderType: String?,
        coffeeName: String?,
        size: String?,
        quantity: Int,
        price: Double,
        comment: String?,
        total: Double
    ) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirmation_receipt_dialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", null) ?: "Hello User!"
        val email = sharedPreferences.getString("user_email", null) ?: ""
        val coffeeId = intent?.getIntExtra("coffeeId", -1) ?: -1// Default value -1 if not found


        val okBtn = dialog.findViewById<Button>(R.id.okBtn)
        okBtn.setOnClickListener {
            val orderRequest = OrderRequest(
                reference_number = transactionId ?: "N/A",
                username = username,
                email = email,
                total_amount = total.toString(),
                status = "pending",
                promo_code = "none",
                order_type = orderType ?: "Unknown",
                payment_method = "Cash",
                payment_status = "Unpaid",
                order_items = listOf(
                    OrderItem(
                        item_id = coffeeId.toString(),
                        item_name = coffeeName ?: "Unknown Coffee",
                        quantity = quantity,
                        price = String.format("%.2f", price),
                        size = size ?: "Unknown",
                        special_instructions = comment ?: "N/A",
                        username = username,
                        email = email
                    )
                )
            )


            RetrofitClient.instance.createOrder(orderRequest).enqueue(object : retrofit2.Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: retrofit2.Response<OrderResponse>) {
                    if (response.isSuccessful) {
                        Log.d("ORDER", "Order created successfully")
                        dialog.dismiss()
                        startActivity(Intent(this@Receipts, Main_Home_Page::class.java))
                        finish()
                    } else {
                        Log.d("ORDER", "Response Code: ${response.code()}")
                        Log.d("ORDER", "Response Body: ${response.body()}")
                        Log.e("ORDER", "Failed to create order")
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Log.e("ORDER", "Error: ${t.message}")
                }
            })
        }

        dialog.show()
    }

}