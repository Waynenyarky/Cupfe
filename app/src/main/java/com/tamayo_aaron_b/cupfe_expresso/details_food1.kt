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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tamayo_aaron_b.cupfe_expresso.menu.Coffee

class details_food1 : AppCompatActivity() {

    private var isHeartRed = false
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
            findViewById<TextView>(R.id.tvPrice).text = "₱0.00"
            findViewById<TextView>(R.id.tvNumber).text = "0"
            etComment.setText("")

            // Reset Selection UI
            ivSmall.setBackgroundResource(R.drawable.small_white)
            ivMedium.setBackgroundResource(R.drawable.medium_white)
            ivLarge.setBackgroundResource(R.drawable.large_white)

            finish()
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }




        // Load favorite state
        loadFavoriteState(heartIcon)
        loadCart()

        //BUY NOW
        ivBuyNow.setOnClickListener {
            if (selectedSize == "none" || currentNumber == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
            } else {
                val priceText = findViewById<TextView>(R.id.tvPrice).text.toString()
                val cleanedPrice = priceText.replace("₱", "").replace(".00", "").trim()


                // Save selected size and quantity before navigating
                val prefs = getSharedPreferences("TempSelection", Context.MODE_PRIVATE).edit()
                prefs.putString("selectedSize", selectedSize)
                prefs.putInt("quantity", currentNumber)
                val userComment1 = etComment.text.toString()
                prefs.putBoolean("fromOrderSummary", true)
                prefs.apply()

                val intent = Intent(this, Order_Summary::class.java).apply {
                    putExtra("coffeeName", findViewById<TextView>(R.id.tvCoffeeName).text.toString())
                    putExtra("selectedSize", selectedSize)
                    putExtra("quantity", currentNumber)
                    putExtra("userComment1", userComment1)
                    putExtra("price", cleanedPrice)
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

        ivAddToCart.setOnClickListener {
            if (isAddedCart) {
                // Allow removing favorite without checking size or quantity
                toggleCart()
            } else {
                // Only show alert if trying to add to favorites without size or quantity
                if (selectedSize == "none" || currentNumber == 0) {
                    showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                } else {
                    // Proceed to add to favorites
                    toggleCart()
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

    private fun handleSizeSelection(coffee: Coffee?, size: String, ivSmall: ImageView, ivMedium: ImageView, ivLarge: ImageView, tvVolumeSize: TextView, volume: String) {
        val tvPrice= findViewById<TextView>(R.id.tvPrice) // Reference to price TextView
        val price = when (size) {
            "Small" -> coffee?.priceSmall
            "Medium" -> coffee?.priceMedium
            "Large" -> coffee?.priceLarge
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

    private fun toggleCart() {
        val coffeeName = findViewById<TextView>(R.id.tvCoffeeName).text.toString()
        val priceText = findViewById<TextView>(R.id.tvPrice).text.toString()
        val savedComment = findViewById<TextView>(R.id.etComment).text.toString()
        val editor = sharedPreferences1.edit()

        if (!isAddedCart) { // Ensuring it's only added once
            if (selectedSize == "none" || currentNumber == 0) {
                showCustomAlertDialog("Incomplete Selection", "Please select a size and quantity first.")
                return
            }

            // Adding to cart after validation
            val price = priceText.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0
            val totalPrice = price * currentNumber

            editor.putString("coffeeName", coffeeName)
            editor.putString("userComment", savedComment)
            editor.putString("selectedSize", selectedSize)
            editor.putInt("quantity", currentNumber)
            editor.putInt("totalPrice", totalPrice)
            val favoriteCount = sharedPreferences.getInt("favoriteCount", 0) + 1
            editor.putInt("favoriteCount", favoriteCount)
            editor.apply()

            isAddedCart = true
            showCustomAlertDialog("Added to Cart", "Size: $selectedSize\nQuantity: $currentNumber\nTotal: ₱$totalPrice.00")
        } else {
            showCustomAlertDialog("Already Added", "This item is already in your cart.")
        }
    }



    private fun loadCart() {
        val savedName = sharedPreferences1.getString("coffeeName", null)
        val savedSize = sharedPreferences1.getString("selectedSize", null)
        val savedQuantity = sharedPreferences1.getInt("quantity", 0)
        val savedComment = sharedPreferences1.getString("userComment", null)

        if (!savedName.isNullOrEmpty() && !savedSize.isNullOrEmpty() && savedQuantity > 0) {
            isAddedCart = true
            selectedSize = savedSize
            currentNumber = savedQuantity
            findViewById<EditText>(R.id.etComment).setText(savedComment)
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
                "Small" -> "₱99.00"
                "Medium" -> "₱109.00"
                "Large" -> "₱119.00"
                else -> "₱0.00"
            }

            // Reset flag so it doesn't retain old values indefinitely
            prefs.edit().putBoolean("fromOrderSummary", false).apply()
        }
    }

    private fun resetSelections() {
        selectedSize = "none"
        currentNumber = 0

        findViewById<TextView>(R.id.tvVolume_Size).text = "0ml"
        findViewById<TextView>(R.id.tvPrice).text = "₱0.00"
        findViewById<TextView>(R.id.tvNumber).text = "0"

        ivSmall.setBackgroundResource(R.drawable.small_white)
        ivMedium.setBackgroundResource(R.drawable.medium_white)
        ivLarge.setBackgroundResource(R.drawable.large_white)
    }
}

