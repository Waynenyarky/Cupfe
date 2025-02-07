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

class details_food2 : AppCompatActivity() {
    private var isHeartRed2 = false
    private var selectedSize2 = "none"
    private var currentNumber2 = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_food2)

        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)

        val heartIcon2 = findViewById<ImageView>(R.id.heartIcon2)
        val ivSmall2 = findViewById<ImageView>(R.id.ivSmall2)
        val ivMedium2 = findViewById<ImageView>(R.id.ivMedium2)
        val ivLarge2 = findViewById<ImageView>(R.id.ivLarge2)
        val ivMinus2 = findViewById<ImageView>(R.id.ivMinus2)
        val tvNumber2 = findViewById<TextView>(R.id.tvNumber2)
        val ivAdd2 = findViewById<ImageView>(R.id.ivAdd2)
        val tvVolumeSize2 = findViewById<TextView>(R.id.tvVolume_Size2)
        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)

        // Back Home
        ivBackHome.setOnClickListener {
            startActivity(Intent(this, Main_Home_Page::class.java))
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }

        // Load favorite state
        loadFavoriteState(heartIcon2)

        // Logic for selecting size
        ivSmall2.setOnClickListener { handleSizeSelection("Small", ivSmall2, ivMedium2, ivLarge2, tvVolumeSize2, "240ml") }
        ivMedium2.setOnClickListener { handleSizeSelection("Medium", ivSmall2, ivMedium2, ivLarge2, tvVolumeSize2, "350ml") }
        ivLarge2.setOnClickListener { handleSizeSelection("Large", ivSmall2, ivMedium2, ivLarge2, tvVolumeSize2, "450ml") }

        // Logic for quantity
        tvNumber2.text = currentNumber2.toString()

        ivMinus2.setOnClickListener {
            if (currentNumber2 > 0) {
                currentNumber2--
                tvNumber2.text = currentNumber2.toString()
            }
        }

        ivAdd2.setOnClickListener {
            if (currentNumber2 < 500) {
                currentNumber2++
                tvNumber2.text = currentNumber2.toString()
            }
        }

        heartIcon2.setOnClickListener {
            if (isHeartRed2) {
                // Allow removing favorite without checking size or quantity
                toggleFavorite(heartIcon2)
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize2 == "none" || currentNumber2 == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleFavorite(heartIcon2)
                }
            }
        }

        tvNumber2.setOnClickListener {
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
                    currentNumber2 = newQuantity
                    tvNumber2.text = currentNumber2.toString()
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
        val tvPrice2= findViewById<TextView>(R.id.tvPrice2) // Reference to price TextView
        val price2 = when (size) {
            "Small" -> "₱87.00"
            "Medium" -> "₱97.00"
            "Large" -> "₱107.00"
            else -> "₱0.00"
        }

        if (selectedSize2 != size) {
            ivSmall.setBackgroundResource(if (size == "Small") R.drawable.small_brown else R.drawable.small_white)
            ivMedium.setBackgroundResource(if (size == "Medium") R.drawable.medium_brown else R.drawable.medium_white)
            ivLarge.setBackgroundResource(if (size == "Large") R.drawable.large_brown else R.drawable.large_white)

            selectedSize2 = size
            tvVolumeSize.text = volume
            tvPrice2.text = price2 // Update price when size is selected
        } else {
            selectedSize2 = "none"
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            tvVolumeSize.text = "0ml"
            tvPrice2.text = "₱0.00" // Reset price when deselected
        }
    }

    private fun toggleFavorite(heartIcon: ImageView) {
        val coffeeName2 = findViewById<TextView>(R.id.tvCoffeeName2).text.toString()
        val priceText2 = findViewById<TextView>(R.id.tvPrice2).text.toString()
        val editor = sharedPreferences.edit()

        if (!isHeartRed2) { // Only validate if adding to favorites
            if (selectedSize2 == "none" || currentNumber2 == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }
        }

        if (isHeartRed2) {
            editor.remove("coffeeName2")
            editor.remove("selectedSize2")
            editor.remove("quantity2")
            editor.remove("totalPrice2")
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) - 1
            editor.putInt("favoriteCount", favoriteCount.coerceAtLeast(0))
            editor.apply()

            isHeartRed2 = false
            heartIcon.setImageResource(R.drawable.fav_heart_white)
            showCustomAlertDialog("Removed from Favorites", "Item has been removed.")
        } else {
            // **Adding to favorites (Validation already passed)**
            val price = priceText2.replace("₱", "").replace(".00", "").toInt()
            val totalPrice2 = price * currentNumber2

            editor.putString("coffeeName2", coffeeName2)
            editor.putString("selectedSize2", selectedSize2)
            editor.putInt("quantity2", currentNumber2)
            editor.putInt("totalPrice2", totalPrice2)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isHeartRed2 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            showCustomAlertDialog("Added to Favorites", "Size: $selectedSize2\nQuantity: $currentNumber2\nTotal: ₱$totalPrice2.00")
        }
    }

    private fun loadFavoriteState(heartIcon: ImageView) {
        val savedName = sharedPreferences.getString("coffeeName2", null)
        val savedSize = sharedPreferences.getString("selectedSize2", null)
        val savedQuantity = sharedPreferences.getInt("quantity2", 0)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isHeartRed2 = true
            heartIcon.setImageResource(R.drawable.fav_heart_red)
            selectedSize2 = savedSize
            currentNumber2 = savedQuantity
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
        currentNumber2 = 0
        val tvNumber = findViewById<TextView>(R.id.tvNumber2)
        tvNumber.text = currentNumber2.toString()

        // Reset the size selection
        selectedSize2 = "none"
        val ivSmall2 = findViewById<ImageView>(R.id.ivSmall2)
        val ivMedium2 = findViewById<ImageView>(R.id.ivMedium2)
        val ivLarge2 = findViewById<ImageView>(R.id.ivLarge2)
        val tvVolumeSize2 = findViewById<TextView>(R.id.tvVolume_Size2)

        ivSmall2.setBackgroundResource(R.drawable.small_white)
        ivMedium2.setBackgroundResource(R.drawable.medium_white)
        ivLarge2.setBackgroundResource(R.drawable.large_white)
        tvVolumeSize2.text = "0ml"
    }
}