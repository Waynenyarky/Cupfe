package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class Order_Summary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_summary)

        val coffeeNameTextView = findViewById<TextView>(R.id.tvCoffeeName)
        val sizeTextView = findViewById<TextView>(R.id.tvSelectedSize)
        val quantityTextView = findViewById<TextView>(R.id.tvQuantity)
        val priceTextView = findViewById<TextView>(R.id.tvPrice)
        val btnDineIn: TextView = findViewById(R.id.btnDineIn)
        val btnTakeOut: TextView = findViewById(R.id.btnTakeOut)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val totalTextView = findViewById<TextView>(R.id.Total)
        val tvAdd = findViewById<TextView>(R.id.tvAdd)
        val tvMinus = findViewById<TextView>(R.id.tvMinus)
        val subTotal = findViewById<TextView>(R.id.subTotal)
        val PayNow = findViewById<Button>(R.id.PayNow)
        val coffeeNameTextView2 = findViewById<TextView>(R.id.tvCoffeeName2)
        val sizeTextView2 = findViewById<TextView>(R.id.tvSelectedSize2)
        val quantityTextView2 = findViewById<TextView>(R.id.tvQuantity2)
        val priceTextView2 = findViewById<TextView>(R.id.tvPrice2)
        val tvAdd2 = findViewById<TextView>(R.id.tvAdd2)
        val tvMinus2 = findViewById<TextView>(R.id.tvMinus2)
        val coffeeNameTextView3 = findViewById<TextView>(R.id.tvCoffeeName3)
        val sizeTextView3 = findViewById<TextView>(R.id.tvSelectedSize3)
        val quantityTextView3 = findViewById<TextView>(R.id.tvQuantity3)
        val priceTextView3 = findViewById<TextView>(R.id.tvPrice3)
        val tvAdd3 = findViewById<TextView>(R.id.tvAdd3)
        val tvMinus3 = findViewById<TextView>(R.id.tvMinus3)
        val favFoodLayout = findViewById<LinearLayout>(R.id.favFoodLayout)
        val favFoodLayout2 = findViewById<LinearLayout>(R.id.favFoodLayout2)
        val favFoodLayout3 = findViewById<LinearLayout>(R.id.favFoodLayout3)
        val lDiscount = findViewById<LinearLayout>(R.id.lDiscounts)
        val etComment1 = findViewById<TextView>(R.id.etComment1)
        val etComment2 = findViewById<TextView>(R.id.etComment2)
        val etComment3 = findViewById<TextView>(R.id.etComment3)





        lDiscount.setOnClickListener{
            Toast.makeText(this, "No available voucher", Toast.LENGTH_SHORT).show()
        }

        PayNow.setOnClickListener {
            val transactionId = generateTransactionId()
            val orderType = if (btnDineIn.isSelected) "Dine-In" else "Take-Out"
            val coffeeName = coffeeNameTextView.text.toString()
            val coffeeSize = intent.getStringExtra("selectedSize") ?: "Unknown Size"
            val coffeeName2 = coffeeNameTextView2.text.toString()
            val coffeeName3 = coffeeNameTextView3.text.toString()
            val subTotalAmount = subTotal.text.toString()
            val totalAmount = totalTextView.text.toString()
            val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            val userComments1 = intent.getStringExtra("userComment1") ?: "N/A"
            val userComments2 = intent.getStringExtra("userComment2") ?: "N/A"
            val userComments3 = intent.getStringExtra("userComment3") ?: "N/A"
            val price = priceTextView.text.toString().replace("₱", "").trim().toDoubleOrNull() ?: 0.00


            val intent = Intent(this, Receipts::class.java).apply {
                putExtra("transactionId", transactionId)
                putExtra("orderType", orderType)
                putExtra("coffeeName", coffeeName)
                putExtra("selectedSize", coffeeSize)
                putExtra("userComment1", userComments1)
                putExtra("quantityTextView", quantityTextView.text.toString())
                putExtra("coffeeName2", coffeeName2)
                putExtra("userComment2", userComments2)
                putExtra("quantityTextView2", quantityTextView2.text.toString())
                putExtra("coffeeName3", coffeeName3)
                putExtra("userComment3", userComments3)
                putExtra("quantityTextView3", quantityTextView3.text.toString())
                putExtra("price", price)
                putExtra("subTotal", subTotalAmount)
                putExtra("total", totalAmount)
                putExtra("currentDate", currentDate)
                putExtra("currentTime", currentTime)
            }

            startActivity(intent)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        // Function to check and update favFoodLayout visibility
        fun updateFavFoodVisibility() {
            val isFavFoodVisible = coffeeNameTextView.text.isNotBlank() &&
                    sizeTextView.text.isNotBlank() &&
                    priceTextView.text.isNotBlank() &&
                    (quantityTextView.text.toString().toIntOrNull() ?: 0) > 0

            val isFavFood2Visible = coffeeNameTextView2.text.isNotBlank() &&
                    sizeTextView2.text.isNotBlank() &&
                    priceTextView2.text.isNotBlank() &&
                    (quantityTextView2.text.toString().toIntOrNull() ?: 0) > 0

            val isFavFood3Visible = coffeeNameTextView3.text.isNotBlank() &&
                    sizeTextView3.text.isNotBlank() &&
                    priceTextView3.text.isNotBlank() &&
                    (quantityTextView3.text.toString().toIntOrNull() ?: 0) > 0

            favFoodLayout.visibility = if (isFavFoodVisible) View.VISIBLE else View.GONE
            favFoodLayout2.visibility = if (isFavFood2Visible) View.VISIBLE else View.GONE
            favFoodLayout3.visibility = if (isFavFood3Visible) View.VISIBLE else View.GONE
        }



        // Retrieve data from intent
        val coffeeName = intent.getStringExtra("coffeeName") ?: "Unknown Coffee"
        val selectedSize = intent.getStringExtra("selectedSize") ?: "Unknown Size"
        var quantity = intent.getIntExtra("quantity", 0) // Default to 0
        val price = intent.getStringExtra("price")?.toDoubleOrNull() ?: 0.00 // Convert to Double
        val userComment1 = intent.getStringExtra("userComment1") ?: "N/A"



        val coffeeName2 = intent.getStringExtra("coffeeName2") ?: "Unknown Coffee"
        val selectedSize2 = intent.getStringExtra("selectedSize2") ?: "Unknown Size"
        var quantity2 = intent.getIntExtra("quantity2", 0) // Default to 0
        val price2 = intent.getStringExtra("price2")?.toDoubleOrNull() ?: 0.00 // Convert to Double
        val userComment2 = intent.getStringExtra("userComment2") ?: "N/A"

        val coffeeName3 = intent.getStringExtra("coffeeName3") ?: "Unknown Coffee"
        val selectedSize3 = intent.getStringExtra("selectedSize3") ?: "Unknown Size"
        var quantity3 = intent.getIntExtra("quantity3", 0) // Default to 0
        val price3 = intent.getStringExtra("price3")?.toDoubleOrNull() ?: 0.00 // Convert to Double
        val userComment3 = intent.getStringExtra("userComment3") ?: "N/A"


        // Back Home
        ivBack.setOnClickListener {
            // Reset all values
            coffeeNameTextView.text = ""
            sizeTextView.text = ""
            quantityTextView.text = "0"
            subTotal.text = "₱0.00"
            totalTextView.text = "₱0.00"

            coffeeNameTextView2.text = ""
            sizeTextView2.text = ""
            quantityTextView2.text = "0"

            coffeeNameTextView3.text = ""
            sizeTextView3.text = ""
            quantityTextView3.text = "0"

            // Reset quantity variable
            quantity = 0
            quantity2 = 0

            // Reset button selections
            btnDineIn.isSelected = true
            btnTakeOut.isSelected = false
            btnDineIn.setTextColor(ContextCompat.getColor(this, R.color.white))
            btnTakeOut.setTextColor(ContextCompat.getColor(this, R.color.black))
            updateFavFoodVisibility()

            // Finish activity and apply animation
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }


        // Function to update quantity and total price
        fun updateQuantity() {
            quantityTextView.text = quantity.toString() // Update quantity display
            updateFavFoodVisibility()

            // Only update subTotal and totalTextView if at least one favFoodLayout is visible
            if (favFoodLayout.visibility == View.VISIBLE) {
                etComment1.text = "``$userComment1"
                val totalPrice = price * quantity
                subTotal.text = "₱${String.format("%.2f", totalPrice)}"
                totalTextView.text = "₱${String.format("%.2f", totalPrice)}"
            }
        }

        fun updateQuantity2() {
            quantityTextView2.text = quantity2.toString() // Update quantity display
            updateFavFoodVisibility()

            // Only update subTotal and totalTextView if at least one favFoodLayout is visible
            if (favFoodLayout2.visibility == View.VISIBLE) {
                etComment2.text = "``$userComment2"
                val totalPrice = price2 * quantity2
                subTotal.text = "₱${String.format("%.2f", totalPrice)}"
                totalTextView.text = "₱${String.format("%.2f", totalPrice)}"
            }
        }

        fun updateQuantity3() {
            quantityTextView3.text = quantity3.toString() // Update quantity display
            updateFavFoodVisibility()

            // Only update subTotal and totalTextView if at least one favFoodLayout is visible
            if (favFoodLayout3.visibility == View.VISIBLE) {
                etComment3.text = "``$userComment3"
                val totalPrice = price3 * quantity3
                subTotal.text = "₱${String.format("%.2f", totalPrice)}"
                totalTextView.text = "₱${String.format("%.2f", totalPrice)}"
            }
        }

        if (userComment1.isBlank() || userComment1 == "N/A") {
            etComment1.visibility = View.GONE
        } else {
            etComment1.text = userComment1
            etComment1.visibility = View.VISIBLE
        }

        if (userComment2.isBlank() || userComment2 == "N/A") {
            etComment2.visibility = View.GONE
        } else {
            etComment2.text = userComment2
            etComment2.visibility = View.VISIBLE
        }

        if (userComment3.isBlank() || userComment3 == "N/A") {
            etComment3.visibility = View.GONE
        } else {
            etComment3.text = userComment3
            etComment3.visibility = View.VISIBLE
        }

        fun mapSizeForDisplay(size: String): String {
            return when (size) {
                "Small" -> "Size: S"
                "Medium" -> "Size: M"
                "Large" -> "Size: L"
                else -> size // Default to original if not mapped
            }
        }



        // Set initial values
        coffeeNameTextView.text = if (coffeeName != "Unknown Coffee") coffeeName else ""
        sizeTextView.text = mapSizeForDisplay(selectedSize)
        priceTextView.text = if (price > 0) "₱${String.format("%.2f", price)}" else ""
        etComment1.text = userComment1
        updateQuantity()
        updateFavFoodVisibility()

        coffeeNameTextView2.text = if (coffeeName2 != "Unknown Coffee") coffeeName2 else ""
        sizeTextView2.text = if (selectedSize2 != "Unknown Size") selectedSize2 else ""
        priceTextView2.text = if (price2 > 0) "₱${String.format("%.2f", price2)}" else ""
        etComment2.text = userComment2
        updateQuantity2()
        updateFavFoodVisibility()

        coffeeNameTextView3.text = if (coffeeName3 != "Unknown Coffee") coffeeName3 else ""
        sizeTextView3.text = if (selectedSize3 != "Unknown Size") selectedSize3 else ""
        priceTextView3.text = if (price3 > 0) "₱${String.format("%.2f", price3)}" else ""
        etComment3.text = userComment3
        updateQuantity3()
        updateFavFoodVisibility()




        // Handle Add Button Click
        tvAdd.setOnClickListener {
            quantity++
            updateQuantity()
        }

        // Handle Minus Button Click
        tvMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantity()
            }
        }

        // Handle Add Button Click
        tvAdd2.setOnClickListener {
            quantity2++
            updateQuantity2()
        }

        // Handle Minus Button Click
        tvMinus2.setOnClickListener {
            if (quantity2 > 1) {
                quantity2--
                updateQuantity2()
            }
        }

        // Handle Add Button Click
        tvAdd3.setOnClickListener {
            quantity3++
            updateQuantity3()
        }

        // Handle Minus Button Click
        tvMinus3.setOnClickListener {
            if (quantity3 > 1) {
                quantity3--
                updateQuantity3()
            }
        }



        // Set default styles
        btnDineIn.isSelected = true
        btnTakeOut.isSelected = false

        btnDineIn.setTextColor(ContextCompat.getColor(this, R.color.white))
        btnTakeOut.setTextColor(ContextCompat.getColor(this, R.color.black))

        // Apply background selector
        btnDineIn.setBackgroundResource(R.drawable.dine_in_out)
        btnTakeOut.setBackgroundResource(R.drawable.dine_in_out)

        // Handle Dine-In Click
        btnDineIn.setOnClickListener {
            btnDineIn.isSelected = true
            btnTakeOut.isSelected = false
            btnDineIn.setTextColor(ContextCompat.getColor(this, R.color.white))
            btnTakeOut.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        // Handle Take-Out Click
        btnTakeOut.setOnClickListener {
            btnDineIn.isSelected = false
            btnTakeOut.isSelected = true
            btnDineIn.setTextColor(ContextCompat.getColor(this, R.color.black))
            btnTakeOut.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    private fun generateTransactionId(): String {
        val random = Random()
        val letter = ('A'..'Z').random() // Random uppercase letter
        val numbers = (1..9).map { random.nextInt(10) }.joinToString("") // 9 random numbers
        return "$letter$numbers"
    }

}