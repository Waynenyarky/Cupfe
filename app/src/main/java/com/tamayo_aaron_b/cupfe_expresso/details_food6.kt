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

class details_food6 : AppCompatActivity() {
    private var isHeartRed6 = false
    private var selectedSize6 = "none"
    private var currentNumber6 = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food6)

        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)

        val heartIcon6 = findViewById<ImageView>(R.id.heartIcon6)
        val ivSmall6 = findViewById<ImageView>(R.id.ivSmall6)
        val ivMedium6 = findViewById<ImageView>(R.id.ivMedium6)
        val ivLarge6 = findViewById<ImageView>(R.id.ivLarge6)
        val ivMinus6 = findViewById<ImageView>(R.id.ivMinus6)
        val tvNumber6 = findViewById<TextView>(R.id.tvNumber6)
        val ivAdd6 = findViewById<ImageView>(R.id.ivAdd6)
        val tvVolumeSize6 = findViewById<TextView>(R.id.tvVolume_Size6)
        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)

        // Back Home
        ivBackHome.setOnClickListener {
            startActivity(Intent(this, Main_Home_Page::class.java))
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }

        // Load favorite state
        loadFavoriteState(heartIcon6)

        // Logic for selecting size
        ivSmall6.setOnClickListener { handleSizeSelection("Small", ivSmall6, ivMedium6, ivLarge6, tvVolumeSize6, "240ml") }
        ivMedium6.setOnClickListener { handleSizeSelection("Medium", ivSmall6, ivMedium6, ivLarge6, tvVolumeSize6, "350ml") }
        ivLarge6.setOnClickListener { handleSizeSelection("Large", ivSmall6, ivMedium6, ivLarge6, tvVolumeSize6, "450ml") }

        // Logic for quantity
        tvNumber6.text = currentNumber6.toString()

        ivMinus6.setOnClickListener {
            if (currentNumber6 > 0) {
                currentNumber6--
                tvNumber6.text = currentNumber6.toString()
            }
        }

        ivAdd6.setOnClickListener {
            if (currentNumber6 < 500) {
                currentNumber6++
                tvNumber6.text = currentNumber6.toString()
            }
        }

        heartIcon6.setOnClickListener {
            if (isHeartRed6) {
                // Allow removing favorite without checking size or quantity
                toggleFavorite(heartIcon6)
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize6 == "none" || currentNumber6 == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleFavorite(heartIcon6)
                }
            }
        }

        tvNumber6.setOnClickListener {
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
                    currentNumber6 = newQuantity
                    tvNumber6.text = currentNumber6.toString()
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
        val tvPrice6= findViewById<TextView>(R.id.tvPrice6)
        val price6 = when (size) {
            "Small" -> "₱39.00"
            "Medium" -> "₱49.00"
            "Large" -> "₱59.00"
            else -> "₱0.00"
        }

        if (selectedSize6 != size) {
            ivSmall.setBackgroundResource(if (size == "Small") R.drawable.small_brown else R.drawable.small_white)
            ivMedium.setBackgroundResource(if (size == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
            ivLarge.setBackgroundResource(if (size == "Large") R.drawable.large_brown else R.drawable.large_white)

            selectedSize6 = size
            tvVolumeSize.text = volume
            tvPrice6.text = price6 // Update price when size is selected
        } else {
            selectedSize6 = "none"
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            tvVolumeSize.text = "0ml"
            tvPrice6.text = "₱0.00" // Reset price when deselected
        }
    }

    private fun toggleFavorite(heartIcon: ImageView) {
        val coffeeName6 = findViewById<TextView>(R.id.tvCoffeeName6).text.toString()
        val priceText6 = findViewById<TextView>(R.id.tvPrice6).text.toString()
        val editor = sharedPreferences.edit()

        if (!isHeartRed6) { // Only validate if adding to favorites
            if (selectedSize6== "none" || currentNumber6 == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }
        }

        if (isHeartRed6) {
            editor.remove("coffeeName6")
            editor.remove("selectedSize6")
            editor.remove("quantity6")
            editor.remove("totalPrice6")
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) - 1
            editor.putInt("favoriteCount", favoriteCount.coerceAtLeast(0))
            editor.apply()

            isHeartRed6 = false
            heartIcon.setImageResource(R.drawable.fav_heart_white)
            showCustomAlertDialog("Removed from Favorites", "Item has been removed.")
        } else {
            // **Adding to favorites (Validation already passed)**
            val price = priceText6.replace( "₱", "").replace(".00", "").toInt()
            val totalPrice6 = price * currentNumber6

            editor.putString("coffeeName6", coffeeName6)
            editor.putString("selectedSize6", selectedSize6)
            editor.putInt("quantity6", currentNumber6)
            editor.putInt("totalPrice6", totalPrice6)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isHeartRed6 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            showCustomAlertDialog("Added to Favorites", "Size: $selectedSize6\nQuantity: $currentNumber6\nTotal: ₱$totalPrice6.00")
        }
    }

    private fun loadFavoriteState(heartIcon: ImageView) {
        val savedName = sharedPreferences.getString("coffeeName6", null)
        val savedSize = sharedPreferences.getString("selectedSize6", null)
        val savedQuantity = sharedPreferences.getInt("quantity6", 0)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isHeartRed6 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            selectedSize6 = savedSize
            currentNumber6 = savedQuantity
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
        currentNumber6 = 0
        val tvNumber = findViewById<TextView>(R.id.tvNumber6)
        tvNumber.text = currentNumber6.toString()

        // Reset the size selection
        selectedSize6 = "none"
        val ivSmall6 = findViewById<ImageView>(R.id.ivSmall6)
        val ivMedium6 = findViewById<ImageView>(R.id.ivMedium6)
        val ivLarge6 = findViewById<ImageView>(R.id.ivLarge6)
        val tvVolumeSize6 = findViewById<TextView>(R.id.tvVolume_Size6)

        ivSmall6.setBackgroundResource(R.drawable.small_white)
        ivMedium6.setBackgroundResource(R.drawable.medium_white)
        ivLarge6.setBackgroundResource(R.drawable.large_white)
        tvVolumeSize6.text = "0ml"
    }
}