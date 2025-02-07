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

class details_food5 : AppCompatActivity() {
    private var isHeartRed5 = false
    private var selectedSize5 = "none"
    private var currentNumber5 = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food5)

        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)

        val heartIcon5 = findViewById<ImageView>(R.id.heartIcon5)
        val ivSmall5 = findViewById<ImageView>(R.id.ivSmall5)
        val ivMedium5 = findViewById<ImageView>(R.id.ivMedium5)
        val ivLarge5 = findViewById<ImageView>(R.id.ivLarge5)
        val ivMinus5 = findViewById<ImageView>(R.id.ivMinus5)
        val tvNumber5 = findViewById<TextView>(R.id.tvNumber5)
        val ivAdd5 = findViewById<ImageView>(R.id.ivAdd5)
        val tvVolumeSize5 = findViewById<TextView>(R.id.tvVolume_Size5)
        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)

        // Back Home
        ivBackHome.setOnClickListener {
            startActivity(Intent(this, Main_Home_Page::class.java))
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }

        // Load favorite state
        loadFavoriteState(heartIcon5)

        // Logic for selecting size
        ivSmall5.setOnClickListener { handleSizeSelection("Small", ivSmall5, ivMedium5, ivLarge5, tvVolumeSize5, "240ml") }
        ivMedium5.setOnClickListener { handleSizeSelection("Medium", ivSmall5, ivMedium5, ivLarge5, tvVolumeSize5, "350ml") }
        ivLarge5.setOnClickListener { handleSizeSelection("Large", ivSmall5, ivMedium5, ivLarge5, tvVolumeSize5, "450ml") }

        // Logic for quantity
        tvNumber5.text = currentNumber5.toString()

        ivMinus5.setOnClickListener {
            if (currentNumber5 > 0) {
                currentNumber5--
                tvNumber5.text = currentNumber5.toString()
            }
        }

        ivAdd5.setOnClickListener {
            if (currentNumber5 < 500) {
                currentNumber5++
                tvNumber5.text = currentNumber5.toString()
            }
        }

        heartIcon5.setOnClickListener {
            if (isHeartRed5) {
                // Allow removing favorite without checking size or quantity
                toggleFavorite(heartIcon5)
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize5 == "none" || currentNumber5 == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleFavorite(heartIcon5)
                }
            }
        }

        tvNumber5.setOnClickListener {
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
                    currentNumber5 = newQuantity
                    tvNumber5.text = currentNumber5.toString()
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
        val tvPrice5= findViewById<TextView>(R.id.tvPrice5) // Reference to price TextView
        val price5 = when (size) {
            "Small" -> "₱109.00"
            "Medium" -> "₱119.00"
            "Large" -> "₱129.00"
            else -> "₱0.00"
        }

        if (selectedSize5 != size) {
            ivSmall.setBackgroundResource(if (size == "Small") R.drawable.small_brown else R.drawable.small_white)
            ivMedium.setBackgroundResource(if (size == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
            ivLarge.setBackgroundResource(if (size == "Large") R.drawable.large_brown else R.drawable.large_white)

            selectedSize5 = size
            tvVolumeSize.text = volume
            tvPrice5.text = price5 // Update price when size is selected
        } else {
            selectedSize5 = "none"
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            tvVolumeSize.text = "0ml"
            tvPrice5.text = "₱0.00" // Reset price when deselected
        }
    }

    private fun toggleFavorite(heartIcon: ImageView) {
        val coffeeName5 = findViewById<TextView>(R.id.tvCoffeeName5).text.toString()
        val priceText5 = findViewById<TextView>(R.id.tvPrice5).text.toString()
        val editor = sharedPreferences.edit()

        if (!isHeartRed5) { // Only validate if adding to favorites
            if (selectedSize5== "none" || currentNumber5 == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }
        }

        if (isHeartRed5) {
            editor.remove("coffeeName5")
            editor.remove("selectedSize5")
            editor.remove("quantity5")
            editor.remove("totalPrice5")
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) - 1
            editor.putInt("favoriteCount", favoriteCount.coerceAtLeast(0))
            editor.apply()

            isHeartRed5 = false
            heartIcon.setImageResource(R.drawable.fav_heart_white)
            showCustomAlertDialog("Removed from Favorites", "Item has been removed.")
        } else {
            // **Adding to favorites (Validation already passed)**
            val price = priceText5.replace( "₱", "").replace(".00", "").toInt()
            val totalPrice5 = price * currentNumber5

            editor.putString("coffeeName5", coffeeName5)
            editor.putString("selectedSize5", selectedSize5)
            editor.putInt("quantity5", currentNumber5)
            editor.putInt("totalPrice5", totalPrice5)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isHeartRed5 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            showCustomAlertDialog("Added to Favorites", "Size: $selectedSize5\nQuantity: $currentNumber5\nTotal: ₱$totalPrice5.00")
        }
    }

    private fun loadFavoriteState(heartIcon: ImageView) {
        val savedName = sharedPreferences.getString("coffeeName5", null)
        val savedSize = sharedPreferences.getString("selectedSize5", null)
        val savedQuantity = sharedPreferences.getInt("quantity5", 0)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isHeartRed5 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            selectedSize5 = savedSize
            currentNumber5 = savedQuantity
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
        currentNumber5 = 0
        val tvNumber = findViewById<TextView>(R.id.tvNumber5)
        tvNumber.text = currentNumber5.toString()

        // Reset the size selection
        selectedSize5 = "none"
        val ivSmall5 = findViewById<ImageView>(R.id.ivSmall5)
        val ivMedium5 = findViewById<ImageView>(R.id.ivMedium5)
        val ivLarge5 = findViewById<ImageView>(R.id.ivLarge5)
        val tvVolumeSize5 = findViewById<TextView>(R.id.tvVolume_Size5)

        ivSmall5.setBackgroundResource(R.drawable.small_white)
        ivMedium5.setBackgroundResource(R.drawable.medium_white)
        ivLarge5.setBackgroundResource(R.drawable.large_white)
        tvVolumeSize5.text = "0ml"
    }
}