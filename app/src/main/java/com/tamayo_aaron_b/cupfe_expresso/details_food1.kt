package com.tamayo_aaron_b.cupfe_expresso

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class details_food1 : AppCompatActivity() {

    private var isHeartRed = false
    private var selectedSize = "none"
    private var currentNumber = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food1)

        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)

        val heartIcon = findViewById<ImageView>(R.id.heartIcon)
        val ivSmall = findViewById<ImageView>(R.id.ivSmall)
        val ivMedium = findViewById<ImageView>(R.id.ivMedium)
        val ivLarge = findViewById<ImageView>(R.id.ivLarge)
        val ivMinus = findViewById<ImageView>(R.id.ivMinus)
        val tvNumber = findViewById<TextView>(R.id.tvNumber)
        val ivAdd = findViewById<ImageView>(R.id.ivAdd)
        val tvVolumeSize = findViewById<TextView>(R.id.tvVolume_Size)
        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)

        // Back Home
        ivBackHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Load favorite state
        loadFavoriteState(heartIcon)

        // Logic for selecting size
        ivSmall.setOnClickListener { handleSizeSelection("Small", ivSmall, ivMedium, ivLarge, tvVolumeSize, "240ml") }
        ivMedium.setOnClickListener { handleSizeSelection("Medium", ivSmall, ivMedium, ivLarge, tvVolumeSize, "350ml") }
        ivLarge.setOnClickListener { handleSizeSelection("Large", ivSmall, ivMedium, ivLarge, tvVolumeSize, "450ml") }

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



        heartIcon.setOnClickListener {
            if (isHeartRed) {
                // Allow removing favorite without checking size or quantity
                toggleFavorite(heartIcon)
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize == "none" || currentNumber == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleFavorite(heartIcon)
                }
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

    private fun handleSizeSelection(size: String, ivSmall: ImageView, ivMedium: ImageView, ivLarge: ImageView, tvVolumeSize: TextView, volume: String) {
        val tvPrice = findViewById<TextView>(R.id.tvPrice) // Reference to price TextView
        val price = when (size) {
            "Small" -> "₱99.00"
            "Medium" -> "₱109.00"
            "Large" -> "₱119.00"
            else -> "₱0.00"
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
            tvPrice.text = "₱0.00" // Reset price when deselected
        }
    }

    private fun toggleFavorite(heartIcon: ImageView) {
        val coffeeName = findViewById<TextView>(R.id.tvCoffeeName).text.toString()
        val priceText = findViewById<TextView>(R.id.tvPrice).text.toString()
        val editor = sharedPreferences.edit()

        if (!isHeartRed) { // Only validate if adding to favorites
            if (selectedSize == "none" || currentNumber == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }
        }

        if (isHeartRed) {
            // **Removing from favorites (No validation needed)**
            editor.remove("coffeeName")
            editor.remove("selectedSize")
            editor.remove("quantity")
            editor.remove("totalPrice")
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) - 1
            editor.putInt("favoriteCount", favoriteCount.coerceAtLeast(0))
            editor.apply()

            isHeartRed = false
            heartIcon.setImageResource(R.drawable.fav_heart_white)
            showCustomAlertDialog("Removed from Favorites", "Item has been removed.")
        } else {
            // **Adding to favorites (Validation already passed)**
            val price = priceText.replace("₱", "").replace(".00", "").toInt()
            val totalPrice = price * currentNumber

            editor.putString("coffeeName", coffeeName)
            editor.putString("selectedSize", selectedSize)
            editor.putInt("quantity", currentNumber)
            editor.putInt("totalPrice", totalPrice)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isHeartRed = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            showCustomAlertDialog("Added to Favorites", "Size: $selectedSize\nQuantity: $currentNumber\nTotal: ₱$totalPrice.00")
        }
    }

    private fun loadFavoriteState(heartIcon: ImageView) {
        val savedName = sharedPreferences.getString("coffeeName", null)
        val savedSize = sharedPreferences.getString("selectedSize", null)
        val savedQuantity = sharedPreferences.getInt("quantity", 0)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isHeartRed = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            selectedSize = savedSize
            currentNumber = savedQuantity
        }
    }

    private fun showCustomAlertDialog(title: String, message: String, onOkClick: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(this)
        val customView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(customView)

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)

        customView.findViewById<TextView>(R.id.dialogTitle).text = title
        customView.findViewById<TextView>(R.id.dialogMessage).text = message

        customView.findViewById<Button>(R.id.okButton).setOnClickListener {
            onOkClick?.invoke()
            alertDialog.dismiss()
        }
        // Prevent dismissing by clicking outside
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)

        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()

        // Reset quantity to default (0)
        currentNumber = 0
        val tvNumber = findViewById<TextView>(R.id.tvNumber)
        tvNumber.text = currentNumber.toString()

        // Reset the size selection
        selectedSize = "none"
        val ivSmall = findViewById<ImageView>(R.id.ivSmall)
        val ivMedium = findViewById<ImageView>(R.id.ivMedium)
        val ivLarge = findViewById<ImageView>(R.id.ivLarge)
        val tvVolumeSize = findViewById<TextView>(R.id.tvVolume_Size)

        ivSmall.setBackgroundResource(R.drawable.small_white)
        ivMedium.setBackgroundResource(R.drawable.medium_white)
        ivLarge.setBackgroundResource(R.drawable.large_white)
        tvVolumeSize.text = "0ml"
    }
}

