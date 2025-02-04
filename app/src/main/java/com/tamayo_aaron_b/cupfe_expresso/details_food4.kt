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

class details_food4 : AppCompatActivity() {
    private var isHeartRed4 = false
    private var selectedSize4 = "none"
    private var currentNumber4 = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food4)

        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)

        val heartIcon4 = findViewById<ImageView>(R.id.heartIcon4)
        val ivSmall4 = findViewById<ImageView>(R.id.ivSmall4)
        val ivMedium4 = findViewById<ImageView>(R.id.ivMedium4)
        val ivLarge4 = findViewById<ImageView>(R.id.ivLarge4)
        val ivMinus4 = findViewById<ImageView>(R.id.ivMinus4)
        val tvNumber4 = findViewById<TextView>(R.id.tvNumber4)
        val ivAdd4 = findViewById<ImageView>(R.id.ivAdd4)
        val tvVolumeSize4 = findViewById<TextView>(R.id.tvVolume_Size4)
        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)

        // Back Home
        ivBackHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Load favorite state
        loadFavoriteState(heartIcon4)

        // Logic for selecting size
        ivSmall4.setOnClickListener { handleSizeSelection("Small", ivSmall4, ivMedium4, ivLarge4, tvVolumeSize4, "240ml") }
        ivMedium4.setOnClickListener { handleSizeSelection("Medium", ivSmall4, ivMedium4, ivLarge4, tvVolumeSize4, "350ml") }
        ivLarge4.setOnClickListener { handleSizeSelection("Large", ivSmall4, ivMedium4, ivLarge4, tvVolumeSize4, "450ml") }

        // Logic for quantity
        tvNumber4.text = currentNumber4.toString()

        ivMinus4.setOnClickListener {
            if (currentNumber4 > 0) {
                currentNumber4--
                tvNumber4.text = currentNumber4.toString()
            }
        }

        ivAdd4.setOnClickListener {
            if (currentNumber4 < 500) {
                currentNumber4++
                tvNumber4.text = currentNumber4.toString()
            }
        }

        heartIcon4.setOnClickListener {
            if (isHeartRed4) {
                // Allow removing favorite without checking size or quantity
                toggleFavorite(heartIcon4)
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize4 == "none" || currentNumber4 == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleFavorite(heartIcon4)
                }
            }
        }

        tvNumber4.setOnClickListener {
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
                    currentNumber4 = newQuantity
                    tvNumber4.text = currentNumber4.toString()
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
        val tvPrice4= findViewById<TextView>(R.id.tvPrice4) // Reference to price TextView
        val price4 = when (size) {
            "Small" -> "₱49.00"
            "Medium" -> "₱59.00"
            "Large" -> "₱69.00"
            else -> "₱0.00"
        }

        if (selectedSize4 != size) {
            ivSmall.setBackgroundResource(if (size == "Small") R.drawable.small_brown else R.drawable.small_white)
            ivMedium.setBackgroundResource(if (size == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
            ivLarge.setBackgroundResource(if (size == "Large") R.drawable.large_brown else R.drawable.large_white)

            selectedSize4 = size
            tvVolumeSize.text = volume
            tvPrice4.text = price4 // Update price when size is selected
        } else {
            selectedSize4 = "none"
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            tvVolumeSize.text = "0ml"
            tvPrice4.text = "₱0.00" // Reset price when deselected
        }
    }

    private fun toggleFavorite(heartIcon: ImageView) {
        val coffeeName4 = findViewById<TextView>(R.id.tvCoffeeName4).text.toString()
        val priceText4 = findViewById<TextView>(R.id.tvPrice4).text.toString()
        val editor = sharedPreferences.edit()

        if (!isHeartRed4) { // Only validate if adding to favorites
            if (selectedSize4 == "none" || currentNumber4 == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }
        }

        if (isHeartRed4) {
            editor.remove("coffeeName4")
            editor.remove("selectedSize4")
            editor.remove("quantity4")
            editor.remove("totalPrice4")
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) - 1
            editor.putInt("favoriteCount", favoriteCount.coerceAtLeast(0))
            editor.apply()

            isHeartRed4 = false
            heartIcon.setImageResource(R.drawable.fav_heart_white)
            showCustomAlertDialog("Removed from Favorites", "Item has been removed.")
        } else {
            // **Adding to favorites (Validation already passed)**
            val price = priceText4.replace( "₱", "").replace(".00", "").toInt()
            val totalPrice4 = price * currentNumber4

            editor.putString("coffeeName4", coffeeName4)
            editor.putString("selectedSize4", selectedSize4)
            editor.putInt("quantity4", currentNumber4)
            editor.putInt("totalPrice4", totalPrice4)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isHeartRed4 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            showCustomAlertDialog("Added to Favorites", "Size: $selectedSize4\nQuantity: $currentNumber4\nTotal: ₱$totalPrice4.00")
        }
    }

    private fun loadFavoriteState(heartIcon: ImageView) {
        val savedName = sharedPreferences.getString("coffeeName4", null)
        val savedSize = sharedPreferences.getString("selectedSize4", null)
        val savedQuantity = sharedPreferences.getInt("quantity4", 0)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isHeartRed4 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            selectedSize4 = savedSize
            currentNumber4 = savedQuantity
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
        currentNumber4 = 0
        val tvNumber = findViewById<TextView>(R.id.tvNumber4)
        tvNumber.text = currentNumber4.toString()

        // Reset the size selection
        selectedSize4 = "none"
        val ivSmall4 = findViewById<ImageView>(R.id.ivSmall4)
        val ivMedium4 = findViewById<ImageView>(R.id.ivMedium4)
        val ivLarge4 = findViewById<ImageView>(R.id.ivLarge4)
        val tvVolumeSize4 = findViewById<TextView>(R.id.tvVolume_Size4)

        ivSmall4.setBackgroundResource(R.drawable.small_white)
        ivMedium4.setBackgroundResource(R.drawable.medium_white)
        ivLarge4.setBackgroundResource(R.drawable.large_white)
        tvVolumeSize4.text = "0ml"
    }
}