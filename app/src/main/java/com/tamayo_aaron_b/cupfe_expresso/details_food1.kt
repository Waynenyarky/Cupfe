package com.tamayo_aaron_b.cupfe_expresso

import FavoriteItem
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tamayo_aaron_b.cupfe_expresso.menu.Coffee
import com.tamayo_aaron_b.cupfe_expresso.summary.OrderItem

class details_food1 : AppCompatActivity() {

    private var isAddedCart = false
    private var selectedSize = "none"
    private var currentNumber = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences1: SharedPreferences
    private lateinit var ivSmall : ImageView
    private lateinit var ivMedium : ImageView
    private lateinit var ivLarge : ImageView

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food1)


        val coffee = intent.getParcelableExtra<Coffee>("coffee")

        if (coffee != null) {
            // Use coffee object (e.g., update UI)
            findViewById<TextView>(R.id.tvCoffeeName).text = coffee.name
            findViewById<TextView>(R.id.description).text = coffee.description
            findViewById<TextView>(R.id.tvPrice).text = "$${coffee.priceSmall}"
            Glide.with(this)
                .load(coffee.imageUrl)
                .into(findViewById(R.id.image))
        }

        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)
        sharedPreferences1 = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)

        val heartIcon = findViewById<ImageView>(R.id.heartIcon)
        ivSmall = findViewById(R.id.ivSmall)
        ivMedium = findViewById(R.id.ivMedium)
        ivLarge = findViewById(R.id.ivLarge)
        val ivMinus = findViewById<ImageView>(R.id.ivMinus)
        val tvNumber = findViewById<TextView>(R.id.tvNumber)
        val ivAdd = findViewById<ImageView>(R.id.ivAdd)
        val tvVolumeSize = findViewById<TextView>(R.id.tvVolume_Size)
        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)
        val ivAddToCart = findViewById<ImageView>(R.id.ivAddToCart)
        val ivBuyNow = findViewById<ImageView>(R.id.ivBuyNow)
        val etComment = findViewById<EditText>(R.id.etComment)

        // Back Home
        ivBackHome.setOnClickListener {
            val prefs = getSharedPreferences("TempSelection", Context.MODE_PRIVATE).edit()
            prefs.clear().apply() // Clear saved selection
            selectedSize = "none"
            currentNumber = 0

            // Reset UI Elements
            tvVolumeSize.text = "0ml"
            findViewById<TextView>(R.id.tvPrice).text = "0.00"
            findViewById<TextView>(R.id.tvNumber).text = "0"
            etComment.setText("")

            // Reset Selection UI
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            finish()
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }


        //BUY NOW
        ivBuyNow.setOnClickListener {
            if (selectedSize == "none" || currentNumber == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
            } else {
                val priceText = findViewById<TextView>(R.id.tvPrice).text.toString()
                val cleanedPrice = priceText.replace("₱", "").replace(".00", "").trim()
                val userComment1 = etComment.text.toString()
                val priceDouble = cleanedPrice.toDoubleOrNull() ?: 0.0

                val coffeeId = coffee?.id
                if (coffeeId == null) {
                    Log.e("ORDER_ITEM", "Coffee ID is null! Order cannot proceed.")
                    showCustomAlertDialog("Error", "Something went wrong. Please try again.")
                    return@setOnClickListener
                }

                // Create an OrderItem object
                val orderItem = OrderItem(
                    id = coffeeId,
                    coffeeName = findViewById<TextView>(R.id.tvCoffeeName).text.toString(),
                    selectedSize = selectedSize,
                    quantity = currentNumber,
                    userComment = userComment1,
                    price = priceDouble,
                    imageUrl = coffee.imageUrl
                )

                // Pass OrderItem to Order_Summary
                val intent = Intent(this, Order_Summary::class.java).apply {
                    putExtra("orderItem", orderItem)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
        }

        ivAddToCart.setOnClickListener {
            if (!isAddedCart){
                if (selectedSize == "none" || currentNumber == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                    return@setOnClickListener
                }

                val priceText = findViewById<TextView>(R.id.tvPrice).text.toString()
                val cleanedPrice = priceText.replace("₱", "").replace(".00", "").trim()
                val userComment1 = etComment.text.toString()

                // Create an OrderItem object
                val orderItem = FavoriteItem(
                    coffeeName = findViewById<TextView>(R.id.tvCoffeeName).text.toString(),
                    selectedSize = selectedSize,
                    quantity = currentNumber,
                    userComment = userComment1,
                    price = cleanedPrice,
                    imageUrl = coffee?.imageUrl
                )

                isAddedCart = true
                showCustomAlertDialog("Added to Cart", "Size: $selectedSize\nQuantity: $currentNumber\nTotal: ₱$cleanedPrice.00")

                // Pass OrderItem to Order_Summary
                val intent = Intent(this, favoriteNav::class.java).apply {
                    putExtra("orderItem", orderItem)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)

            }else {
                showCustomAlertDialog("Already Added", "This item is already in your cart.")
            }
        }


        heartIcon.setOnClickListener {
            if (selectedSize == "none" || currentNumber == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
            } else {
                val priceText = findViewById<TextView>(R.id.tvPrice).text.toString()
                val cleanedPrice = priceText.replace("₱", "").replace(".00", "").trim()
                val userComment1 = etComment.text.toString()

                // Create an OrderItem object
                val favoriteItem = FavoriteItem(
                    coffeeName = findViewById<TextView>(R.id.tvCoffeeName).text.toString(),
                    selectedSize = selectedSize,
                    quantity = currentNumber,
                    userComment = userComment1,
                    price = cleanedPrice,
                    imageUrl = coffee?.imageUrl
                )

                // Pass OrderItem to Order_Summary
                val intent = Intent(this, favoriteNav::class.java).apply {
                    putExtra("favoriteItem", favoriteItem)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
        }





        // Logic for selecting size
        ivSmall.setOnClickListener { handleSizeSelection(coffee,"Small", ivSmall, ivMedium, ivLarge, tvVolumeSize, "240ml") }
        ivMedium.setOnClickListener { handleSizeSelection(coffee,"Medium", ivSmall, ivMedium, ivLarge, tvVolumeSize, "350ml") }
        ivLarge.setOnClickListener { handleSizeSelection(coffee,"Large", ivSmall, ivMedium, ivLarge, tvVolumeSize, "450ml") }

        // Logic for quantity
        tvNumber.text = currentNumber.toString()

        ivMinus.setOnClickListener {
            if (currentNumber > 0) {
                currentNumber--
                tvNumber.text = currentNumber.toString()
            }
        }

        ivAdd.setOnClickListener {
            if (currentNumber < 500) {
                currentNumber++
                tvNumber.text = currentNumber.toString()
            }
        }




        tvNumber.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_quantity_input, null)


            val etQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
            val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

            etQuantity.hint = "Enter a number (1 - 500)"

            builder.setView(dialogView)
            val dialog = builder.create()

            // Prevent dismissing by clicking outside
            dialog.setCanceledOnTouchOutside(false)

            btnOk.setOnClickListener {
                val enteredValue = etQuantity.text.toString()
                val newQuantity = enteredValue.toIntOrNull()

                if (newQuantity != null && newQuantity in 1..500) {
                    currentNumber = newQuantity
                    tvNumber.text = currentNumber.toString()
                    dialog.dismiss()
                } else {
                    showCustomAlertDialog("Invalid Input", "Please enter a quantity between 1 and 500.")
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun handleSizeSelection(coffee: Coffee?, size: String, ivSmall: ImageView, ivMedium: ImageView, ivLarge: ImageView, tvVolumeSize: TextView, volume: String) {
        val tvPrice= findViewById<TextView>(R.id.tvPrice) // Reference to price TextView
        val price = when (size) {
            "Small" -> coffee?.priceSmall
            "Medium" -> coffee?.priceMedium
            "Large" -> coffee?.priceLarge
            else -> "0.00"
        }


        if (selectedSize != size) {
            ivSmall.setBackgroundResource(if (size == "Small") R.drawable.small_brown else R.drawable.small_white)
            ivMedium.setBackgroundResource(if (size == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
            ivLarge.setBackgroundResource(if (size == "Large") R.drawable.large_brown else R.drawable.large_white)

            selectedSize = size
            tvVolumeSize.text = volume
            tvPrice.text = price // Update price when size is selected
        } else {
            selectedSize = "none"
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            tvVolumeSize.text = "0ml"
            tvPrice.text = "0.00" // Reset price when deselected
        }
    }


    private fun showCustomAlertDialog(title: String, message: String, onOkClick: (() -> Unit)? = null) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_alert_dialog) // Directly setting the layout

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<TextView>(R.id.dialogTitle).text = title
        dialog.findViewById<TextView>(R.id.dialogMessage).text = message

        dialog.findViewById<Button>(R.id.okButton).setOnClickListener {
            onOkClick?.invoke()
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        resetSelections()

        val prefs = getSharedPreferences("TempSelection", Context.MODE_PRIVATE)
        val fromOrderSummary = prefs.getBoolean("fromOrderSummary", false)
        selectedSize = prefs.getString("selectedSize", "none") ?: "none"

        val etComment = findViewById<EditText>(R.id.etComment)
        etComment.setText("")

        // Keep the selected size highlighted
        ivSmall.setBackgroundResource(if (selectedSize == "Small") R.drawable.small_brown else R.drawable.small_white)
        ivMedium.setBackgroundResource(if (selectedSize == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
        ivLarge.setBackgroundResource(if (selectedSize == "Large") R.drawable.large_brown else R.drawable.large_white)

        if (fromOrderSummary) {
            selectedSize = prefs.getString("selectedSize", "none") ?: "none"
            currentNumber = prefs.getInt("quantity", 0)

            val priceTextView = findViewById<TextView>(R.id.tvPrice)
            val tvVolumeSize = findViewById<TextView>(R.id.tvVolume_Size)
            val tvNumber = findViewById<TextView>(R.id.tvNumber)

            // Restore values
            tvNumber.text = currentNumber.toString()
            tvVolumeSize.text = when (selectedSize) {
                "Small" -> "240ml"
                "Medium" -> "350ml"
                "Large" -> "450ml"
                else -> "0ml"
            }

            priceTextView.text = when (selectedSize) {
                "Small" -> "99.00"
                "Medium" -> "109.00"
                "Large" -> "119.00"
                else -> "0.00"
            }

            // Reset flag so it doesn't retain old values indefinitely
            prefs.edit().putBoolean("fromOrderSummary", false).apply()
        }
    }

    private fun resetSelections() {
        selectedSize = "none"
        currentNumber = 0

        findViewById<TextView>(R.id.tvVolume_Size).text = "0ml"
        findViewById<TextView>(R.id.tvPrice).text = "0.00"
        findViewById<TextView>(R.id.tvNumber).text = "0"

        ivSmall.setBackgroundResource(R.drawable.small_white)
        ivMedium.setBackgroundResource(R.drawable.medium_white)
        ivLarge.setBackgroundResource(R.drawable.large_white)
    }
}

