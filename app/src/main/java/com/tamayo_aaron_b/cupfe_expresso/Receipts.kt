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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val tvCoffeeName2 = findViewById<TextView>(R.id.tvCoffeeName2)
        val tvCoffeeQuantity2 = findViewById<TextView>(R.id.tvCoffeeQuantity2)
        val tvCoffeeName3 = findViewById<TextView>(R.id.tvCoffeeName3)
        val tvCoffeeQuantity3 = findViewById<TextView>(R.id.tvCoffeeQuantity3)
        val tvSubTotal = findViewById<TextView>(R.id.tvSubTotal)
        val total_amount = findViewById<TextView>(R.id.tvTotal)
        val etComment1 = findViewById<TextView>(R.id.etComment1)
        val etComment2 = findViewById<TextView>(R.id.etComment2)
        val etComment3 = findViewById<TextView>(R.id.etComment3)
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



        // Get intent extras
        val orderType = intent.getStringExtra("orderType") ?: "Unknown"
        val coffeeName = intent.getStringExtra("coffeeName") ?: "Unknown Coffee"
        val coffeeSize = intent.getStringExtra("selectedSize") ?: "Unknown Coffee"
        val price = intent.getDoubleExtra("price", 0.00)
        val quantityTextView = intent.getStringExtra("quantityTextView") ?: ""
        val coffeeName2 = intent.getStringExtra("coffeeName2") ?: "Unknown Coffee"
        val quantityTextView2 = intent.getStringExtra("quantityTextView2") ?: ""
        val coffeeName3 = intent.getStringExtra("coffeeName3") ?: "Unknown Coffee"
        val quantityTextView3 = intent.getStringExtra("quantityTextView3") ?: ""
        val subTotal = intent.getStringExtra("subTotal") ?: "₱0.00"
        val total = intent.getStringExtra("total") ?: "₱0.00"
        val userComment1 = intent.getStringExtra("userComment1") ?: "N/A"
        val userComment2 = intent.getStringExtra("userComment2") ?: "N/A"
        val userComment3 = intent.getStringExtra("userComment3") ?: "N/A"
        val currentDate = intent.getStringExtra("currentDate") ?: "N/A"
        val currentTime = intent.getStringExtra("currentTime") ?: "N/A"
        val transactionId = intent.getStringExtra("transactionId") ?: "N/A"

        // Set values to TextViews
        tvOrderType.text = "$orderType"
        tvCoffeeName.text = "$coffeeName"
        tvSize.text = "$coffeeSize"
        tvPrize.text = "%.2f".format(price)
        tvCoffeeQuantity.text = "$quantityTextView"
        tvCoffeeName2.text = "$coffeeName2"
        tvCoffeeQuantity2.text = "$quantityTextView2"
        tvCoffeeName3.text = "$coffeeName3"
        tvCoffeeQuantity3.text = "$quantityTextView3 "
        tvSubTotal.text = "$subTotal"
        total_amount.text = "$total"
        etComment1.text = "$userComment1"
        etComment2.text = "$userComment2"
        etComment3.text = "$userComment3"
        tvDate.text = "$currentDate"
        tvTime.text = "$currentTime"
        reference_number.text = transactionId

        fun mapSizeForDisplay(size: String): String {
            return when (size) {
                "Small" -> "Size: S"
                "Medium" -> "Size: M"
                "Large" -> "Size: L"
                else -> size // Default to original if not mapped
            }
        }

        // Set values and visibility
        setVisibility(tvCoffeeName, coffeeName)
        setVisibility(tvSize, mapSizeForDisplay(coffeeSize))
        setPriceVisibility(tvPrize, price)
        setQuantityVisibility(tvCoffeeQuantity, quantityTextView)
        setVisibility(tvCoffeeName2, coffeeName2)
        setQuantityVisibility(tvCoffeeQuantity2, quantityTextView2)
        setVisibility(tvCoffeeName3, coffeeName3)
        setQuantityVisibility(tvCoffeeQuantity3, quantityTextView3)

        if (userComment1.isBlank() || userComment1 == "N/A") {
            etComment1.visibility = View.GONE
        } else {
            etComment1.text = "``$userComment1"
            etComment1.visibility = View.VISIBLE
        }

        if (userComment2.isBlank() || userComment2 == "N/A") {
            etComment2.visibility = View.GONE
        } else {
            etComment2.text = "``$userComment2"
            etComment2.visibility = View.VISIBLE
        }

        if (userComment3.isBlank() || userComment3 == "N/A") {
            etComment3.visibility = View.GONE
        } else {
            etComment3.text = "``$userComment3"
            etComment3.visibility = View.VISIBLE
        }



        confirmBtn.setOnClickListener { showSuccessDialog() }

        cancelBtn.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }


    }

    private fun setPriceVisibility(textView: TextView, price: Double) {
        if (price <= 0.0) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = "₱%.2f".format(price)
        }
    }

    // Function to set visibility dynamically
    private fun setVisibility(textView: TextView, value: String) {
        if (value.isBlank() || value == "N/A") {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = value
        }
    }

    private fun setQuantityVisibility(textView: TextView, quantity: String) {
        if (quantity.isBlank() || quantity == "0") {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = "( x$quantity )"
        }
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirmation_receipt_dialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val reference_number = findViewById<TextView>(R.id.tvTransactionId)
        val username = sharedPreferences.getString("USERNAME", null) ?: "Hello User!"
        val email = sharedPreferences.getString("user_email", null) ?: ""
        val orderType = intent.getStringExtra("orderType") ?: "Unknown"
        val payment_method = findViewById<TextView>(R.id.payment_method)
        val price = intent.getDoubleExtra("price", 0.00)
        val formattedPrice = "%.2f".format(price)
        payment_method.text = "Cash"
        val tvCoffeeName = findViewById<TextView>(R.id.tvCoffeeName)
        val tvCoffeeQuantity = findViewById<TextView>(R.id.tvCoffeeQuantity)
        val coffeeQuantity = tvCoffeeQuantity.text.toString().replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 1
        val priceDouble = formattedPrice.toDoubleOrNull() ?: 0.0
        val subTotalAmount = priceDouble * coffeeQuantity
        val tvSubTotal = findViewById<TextView>(R.id.tvSubTotal)
        tvSubTotal.text = "%.2f".format(subTotalAmount)
        val total_amount = findViewById<TextView>(R.id.tvTotal)
        total_amount.text = "%.2f".format(subTotalAmount)

        val coffeeSize = intent.getStringExtra("selectedSize") ?: "Unknown Coffee"
        val userComment1 = intent.getStringExtra("userComment1") ?: "N/A"

        val okBtn = dialog.findViewById<Button>(R.id.okBtn)
        okBtn.setOnClickListener {
            val orderRequest = OrderRequest(
                reference_number = reference_number.text.toString(),
                username = username,
                email = email,
                total_amount = total_amount.text.toString(),
                status = "pending",
                promo_code = "none",
                order_type = orderType,
                payment_method = payment_method.text.toString(),
                payment_status = "Unpaid",
                order_items = listOf(
                    OrderItem("1", tvCoffeeName.text.toString(), tvCoffeeQuantity.text.toString().toIntOrNull() ?: 1, formattedPrice, coffeeSize, userComment1, username, email)
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