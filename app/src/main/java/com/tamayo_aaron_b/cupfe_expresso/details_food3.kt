package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class details_food3 : AppCompatActivity() {

    private var isHeartRed3 = false
    private var isAddedCart3 = false
    private var selectedSize3 = "none"
    private var currentNumber3 = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences1: SharedPreferences
    private lateinit var ivSmall3 : ImageView
    private lateinit var ivMedium3 : ImageView
    private lateinit var ivLarge3 : ImageView

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food3)

        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)
        sharedPreferences1 = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)

        val heartIcon3 = findViewById<ImageView>(R.id.heartIcon3)
        ivSmall3 = findViewById(R.id.ivSmall3)
        ivMedium3 = findViewById(R.id.ivMedium3)
        ivLarge3 = findViewById(R.id.ivLarge3)
        val ivMinus3 = findViewById<ImageView>(R.id.ivMinus3)
        val tvNumber3 = findViewById<TextView>(R.id.tvNumber3)
        val ivAdd3 = findViewById<ImageView>(R.id.ivAdd3)
        val tvVolumeSize3 = findViewById<TextView>(R.id.tvVolume_Size3)
        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)
        val ivAddToCart3 = findViewById<ImageView>(R.id.ivAddToCart3)
        val ivBuyNow3 = findViewById<ImageView>(R.id.ivBuyNow3)
        val etComment3 = findViewById<EditText>(R.id.etComment3)

        // Back Home
        ivBackHome.setOnClickListener {
            val prefs = getSharedPreferences("TempSelection3", Context.MODE_PRIVATE).edit()
            prefs.clear().apply() // Clear saved selection
            selectedSize3 = "none"
            currentNumber3 = 0

            // Reset UI Elements
            tvVolumeSize3.text = "0ml"
            findViewById<TextView>(R.id.tvPrice3).text = "₱0.00"
            findViewById<TextView>(R.id.tvNumber3).text = "0"

            // Reset Selection UI
            ivSmall3.setBackgroundResource(R.drawable.small_white)
            ivMedium3.setBackgroundResource(R.drawable.medium_white)
            ivLarge3.setBackgroundResource(R.drawable.large_white)

            finish()
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }

        // Load favorite state
        loadFavoriteState(heartIcon3)
        loadCart()

        ivBuyNow3.setOnClickListener {
            if (selectedSize3 == "none" || currentNumber3 == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
            } else {
                val priceText3 = findViewById<TextView>(R.id.tvPrice3).text.toString()
                val cleanedPrice3 = priceText3.replace("₱", "").replace(".00", "").trim()


                // Save selected size and quantity before navigating
                val prefs = getSharedPreferences("TempSelection3", Context.MODE_PRIVATE).edit()
                prefs.putString("selectedSize3", selectedSize3)
                prefs.putInt("quantity3", currentNumber3)
                val userComment3 = etComment3.text.toString()
                prefs.putBoolean("fromOrderSummary3", true)
                prefs.apply()

                val intent = Intent(this, Order_Summary::class.java).apply {
                    putExtra("coffeeName3", findViewById<TextView>(R.id.tvCoffeeName3).text.toString())
                    putExtra("selectedSize3", selectedSize3)
                    putExtra("quantity3", currentNumber3)
                    putExtra("userComment3", userComment3)
                    putExtra("price3", cleanedPrice3)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
        }

        // Logic for selecting size
        ivSmall3.setOnClickListener { handleSizeSelection("Small", ivSmall3, ivMedium3, ivLarge3, tvVolumeSize3, "240ml") }
        ivMedium3.setOnClickListener { handleSizeSelection("Medium", ivSmall3, ivMedium3, ivLarge3, tvVolumeSize3, "350ml") }
        ivLarge3.setOnClickListener { handleSizeSelection("Large", ivSmall3, ivMedium3, ivLarge3, tvVolumeSize3, "450ml") }

        // Logic for quantity
        tvNumber3.text = currentNumber3.toString()

        ivMinus3.setOnClickListener {
            if (currentNumber3 > 0) {
                currentNumber3--
                tvNumber3.text = currentNumber3.toString()
            }
        }

        ivAdd3.setOnClickListener {
            if (currentNumber3 < 500) {
                currentNumber3++
                tvNumber3.text = currentNumber3.toString()
            }
        }

        heartIcon3.setOnClickListener {
            if (isHeartRed3) {
                // Allow removing favorite without checking size or quantity
                toggleFavorite(heartIcon3)
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize3 == "none" || currentNumber3 == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleFavorite(heartIcon3)
                }
            }
        }

        ivAddToCart3.setOnClickListener {
            if (isAddedCart3) {
                // Allow removing favorite without checking size or quantity
                toggleCart()
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize3 == "none" || currentNumber3 == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleCart()
                }
            }
        }

        tvNumber3.setOnClickListener {
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
                    currentNumber3 = newQuantity
                    tvNumber3.text = currentNumber3.toString()
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

    private fun handleSizeSelection(size: String, ivSmall: ImageView, ivMedium: ImageView, ivLarge: ImageView, tvVolumeSize: TextView, volume: String) {
        val tvPrice3= findViewById<TextView>(R.id.tvPrice3) // Reference to price TextView
        val price3 = when (size) {
            "Small" -> "₱59.00"
            "Medium" -> "₱69.00"
            "Large" -> "₱79.00"
            else -> "₱0.00"
        }

        if (selectedSize3 != size) {
            ivSmall.setBackgroundResource(if (size == "Small") R.drawable.small_brown else R.drawable.small_white)
            ivMedium.setBackgroundResource(if (size == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
            ivLarge.setBackgroundResource(if (size == "Large") R.drawable.large_brown else R.drawable.large_white)

            selectedSize3 = size
            tvVolumeSize.text = volume
            tvPrice3.text = price3 // Update price when size is selected
        } else {
            selectedSize3 = "none"
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            tvVolumeSize.text = "0ml"
            tvPrice3.text = "₱0.00" // Reset price when deselected
        }
    }

    private fun toggleFavorite(heartIcon: ImageView) {
        val coffeeName3 = findViewById<TextView>(R.id.tvCoffeeName3).text.toString()
        val priceText3 = findViewById<TextView>(R.id.tvPrice3).text.toString()
        val editor = sharedPreferences.edit()

        if (!isHeartRed3) { // Only validate if adding to favorites
            if (selectedSize3 == "none" || currentNumber3 == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }
        }

        if (isHeartRed3) {
            editor.remove("coffeeName3")
            editor.remove("selectedSize3")
            editor.remove("quantity3")
            editor.remove("totalPrice3")
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) - 1
            editor.putInt("favoriteCount", favoriteCount.coerceAtLeast(0))
            editor.apply()

            isHeartRed3 = false
            heartIcon.setImageResource(R.drawable.fav_heart_white)
            showCustomAlertDialog("Removed from Favorites", "Item has been removed.")
        } else {
            // **Adding to favorites (Validation already passed)**
            val price = priceText3.replace("₱", "").replace(".00", "").toInt()
            val totalPrice3 = price * currentNumber3

            editor.putString("coffeeName3", coffeeName3)
            editor.putString("selectedSize3", selectedSize3)
            editor.putInt("quantity3", currentNumber3)
            editor.putInt("totalPrice3", totalPrice3)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isHeartRed3 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            showCustomAlertDialog("Added to Favorites", "Size: $selectedSize3\nQuantity: $currentNumber3\nTotal: ₱$totalPrice3.00")
        }
    }

    private fun loadFavoriteState(heartIcon: ImageView) {
        val savedName = sharedPreferences.getString("coffeeName3", null)
        val savedSize = sharedPreferences.getString("selectedSize3", null)
        val savedQuantity = sharedPreferences.getInt("quantity3", 0)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isHeartRed3 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            selectedSize3 = savedSize
            currentNumber3 = savedQuantity
        }
    }

    private fun toggleCart() {
        val coffeeName3 = findViewById<TextView>(R.id.tvCoffeeName3).text.toString()
        val priceText3 = findViewById<TextView>(R.id.tvPrice3).text.toString()
        val editor = sharedPreferences1.edit()

        if (!isAddedCart3) { // Ensuring it's only added once
            if (selectedSize3 == "none" || currentNumber3 == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }

            // Adding to cart after validation
            val price = priceText3.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0
            val totalPrice3 = price * currentNumber3

            editor.putString("coffeeName3", coffeeName3)
            editor.putString("selectedSize3", selectedSize3)
            editor.putInt("quantity3", currentNumber3)
            editor.putInt("totalPrice3", totalPrice3)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isAddedCart3 = true

            showCustomAlertDialog("Added to Cart", "Size: $selectedSize3\nQuantity: $currentNumber3\nTotal: ₱$totalPrice3.00")
        } else {
            showCustomAlertDialog("Already Added", "This item is already in your cart.")
        }
    }



    private fun loadCart() {
        val savedName = sharedPreferences1.getString("coffeeName3", null)
        val savedSize = sharedPreferences1.getString("selectedSize3", null)
        val savedQuantity = sharedPreferences1.getInt("quantity3", 0)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isAddedCart3 = true
            selectedSize3 = savedSize
            currentNumber3 = savedQuantity
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

        val prefs = getSharedPreferences("TempSelection3", Context.MODE_PRIVATE)
        val fromOrderSummary3 = prefs.getBoolean("fromOrderSummary3", false)
        selectedSize3 = prefs.getString("selectedSize3", "none") ?: "none"

        // Keep the selected size highlighted
        ivSmall3.setBackgroundResource(if (selectedSize3 == "Small") R.drawable.small_brown else R.drawable.small_white)
        ivMedium3.setBackgroundResource(if (selectedSize3 == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
        ivLarge3.setBackgroundResource(if (selectedSize3 == "Large") R.drawable.large_brown else R.drawable.large_white)

        if (fromOrderSummary3) {
            selectedSize3 = prefs.getString("selectedSize3", "none") ?: "none"
            currentNumber3 = prefs.getInt("quantity3", 0)

            val priceTextView = findViewById<TextView>(R.id.tvPrice3)
            val tvVolumeSize = findViewById<TextView>(R.id.tvVolume_Size3)
            val tvNumber = findViewById<TextView>(R.id.tvNumber3)

            // Restore values
            tvNumber.text = currentNumber3.toString()
            tvVolumeSize.text = when (selectedSize3) {
                "Small" -> "240ml"
                "Medium" -> "350ml"
                "Large" -> "450ml"
                else -> "0ml"
            }

            priceTextView.text = when (selectedSize3) {
                "Small" -> "₱99.00"
                "Medium" -> "₱109.00"
                "Large" -> "₱119.00"
                else -> "₱0.00"
            }

            // Reset flag so it doesn't retain old values indefinitely
            prefs.edit().putBoolean("fromOrderSummary3", false).apply()
        }
    }

    private fun resetSelections() {
        selectedSize3 = "none"
        currentNumber3 = 0

        findViewById<TextView>(R.id.tvVolume_Size3).text = "0ml"
        findViewById<TextView>(R.id.tvPrice3).text = "₱0.00"
        findViewById<TextView>(R.id.tvNumber3).text = "0"

        ivSmall3.setBackgroundResource(R.drawable.small_white)
        ivMedium3.setBackgroundResource(R.drawable.medium_white)
        ivLarge3.setBackgroundResource(R.drawable.large_white)
    }
}