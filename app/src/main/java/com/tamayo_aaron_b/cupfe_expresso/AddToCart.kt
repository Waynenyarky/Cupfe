package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AddToCart : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    private lateinit var sharedPreferences1: SharedPreferences
    private lateinit var ivCheck1: ImageView
    private lateinit var ivCheck2: ImageView
    private lateinit var ivCheck3: ImageView
    private lateinit var ivCheck4: ImageView
    private lateinit var ivCheck5: ImageView
    private lateinit var ivCheck6: ImageView
    private lateinit var ivOverallCheck: ImageView

    private lateinit var tvAdd: TextView
    private lateinit var tvMinus: TextView
    private lateinit var tvAdd2: TextView
    private lateinit var tvMinus2: TextView
    private lateinit var tvAdd3: TextView
    private lateinit var tvMinus3: TextView
    private lateinit var tvAdd4: TextView
    private lateinit var tvMinus4: TextView
    private lateinit var tvAdd5: TextView
    private lateinit var tvMinus5: TextView
    private lateinit var tvAdd6: TextView
    private lateinit var tvMinus6: TextView

    private lateinit var delete1: ImageView
    private lateinit var delete2: ImageView
    private lateinit var delete3: ImageView
    private lateinit var delete4: ImageView
    private lateinit var delete5: ImageView
    private lateinit var delete6: ImageView


    private var checkboxesState = mutableListOf(false, false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_to_cart)

        // Get references to navigation buttons
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        delete1 = findViewById(R.id.delete1)
        delete2 = findViewById(R.id.delete2)
        delete3 = findViewById(R.id.delete3)
        delete4 = findViewById(R.id.delete4)
        delete5 = findViewById(R.id.delete5)
        delete6 = findViewById(R.id.delete6)
        tvAdd = findViewById(R.id.tvAdd)
        tvMinus = findViewById(R.id.tvMinus)
        tvAdd2 = findViewById(R.id.tvAdd2)
        tvMinus2 = findViewById(R.id.tvMinus2)
        tvAdd3 = findViewById(R.id.tvAdd3)
        tvMinus3 = findViewById(R.id.tvMinus3)
        tvAdd4 = findViewById(R.id.tvAdd4)
        tvMinus4 = findViewById(R.id.tvMinus4)
        tvAdd5 = findViewById(R.id.tvAdd5)
        tvMinus5 = findViewById(R.id.tvMinus5)
        tvAdd6 = findViewById(R.id.tvAdd6)
        tvMinus6 = findViewById(R.id.tvMinus6)
        ivCheck1 = findViewById(R.id.ivCheck1)
        ivCheck2 = findViewById(R.id.ivCheck2)
        ivCheck3 = findViewById(R.id.ivCheck3)
        ivCheck4 = findViewById(R.id.ivCheck4)
        ivCheck5 = findViewById(R.id.ivCheck5)
        ivCheck6 = findViewById(R.id.ivCheck6)
        ivOverallCheck = findViewById(R.id.ivOverallCheck)


        // Set up click listeners for hearts
        delete1.setOnClickListener { showDeletedDialog(1) }
        delete2.setOnClickListener { showDeletedDialog(2) }
        delete3.setOnClickListener { showDeletedDialog(3) }
        delete4.setOnClickListener { showDeletedDialog(4) }
        delete5.setOnClickListener { showDeletedDialog(5) }
        delete6.setOnClickListener { showDeletedDialog(6) }

        ivBack.setOnClickListener {
            val back = Intent(this, Main_Home_Page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Set up click listeners for individual checkboxes
        ivCheck1.setOnClickListener { toggleCheckbox(0, ivCheck1, tvAdd, tvMinus, tvAdd2, tvMinus2, tvAdd3, tvMinus3, tvAdd4, tvMinus4, tvAdd5, tvMinus5, tvAdd6, tvMinus6, delete1, delete2, delete3, delete4, delete5, delete6) }
        ivCheck2.setOnClickListener { toggleCheckbox(1, ivCheck2, tvAdd, tvMinus, tvAdd2, tvMinus2, tvAdd3, tvMinus3, tvAdd4, tvMinus4, tvAdd5, tvMinus5, tvAdd6, tvMinus6, delete1, delete2, delete3, delete4, delete5, delete6) }
        ivCheck3.setOnClickListener { toggleCheckbox(2, ivCheck3, tvAdd, tvMinus, tvAdd2, tvMinus2, tvAdd3, tvMinus3, tvAdd4, tvMinus4, tvAdd5, tvMinus5, tvAdd6, tvMinus6, delete1, delete2, delete3, delete4, delete5, delete6) }
        ivCheck4.setOnClickListener { toggleCheckbox(3, ivCheck4, tvAdd, tvMinus, tvAdd2, tvMinus2, tvAdd3, tvMinus3, tvAdd4, tvMinus4, tvAdd5, tvMinus5, tvAdd6, tvMinus6, delete1, delete2, delete3, delete4, delete5, delete6) }
        ivCheck5.setOnClickListener { toggleCheckbox(4, ivCheck5, tvAdd, tvMinus, tvAdd2, tvMinus2, tvAdd3, tvMinus3, tvAdd4, tvMinus4, tvAdd5, tvMinus5, tvAdd6, tvMinus6, delete1, delete2, delete3, delete4, delete5, delete6) }
        ivCheck6.setOnClickListener { toggleCheckbox(5, ivCheck6, tvAdd, tvMinus, tvAdd2, tvMinus2, tvAdd3, tvMinus3, tvAdd4, tvMinus4, tvAdd5, tvMinus5, tvAdd6, tvMinus6, delete1, delete2, delete3, delete4, delete5, delete6) }


        tvAdd.setOnClickListener { checkboxesState[0] }
        tvAdd2.setOnClickListener { checkboxesState[1] }
        tvAdd3.setOnClickListener { checkboxesState[2] }
        tvAdd4.setOnClickListener { checkboxesState[3] }
        tvAdd5.setOnClickListener { checkboxesState[4] }
        tvAdd6.setOnClickListener { checkboxesState[5] }
        tvMinus.setOnClickListener { checkboxesState[0] }
        tvMinus2.setOnClickListener { checkboxesState[1] }
        tvMinus3.setOnClickListener { checkboxesState[2] }
        tvMinus4.setOnClickListener { checkboxesState[3] }
        tvMinus5.setOnClickListener { checkboxesState[4] }
        tvMinus6.setOnClickListener { checkboxesState[5] }

        ivOverallCheck.setOnClickListener { toggleOverallCheckbox() }

        sharedPreferences1 = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
        loadCart()
        saveCheckboxState()
        updateOverallCheckState()


        // NAVIGATION
        // Add click listeners with animations and image change
        setupNavigation(navHome, "Home", R.drawable.homes, R.drawable.homes_brown)
        setupNavigation(navCart, "Cart", R.drawable.menu, R.drawable.menu_brown)
        setupNavigation(navFavorite, "Favorite", R.drawable.fav, R.drawable.fav_brown)
        setupNavigation(navBag, "Notification", R.drawable.notif, R.drawable.notif_brown)
        setupNavigation(
            navNotif,
            "Me",
            R.drawable.me,
            R.drawable.me_brown
        )
    }

    @SuppressLint("SetTextI18n")
    private fun toggleCheckbox(
        index: Int,
        checkbox: ImageView,
        tvAdd: TextView, tvMinus: TextView,
        tvAdd2: TextView, tvMinus2: TextView,
        tvAdd3: TextView, tvMinus3: TextView,
        tvAdd4: TextView, tvMinus4: TextView,
        tvAdd5: TextView, tvMinus5: TextView,
        tvAdd6: TextView, tvMinus6: TextView,
        delete1: ImageView, delete2: ImageView,
        delete3: ImageView, delete4: ImageView,
        delete5: ImageView, delete6: ImageView
    ) {
        // Toggle the state of the specific checkbox
        checkboxesState[index] = !checkboxesState[index]

        // Update the checkbox image
        setCheckboxState(index, checkboxesState[index], checkbox)

        // Save the state of checkboxes
        saveCheckboxState()


        // Disable or enable corresponding add/minus buttons based on checkbox state
        when (index) {
            0 -> {
                tvAdd.isEnabled = !checkboxesState[index]
                tvMinus.isEnabled = !checkboxesState[index]
                tvAdd.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                tvMinus.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                delete1.isEnabled = !checkboxesState[index]
                delete1.alpha = if (checkboxesState[index]) 0.5f else 1.0f
            }
            1 -> {
                tvAdd2.isEnabled = !checkboxesState[index]
                tvMinus2.isEnabled = !checkboxesState[index]
                tvAdd2.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                tvMinus2.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                delete2.isEnabled = !checkboxesState[index]
                delete2.alpha = if (checkboxesState[index]) 0.5f else 1.0f
            }
            2 -> {
                tvAdd3.isEnabled = !checkboxesState[index]
                tvMinus3.isEnabled = !checkboxesState[index]
                tvAdd3.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                tvMinus3.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                delete3.isEnabled = !checkboxesState[index]
                delete3.alpha = if (checkboxesState[index]) 0.5f else 1.0f
            }
            3 -> {
                tvAdd4.isEnabled = !checkboxesState[index]
                tvMinus4.isEnabled = !checkboxesState[index]
                tvAdd4.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                tvMinus4.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                delete4.isEnabled = !checkboxesState[index]
                delete4.alpha = if (checkboxesState[index]) 0.5f else 1.0f
            }
            4 -> {
                tvAdd5.isEnabled = !checkboxesState[index]
                tvMinus5.isEnabled = !checkboxesState[index]
                tvAdd5.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                tvMinus5.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                delete5.isEnabled = !checkboxesState[index]
                delete5.alpha = if (checkboxesState[index]) 0.5f else 1.0f
            }
            5 -> {
                tvAdd6.isEnabled = !checkboxesState[index]
                tvMinus6.isEnabled = !checkboxesState[index]
                tvAdd6.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                tvMinus6.alpha = if (checkboxesState[index]) 0.5f else 1.0f
                delete6.isEnabled = !checkboxesState[index]
                delete6.alpha = if (checkboxesState[index]) 0.5f else 1.0f
            }
        }

        // Find all TextViews related to prices
        val tvOverallPrice = findViewById<TextView>(R.id.tvOverallPrice)
        val tvFavPriceValue = findViewById<TextView>(R.id.tvFavPriceValue)
        val tvFavPriceValue2 = findViewById<TextView>(R.id.tvFavPriceValue2)
        val tvFavPriceValue3 = findViewById<TextView>(R.id.tvFavPriceValue3)
        val tvFavPriceValue4 = findViewById<TextView>(R.id.tvFavPriceValue4)
        val tvFavPriceValue5 = findViewById<TextView>(R.id.tvFavPriceValue5)
        val tvFavPriceValue6 = findViewById<TextView>(R.id.tvFavPriceValue6)

        // Convert TextView values to Integers safely
        fun getPriceValue(textView: TextView): Int {
            return try {
                textView.text.toString().replace("₱", "").replace(".00", "").trim().toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }

        // Calculate total price based on selected checkboxes
        var overallPrice = 0
        if (checkboxesState[0]) overallPrice += getPriceValue(tvFavPriceValue)
        if (checkboxesState[1]) overallPrice += getPriceValue(tvFavPriceValue2)
        if (checkboxesState[2]) overallPrice += getPriceValue(tvFavPriceValue3)
        if (checkboxesState[3]) overallPrice += getPriceValue(tvFavPriceValue4)
        if (checkboxesState[4]) overallPrice += getPriceValue(tvFavPriceValue5)
        if (checkboxesState[5]) overallPrice += getPriceValue(tvFavPriceValue6)

        // Update the UI with the calculated total price
        tvOverallPrice.text = "₱$overallPrice.00"



        // Update the overall checkbox state
        updateOverallCheckState()
    }


    @SuppressLint("SetTextI18n")
    private fun toggleOverallCheckbox() {
        val newState = !checkboxesState.all { it }

        for (i in checkboxesState.indices) {
            checkboxesState[i] = newState
            when (i) {
                0 -> ivCheck1
                1 -> ivCheck2
                2 -> ivCheck3
                3 -> ivCheck4
                4 -> ivCheck5
                5 -> ivCheck6
                else -> null
            }?.setImageResource(if (newState) R.drawable.circle_check else R.drawable.circle_non_check)
        }

        ivOverallCheck.setImageResource(if (newState) R.drawable.circle_check else R.drawable.circle_non_check)

        saveCheckboxState()

        var overallPrice = 0
        if (checkboxesState[0]) overallPrice += sharedPreferences1.getInt("totalPrice", 0)
        if (checkboxesState[1]) overallPrice += sharedPreferences1.getInt("totalPrice2", 0)
        if (checkboxesState[2]) overallPrice += sharedPreferences1.getInt("totalPrice3", 0)
        if (checkboxesState[3]) overallPrice += sharedPreferences1.getInt("totalPrice4", 0)
        if (checkboxesState[4]) overallPrice += sharedPreferences1.getInt("totalPrice5", 0)
        if (checkboxesState[5]) overallPrice += sharedPreferences1.getInt("totalPrice6", 0)

        val tvOverallPrice = findViewById<TextView>(R.id.tvOverallPrice)
        val tvFavPriceValue = findViewById<TextView>(R.id.tvFavPriceValue)
        val tvFavPriceValue2 = findViewById<TextView>(R.id.tvFavPriceValue2)
        val tvFavPriceValue3 = findViewById<TextView>(R.id.tvFavPriceValue3)
        val tvFavPriceValue4 = findViewById<TextView>(R.id.tvFavPriceValue4)
        val tvFavPriceValue5 = findViewById<TextView>(R.id.tvFavPriceValue5)
        val tvFavPriceValue6 = findViewById<TextView>(R.id.tvFavPriceValue6)

        // Ensure tvFavPriceValue has the correct value
        val favPriceText = tvFavPriceValue.text.toString()
        val favPriceText2 = tvFavPriceValue2.text.toString()
        val favPriceText3 = tvFavPriceValue3.text.toString()
        val favPriceText4 = tvFavPriceValue4.text.toString()
        val favPriceText5 = tvFavPriceValue5.text.toString()
        val favPriceText6 = tvFavPriceValue6.text.toString()

        val currentTotalPrices = listOf(
            favPriceText.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText2.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText3.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText4.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText5.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText6.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0
        )


        val computedPrice = if (checkboxesState.all { it }) {
            currentTotalPrices.sum()
        } else {
            overallPrice
        }

        tvOverallPrice.text = "₱$computedPrice.00"

        // Disable tvAdd and tvMinus if overall checkbox is checked
        if (newState) {
            tvAdd.isEnabled = false
            delete1.isEnabled = false
            tvMinus.isEnabled = false
            tvAdd.alpha = 0.5f  // Change opacity to indicate disabled state
            delete1.alpha = 0.5f
            tvMinus.alpha = 0.5f
        } else {
            tvAdd.isEnabled = true
            delete1.isEnabled = true
            tvMinus.isEnabled = true
            tvAdd.alpha = 1.0f  // Reset opacity to normal
            delete1.alpha = 1.0f
            tvMinus.alpha = 1.0f
        }

        if (newState) {
            tvAdd2.isEnabled = false
            delete2.isEnabled = false
            tvMinus2.isEnabled = false
            tvAdd2.alpha = 0.5f  // Change opacity to indicate disabled state
            delete2.alpha = 0.5f
            tvMinus2.alpha = 0.5f
        } else {
            tvAdd2.isEnabled = true
            delete2.isEnabled = true
            tvMinus2.isEnabled = true
            tvAdd2.alpha = 1.0f  // Reset opacity to normal
            delete2.alpha = 1.0f
            tvMinus2.alpha = 1.0f
        }

        if (newState) {
            tvAdd3.isEnabled = false
            delete3.isEnabled = false
            tvMinus3.isEnabled = false
            tvAdd3.alpha = 0.5f  // Change opacity to indicate disabled state
            delete3.alpha = 0.5f
            tvMinus3.alpha = 0.5f
        } else {
            tvAdd3.isEnabled = true
            delete3.isEnabled = true
            tvMinus3.isEnabled = true
            tvAdd3.alpha = 1.0f  // Reset opacity to normal
            delete3.alpha = 1.0f
            tvMinus3.alpha = 1.0f
        }

        if (newState) {
            tvAdd4.isEnabled = false
            delete4.isEnabled = false
            tvMinus4.isEnabled = false
            tvAdd4.alpha = 0.5f  // Change opacity to indicate disabled state
            delete4.alpha = 0.5f
            tvMinus4.alpha = 0.5f
        } else {
            tvAdd4.isEnabled = true
            delete4.isEnabled = true
            tvMinus4.isEnabled = true
            tvAdd4.alpha = 1.0f  // Reset opacity to normal
            delete4.alpha = 1.0f
            tvMinus4.alpha = 1.0f
        }

        if (newState) {
            tvAdd5.isEnabled = false
            delete5.isEnabled = false
            tvMinus5.isEnabled = false
            tvAdd5.alpha = 0.5f  // Change opacity to indicate disabled state
            delete5.alpha = 0.5f
            tvMinus5.alpha = 0.5f
        } else {
            tvAdd5.isEnabled = true
            delete5.isEnabled = true
            tvMinus5.isEnabled = true
            tvAdd5.alpha = 1.0f  // Reset opacity to normal
            delete5.alpha = 1.0f
            tvMinus5.alpha = 1.0f
        }

        if (newState) {
            tvAdd6.isEnabled = false
            delete6.isEnabled = false
            tvMinus6.isEnabled = false
            tvAdd6.alpha = 0.5f  // Change opacity to indicate disabled state
            delete6.alpha = 0.5f
            tvMinus6.alpha = 0.5f
        } else {
            tvAdd6.isEnabled = true
            delete6.isEnabled = true
            tvMinus6.isEnabled = true
            tvAdd6.alpha = 1.0f  // Reset opacity to normal
            delete6.alpha = 1.0f
            tvMinus6.alpha = 1.0f
        }

        updateOverallPrice()
    }

    @SuppressLint("SetTextI18n")
    private fun updateOverallPrice() {
        var overallPrice = 0
        val sharedPreferencesKeys = listOf("totalPrice", "totalPrice2", "totalPrice3", "totalPrice4", "totalPrice5", "totalPrice6")
        for (i in checkboxesState.indices) {
            if (checkboxesState[i]) overallPrice += sharedPreferences1.getInt(sharedPreferencesKeys[i], 0)
        }

        val tvOverallPrice = findViewById<TextView>(R.id.tvOverallPrice)
        val tvFavPriceValue = findViewById<TextView>(R.id.tvFavPriceValue)
        val tvFavPriceValue2 = findViewById<TextView>(R.id.tvFavPriceValue2)
        val tvFavPriceValue3 = findViewById<TextView>(R.id.tvFavPriceValue3)
        val tvFavPriceValue4 = findViewById<TextView>(R.id.tvFavPriceValue4)
        val tvFavPriceValue5 = findViewById<TextView>(R.id.tvFavPriceValue5)
        val tvFavPriceValue6 = findViewById<TextView>(R.id.tvFavPriceValue6)

        // Extract numeric value from tvFavPriceValue.text safely
        val favPriceText = tvFavPriceValue.text.toString()
        val favPriceText2 = tvFavPriceValue2.text.toString()
        val favPriceText3 = tvFavPriceValue3.text.toString()
        val favPriceText4 = tvFavPriceValue4.text.toString()
        val favPriceText5 = tvFavPriceValue5.text.toString()
        val favPriceText6 = tvFavPriceValue6.text.toString()

        val currentTotalPrices = listOf(
            favPriceText.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText2.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText3.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText4.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText5.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0,
            favPriceText6.replace("₱", "").replace(".00", "").toIntOrNull() ?: 0
        )

        val computedPrice = if (checkboxesState.all { it }) {
            currentTotalPrices.sum()
        } else {
            overallPrice
        }

        tvOverallPrice.text = "₱$computedPrice.00"
    }


    private fun setCheckboxState(index: Int, isChecked: Boolean, checkbox: ImageView) {
        // Set the image resource based on whether the checkbox is checked
        checkbox.setImageResource(if (isChecked) R.drawable.circle_check else R.drawable.circle_non_check)
    }


    private fun updateOverallCheckState() {
        // Check if all checkboxes are checked
        val allChecked = checkboxesState.all { it }

        // Update the overall checkbox drawable
        ivOverallCheck.setImageResource(if (allChecked) R.drawable.circle_check else R.drawable.circle_non_check)

        // Save overall check state in SharedPreferences
        sharedPreferences1.edit().putBoolean("ivOverallCheck", allChecked).apply()
    }

    private fun saveCheckboxState() {
        val editor = sharedPreferences1.edit()
        checkboxesState.forEachIndexed { index, isChecked ->
            editor.putBoolean("isChecked$index", isChecked)
        }
        editor.putBoolean("isOverallChecked", checkboxesState.all { it })

        // Save the overall price in SharedPreferences
        var overallPrice = 0
        if (checkboxesState[0]) overallPrice += sharedPreferences1.getInt("totalPrice", 0)
        if (checkboxesState[1]) overallPrice += sharedPreferences1.getInt("totalPrice2", 0)
        if (checkboxesState[2]) overallPrice += sharedPreferences1.getInt("totalPrice3", 0)
        if (checkboxesState[3]) overallPrice += sharedPreferences1.getInt("totalPrice4", 0)
        if (checkboxesState[4]) overallPrice += sharedPreferences1.getInt("totalPrice5", 0)
        if (checkboxesState[5]) overallPrice += sharedPreferences1.getInt("totalPrice6", 0)

        editor.putInt("overallPrice", overallPrice)  // Save overall price
        editor.apply()
    }

    private fun showDeletedDialog(deleteId: Int) {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.delete_alert_dialog, null)

        // Create the AlertDialog using the custom layout
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val alert = builder.create()

        // Get the buttons from the custom layout
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)

        // Set button actions
        btnYes.setOnClickListener {
            deleteItem(deleteId)
            alert.dismiss()
        }

        btnNo.setOnClickListener {
            alert.dismiss()
        }

        alert.show()
    }

    // Function to handle unfavoriting the item
    private fun deleteItem(deleteId: Int) {
        val editor = sharedPreferences1.edit()

        // Unfavorite logic for each heart icon, using heartId to determine which one
        when (deleteId) {
            1 -> {
                val coffeeName = sharedPreferences1.getString("coffeeName", "N/A")
                editor.remove("coffeeName")
                editor.remove("cartQuantity${coffeeName}")
                editor.remove("selectedSize")
                editor.remove("quantity")
                editor.remove("totalPrice")
                findViewById<LinearLayout>(R.id.favFoodLayout).visibility = View.GONE
            }
            2 -> {
                val coffeeName2 = sharedPreferences1.getString("coffeeName2", "N/A")
                editor.remove("coffeeName2")
                editor.remove("cartQuantity2${coffeeName2}")
                editor.remove("selectedSize2")
                editor.remove("quantity2")
                editor.remove("totalPrice2")
                findViewById<LinearLayout>(R.id.favFoodLayout2).visibility = View.GONE
            }
            3 -> {
                val coffeeName3 = sharedPreferences1.getString("coffeeName3", "N/A")
                editor.remove("coffeeName3")
                editor.remove("cartQuantity3${coffeeName3}")
                editor.remove("selectedSize3")
                editor.remove("quantity3")
                editor.remove("totalPrice3")
                findViewById<LinearLayout>(R.id.favFoodLayout3).visibility = View.GONE
            }
            4 -> {
                val coffeeName4 = sharedPreferences1.getString("coffeeName4", "N/A")
                editor.remove("coffeeName4")
                editor.remove("cartQuantity4${coffeeName4}")
                editor.remove("selectedSize4")
                editor.remove("quantity4")
                editor.remove("totalPrice4")
                findViewById<LinearLayout>(R.id.favFoodLayout4).visibility = View.GONE
            }
            5 -> {
                val coffeeName5 = sharedPreferences1.getString("coffeeName5", "N/A")
                editor.remove("coffeeName5")
                editor.remove("cartQuantity5${coffeeName5}")
                editor.remove("selectedSize5")
                editor.remove("quantity5")
                editor.remove("totalPrice5")
                findViewById<LinearLayout>(R.id.favFoodLayout5).visibility = View.GONE
            }
            6 -> {
                val coffeeName6 = sharedPreferences1.getString("coffeeName6", "N/A")
                editor.remove("coffeeName6")
                editor.remove("cartQuantity6${coffeeName6}")
                editor.remove("selectedSize6")
                editor.remove("quantity6")
                editor.remove("totalPrice6")
                findViewById<LinearLayout>(R.id.favFoodLayout6).visibility = View.GONE
            }
        }

        editor.apply()  // Commit changes to SharedPreferences
        updateOverallPrice()
        loadCart()  // Reload favorites after removal
    }

    @SuppressLint("CutPasteId", "SetTextI18n")
    private fun loadCart() {

        checkboxesState.forEachIndexed { index, _ ->
            checkboxesState[index] = sharedPreferences1.getBoolean("isChecked$index", false)
        }

        val coffeeName = sharedPreferences1.getString("coffeeName", "N/A")
        val selectedSize = sharedPreferences1.getString("selectedSize", "N/A")
        val quantity = sharedPreferences1.getInt("quantity", 0)
        val totalPrice = sharedPreferences1.getInt("totalPrice", 0)

        val coffeeName2 = sharedPreferences1.getString("coffeeName2", "N/A")
        val selectedSize2 = sharedPreferences1.getString("selectedSize2", "N/A")
        val quantity2 = sharedPreferences1.getInt("quantity2", 0)
        val totalPrice2 = sharedPreferences1.getInt("totalPrice2", 0)

        val coffeeName3 = sharedPreferences1.getString("coffeeName3", "N/A")
        val selectedSize3 = sharedPreferences1.getString("selectedSize3", "N/A")
        val quantity3 = sharedPreferences1.getInt("quantity3", 0)
        val totalPrice3 = sharedPreferences1.getInt("totalPrice3", 0)

        val coffeeName4 = sharedPreferences1.getString("coffeeName4", "N/A")
        val selectedSize4 = sharedPreferences1.getString("selectedSize4", "N/A")
        val quantity4 = sharedPreferences1.getInt("quantity4", 0)
        val totalPrice4 = sharedPreferences1.getInt("totalPrice4", 0)

        val coffeeName5 = sharedPreferences1.getString("coffeeName5", "N/A")
        val selectedSize5 = sharedPreferences1.getString("selectedSize5", "N/A")
        val quantity5 = sharedPreferences1.getInt("quantity5", 0)
        val totalPrice5 = sharedPreferences1.getInt("totalPrice5", 0)

        val coffeeName6 = sharedPreferences1.getString("coffeeName6", "N/A")
        val selectedSize6 = sharedPreferences1.getString("selectedSize6", "N/A")
        val quantity6 = sharedPreferences1.getInt("quantity6", 0)
        val totalPrice6 = sharedPreferences1.getInt("totalPrice6", 0)


        // Set TextViews for each price
        findViewById<TextView>(R.id.tvFavPriceValue).text = "₱$totalPrice.00"
        findViewById<TextView>(R.id.tvFavPriceValue2).text = "₱$totalPrice2.00"
        findViewById<TextView>(R.id.tvFavPriceValue3).text = "₱$totalPrice3.00"
        findViewById<TextView>(R.id.tvFavPriceValue4).text = "₱$totalPrice4.00"
        findViewById<TextView>(R.id.tvFavPriceValue5).text = "₱$totalPrice5.00"
        findViewById<TextView>(R.id.tvFavPriceValue6).text = "₱$totalPrice6.00"



        // Calculate the total price based on checked items
        var overallPrice = 0
        if (checkboxesState[0]) overallPrice += totalPrice
        if (checkboxesState[1]) overallPrice += totalPrice2
        if (checkboxesState[2]) overallPrice += totalPrice3
        if (checkboxesState[3]) overallPrice += totalPrice4
        if (checkboxesState[4]) overallPrice += totalPrice5
        if (checkboxesState[5]) overallPrice += totalPrice6

        // Update the overall price
        findViewById<TextView>(R.id.tvOverallPrice).text = "₱$overallPrice.00"


        // Calculate the correct favorite count
        var favoriteCount = 0
        if (coffeeName != "N/A") favoriteCount++
        if (coffeeName2 != "N/A") favoriteCount++
        if (coffeeName3 != "N/A") favoriteCount++
        if (coffeeName4 != "N/A") favoriteCount++
        if (coffeeName5 != "N/A") favoriteCount++
        if (coffeeName6 != "N/A") favoriteCount++

        // Update the favorite count in tvNumberCoffee
        findViewById<TextView>(R.id.tvNumberCoffee).text = "($favoriteCount)"

        // Check if the first favorite coffee exists
        if (coffeeName != "N/A") {
            findViewById<LinearLayout>(R.id.favFoodLayout).visibility = View.VISIBLE

            val size = when (selectedSize) {
                "Small" -> "S"
                "Medium" -> "M"
                "Large" -> "L"
                else -> selectedSize
            }

            findViewById<TextView>(R.id.tvFavCoffeeNameValue).text = coffeeName
            findViewById<TextView>(R.id.tvFavSizeValue).text = "Size: $size"

            val addQuantity = findViewById<TextView>(R.id.addQuantity)
            val tvAdd = findViewById<TextView>(R.id.tvAdd)
            val tvMinus = findViewById<TextView>(R.id.tvMinus)
            val tvFavPriceValue = findViewById<TextView>(R.id.tvFavPriceValue)
            val tvFavQuantityValue = findViewById<TextView>(R.id.tvFavQuantityValue)

            val sharedPreferences = sharedPreferences1
            val editor = sharedPreferences.edit()

            // Retrieve stored values (default to at least 1 quantity and correct price)
            var currentQuantity = sharedPreferences.getInt("cartQuantity${coffeeName}", quantity).coerceAtLeast(1)
            var currentTotalPrice = sharedPreferences.getInt("totalPrice${coffeeName}", totalPrice)

            // Compute unit price properly
            val unitPrice = if (quantity > 0) totalPrice / quantity else totalPrice.coerceAtLeast(1)

            // Ensure stored total price is valid
            if (currentTotalPrice == 0) {
                currentTotalPrice = unitPrice * currentQuantity
                editor.putInt("totalPrice", currentTotalPrice).apply() // Save the updated price
            }

            // Set initial UI values
            addQuantity.text = currentQuantity.toString()
            tvFavQuantityValue.text = "Quantity: $currentQuantity"
            tvFavPriceValue.text = "₱$currentTotalPrice.00"

            // Function to update UI and SharedPreferences
            fun updateUI() {
                addQuantity.text = currentQuantity.toString()
                tvFavQuantityValue.text = "Quantity: $currentQuantity"
                tvFavPriceValue.text = "₱$currentTotalPrice.00"

                // Save changes persistently
                editor.putInt("cartQuantity${coffeeName}", currentQuantity)
                editor.putInt("totalPrice${coffeeName}", currentTotalPrice)
                editor.apply()
            }

            // Increase quantity
            tvAdd.setOnClickListener {
                currentQuantity++
                currentTotalPrice = unitPrice * currentQuantity
                updateUI()
            }

            // Decrease quantity (ensure it doesn't go below 1)
            tvMinus.setOnClickListener {
                if (currentQuantity > 1) {
                    currentQuantity--
                    currentTotalPrice = unitPrice * currentQuantity
                    updateUI()
                }
            }

        } else {
            findViewById<LinearLayout>(R.id.favFoodLayout).visibility = View.GONE
        }
        // Check if the second favorite coffee exists 2
        if (coffeeName2 != "N/A") {
            findViewById<LinearLayout>(R.id.favFoodLayout2).visibility = View.VISIBLE
            val size2 = when (selectedSize2) {
                "Small" -> "S"
                "Medium" -> "M"
                "Large" -> "L"
                else -> selectedSize2
            }
            findViewById<TextView>(R.id.tvFavCoffeeNameValue2).text = coffeeName2
            findViewById<TextView>(R.id.tvFavSizeValue2).text = "Size: $size2"

            val addQuantity2 = findViewById<TextView>(R.id.addQuantity2)
            val tvAdd2 = findViewById<TextView>(R.id.tvAdd2)
            val tvMinus2 = findViewById<TextView>(R.id.tvMinus2)
            val tvFavPriceValue2 = findViewById<TextView>(R.id.tvFavPriceValue2)
            val tvFavQuantityValue2 = findViewById<TextView>(R.id.tvFavQuantityValue2)

            val sharedPreferences = sharedPreferences1
            val editor = sharedPreferences.edit()

            // Retrieve stored values (default to at least 1 quantity and correct price)
            var currentQuantity2 = sharedPreferences.getInt("cartQuantity2${coffeeName2}", quantity2).coerceAtLeast(1)
            var currentTotalPrice2 = sharedPreferences.getInt("totalPrice2${coffeeName2}", totalPrice2)

            // Compute unit price properly
            val unitPrice2 = if (quantity2 > 0) totalPrice2 / quantity2 else totalPrice2.coerceAtLeast(1)

            // Ensure stored total price is valid
            if (currentTotalPrice2 == 0) {
                currentTotalPrice2 = unitPrice2 * currentQuantity2
                editor.putInt("totalPrice2", currentTotalPrice2).apply() // Save the updated price
            }

            // Set initial UI values
            addQuantity2.text = currentQuantity2.toString()
            tvFavQuantityValue2.text = "Quantity: $currentQuantity2"
            tvFavPriceValue2.text = "₱$currentTotalPrice2.00"

            // Function to update UI and SharedPreferences
            fun updateUI() {
                addQuantity2.text = currentQuantity2.toString()
                tvFavQuantityValue2.text = "Quantity: $currentQuantity2"
                tvFavPriceValue2.text = "₱$currentTotalPrice2.00"

                // Save changes persistently
                editor.putInt("cartQuantity2${coffeeName2}", currentQuantity2)
                editor.putInt("totalPrice2${coffeeName2}", currentTotalPrice2)
                editor.apply()
            }

            // Increase quantity
            tvAdd2.setOnClickListener {
                currentQuantity2++
                currentTotalPrice2 = unitPrice2 * currentQuantity2
                updateUI()
            }

            // Decrease quantity (ensure it doesn't go below 1)
            tvMinus2.setOnClickListener {
                if (currentQuantity2 > 1) {
                    currentQuantity2--
                    currentTotalPrice2 = unitPrice2 * currentQuantity2
                    updateUI()
                }
            }


        } else {
            findViewById<LinearLayout>(R.id.favFoodLayout2).visibility = View.GONE
        }

        // Check if the second favorite coffee exists 3
        if (coffeeName3 != "N/A") {
            findViewById<LinearLayout>(R.id.favFoodLayout3).visibility = View.VISIBLE
            val size3 = when (selectedSize3) {
                "Small" -> "S"
                "Medium" -> "M"
                "Large" -> "L"
                else -> selectedSize3
            }
            findViewById<TextView>(R.id.tvFavCoffeeNameValue3).text = coffeeName3
            findViewById<TextView>(R.id.tvFavSizeValue3).text = "Size: $size3"
            val addQuantity3 = findViewById<TextView>(R.id.addQuantity3)
            val tvAdd3 = findViewById<TextView>(R.id.tvAdd3)
            val tvMinus3 = findViewById<TextView>(R.id.tvMinus3)
            val tvFavPriceValue3 = findViewById<TextView>(R.id.tvFavPriceValue3)
            val tvFavQuantityValue3 = findViewById<TextView>(R.id.tvFavQuantityValue3)

            val sharedPreferences = sharedPreferences1
            val editor = sharedPreferences.edit()

            // Retrieve stored values (default to at least 1 quantity and correct price)
            var currentQuantity3 = sharedPreferences.getInt("cartQuantity3${coffeeName3}", quantity3).coerceAtLeast(1)
            var currentTotalPrice3 = sharedPreferences.getInt("totalPrice3${coffeeName3}", totalPrice3)

            // Compute unit price properly
            val unitPrice3 = if (quantity3 > 0) totalPrice3 / quantity3 else totalPrice3.coerceAtLeast(1)

            // Ensure stored total price is valid
            if (currentTotalPrice3 == 0) {
                currentTotalPrice3 = unitPrice3 * currentQuantity3
                editor.putInt("totalPrice3", currentTotalPrice3).apply() // Save the updated price
            }

            // Set initial UI values
            addQuantity3.text = currentQuantity3.toString()
            tvFavQuantityValue3.text = "Quantity: $currentQuantity3"
            tvFavPriceValue3.text = "₱$currentTotalPrice3.00"

            // Function to update UI and SharedPreferences
            fun updateUI() {
                addQuantity3.text = currentQuantity3.toString()
                tvFavQuantityValue3.text = "Quantity: $currentQuantity3"
                tvFavPriceValue3.text = "₱$currentTotalPrice3.00"

                // Save changes persistently
                editor.putInt("cartQuantity3${coffeeName3}", currentQuantity3)
                editor.putInt("totalPrice3${coffeeName3}", currentTotalPrice3)
                editor.apply()
            }

            // Increase quantity
            tvAdd3.setOnClickListener {
                currentQuantity3++
                currentTotalPrice3 = unitPrice3 * currentQuantity3
                updateUI()
            }

            // Decrease quantity (ensure it doesn't go below 1)
            tvMinus3.setOnClickListener {
                if (currentQuantity3 > 1) {
                    currentQuantity3--
                    currentTotalPrice3 = unitPrice3 * currentQuantity3
                    updateUI()
                }
            }
        } else {
            findViewById<LinearLayout>(R.id.favFoodLayout3).visibility = View.GONE
        }

        // Check if the second favorite coffee exists 4
        if (coffeeName4 != "N/A") {
            findViewById<LinearLayout>(R.id.favFoodLayout4).visibility = View.VISIBLE
            val size4 = when (selectedSize4) {
                "Small" -> "S"
                "Medium" -> "M"
                "Large" -> "L"
                else -> selectedSize4
            }
            findViewById<TextView>(R.id.tvFavCoffeeNameValue4).text = coffeeName4
            findViewById<TextView>(R.id.tvFavSizeValue4).text = "Size: $size4"
            val addQuantity4 = findViewById<TextView>(R.id.addQuantity4)
            val tvAdd4 = findViewById<TextView>(R.id.tvAdd4)
            val tvMinus4 = findViewById<TextView>(R.id.tvMinus4)
            val tvFavPriceValue4 = findViewById<TextView>(R.id.tvFavPriceValue4)
            val tvFavQuantityValue4 = findViewById<TextView>(R.id.tvFavQuantityValue4)

            val sharedPreferences = sharedPreferences1
            val editor = sharedPreferences.edit()

            // Retrieve stored values (default to at least 1 quantity and correct price)
            var currentQuantity4 = sharedPreferences.getInt("cartQuantity4${coffeeName4}", quantity4).coerceAtLeast(1)
            var currentTotalPrice4 = sharedPreferences.getInt("totalPrice4${coffeeName4}", totalPrice4)

            // Compute unit price properly
            val unitPrice4 = if (quantity4 > 0) totalPrice4 / quantity4 else totalPrice4.coerceAtLeast(1)

            // Ensure stored total price is valid
            if (currentTotalPrice4 == 0) {
                currentTotalPrice4 = unitPrice4 * currentQuantity4
                editor.putInt("totalPrice4", currentTotalPrice4).apply() // Save the updated price
            }

            // Set initial UI values
            addQuantity4.text = currentQuantity4.toString()
            tvFavQuantityValue4.text = "Quantity: $currentQuantity4"
            tvFavPriceValue4.text = "₱$currentTotalPrice4.00"

            // Function to update UI and SharedPreferences
            fun updateUI() {
                addQuantity4.text = currentQuantity4.toString()
                tvFavQuantityValue4.text = "Quantity: $currentQuantity4"
                tvFavPriceValue4.text = "₱$currentTotalPrice4.00"

                // Save changes persistently
                editor.putInt("cartQuantity4${coffeeName4}", currentQuantity4)
                editor.putInt("totalPrice4${coffeeName4}", currentTotalPrice4)
                editor.apply()
            }

            // Increase quantity
            tvAdd4.setOnClickListener {
                currentQuantity4++
                currentTotalPrice4 = unitPrice4 * currentQuantity4
                updateUI()
            }

            // Decrease quantity (ensure it doesn't go below 1)
            tvMinus4.setOnClickListener {
                if (currentQuantity4 > 1) {
                    currentQuantity4--
                    currentTotalPrice4 = unitPrice4 * currentQuantity4
                    updateUI()
                }
            }
        } else {
            findViewById<LinearLayout>(R.id.favFoodLayout4).visibility = View.GONE
        }

        // Check if the second favorite coffee exists 5
        if (coffeeName5 != "N/A") {
            findViewById<LinearLayout>(R.id.favFoodLayout5).visibility = View.VISIBLE
            val size5 = when (selectedSize5) {
                "Small" -> "S"
                "Medium" -> "M"
                "Large" -> "L"
                else -> selectedSize5
            }
            findViewById<TextView>(R.id.tvFavCoffeeNameValue5).text = coffeeName5
            findViewById<TextView>(R.id.tvFavSizeValue5).text = "Size: $size5"
            val addQuantity5 = findViewById<TextView>(R.id.addQuantity5)
            val tvAdd5 = findViewById<TextView>(R.id.tvAdd5)
            val tvMinus5 = findViewById<TextView>(R.id.tvMinus5)
            val tvFavPriceValue5 = findViewById<TextView>(R.id.tvFavPriceValue5)
            val tvFavQuantityValue5 = findViewById<TextView>(R.id.tvFavQuantityValue5)

            val sharedPreferences = sharedPreferences1
            val editor = sharedPreferences.edit()

            // Retrieve stored values (default to at least 1 quantity and correct price)
            var currentQuantity5 = sharedPreferences.getInt("cartQuantity5${coffeeName5}", quantity5).coerceAtLeast(1)
            var currentTotalPrice5 = sharedPreferences.getInt("totalPrice5${coffeeName5}", totalPrice5)

            // Compute unit price properly
            val unitPrice5 = if (quantity5 > 0) totalPrice5 / quantity5 else totalPrice5.coerceAtLeast(1)

            // Ensure stored total price is valid
            if (currentTotalPrice5 == 0) {
                currentTotalPrice5 = unitPrice5 * currentQuantity5
                editor.putInt("totalPrice5", currentTotalPrice5).apply() // Save the updated price
            }

            // Set initial UI values
            addQuantity5.text = currentQuantity5.toString()
            tvFavQuantityValue5.text = "Quantity: $currentQuantity5"
            tvFavPriceValue5.text = "₱$currentTotalPrice5.00"

            // Function to update UI and SharedPreferences
            fun updateUI() {
                addQuantity5.text = currentQuantity5.toString()
                tvFavQuantityValue5.text = "Quantity: $currentQuantity5"
                tvFavPriceValue5.text = "₱$currentTotalPrice5.00"

                // Save changes persistently
                editor.putInt("cartQuantity5${coffeeName5}", currentQuantity5)
                editor.putInt("totalPrice5${coffeeName5}", currentTotalPrice5)
                editor.apply()
            }

            // Increase quantity
            tvAdd5.setOnClickListener {
                currentQuantity5++
                currentTotalPrice5 = unitPrice5 * currentQuantity5
                updateUI()
            }

            // Decrease quantity (ensure it doesn't go below 1)
            tvMinus5.setOnClickListener {
                if (currentQuantity5 > 1) {
                    currentQuantity5--
                    currentTotalPrice5 = unitPrice5 * currentQuantity5
                    updateUI()
                }
            }
        } else {
            findViewById<LinearLayout>(R.id.favFoodLayout5).visibility = View.GONE
        }

        // Check if the second favorite coffee exists 6
        if (coffeeName6 != "N/A") {
            findViewById<LinearLayout>(R.id.favFoodLayout6).visibility = View.VISIBLE
            val size6 = when (selectedSize6) {
                "Small" -> "S"
                "Medium" -> "M"
                "Large" -> "L"
                else -> selectedSize6
            }
            findViewById<TextView>(R.id.tvFavCoffeeNameValue6).text = coffeeName6
            findViewById<TextView>(R.id.tvFavSizeValue6).text = "Size: $size6"
            val addQuantity6 = findViewById<TextView>(R.id.addQuantity6)
            val tvAdd6 = findViewById<TextView>(R.id.tvAdd6)
            val tvMinus6 = findViewById<TextView>(R.id.tvMinus6)
            val tvFavPriceValue6 = findViewById<TextView>(R.id.tvFavPriceValue6)
            val tvFavQuantityValue6 = findViewById<TextView>(R.id.tvFavQuantityValue6)

            val sharedPreferences = sharedPreferences1
            val editor = sharedPreferences.edit()

            // Retrieve stored values (default to at least 1 quantity and correct price)
            var currentQuantity6 = sharedPreferences.getInt("cartQuantity6${coffeeName6}", quantity6).coerceAtLeast(1)
            var currentTotalPrice6 = sharedPreferences.getInt("totalPrice6${coffeeName6}", totalPrice6)

            // Compute unit price properly
            val unitPrice6 = if (quantity6 > 0) totalPrice6 / quantity6 else totalPrice6.coerceAtLeast(1)

            // Ensure stored total price is valid
            if (currentTotalPrice6 == 0) {
                currentTotalPrice6 = unitPrice6 * currentQuantity6
                editor.putInt("totalPrice6", currentTotalPrice6).apply() // Save the updated price
            }

            // Set initial UI values
            addQuantity6.text = currentQuantity6.toString()
            tvFavQuantityValue6.text = "Quantity: $currentQuantity6"
            tvFavPriceValue6.text = "₱$currentTotalPrice6.00"

            // Function to update UI and SharedPreferences
            fun updateUI() {
                addQuantity6.text = currentQuantity6.toString()
                tvFavQuantityValue6.text = "Quantity: $currentQuantity6"
                tvFavPriceValue6.text = "₱$currentTotalPrice6.00"

                // Save changes persistently
                editor.putInt("cartQuantity6${coffeeName6}", currentQuantity6)
                editor.putInt("totalPrice6${coffeeName6}", currentTotalPrice6)
                editor.apply()
            }

            // Increase quantity
            tvAdd6.setOnClickListener {
                currentQuantity6++
                currentTotalPrice6 = unitPrice6 * currentQuantity6
                updateUI()
            }

            // Decrease quantity (ensure it doesn't go below 1)
            tvMinus6.setOnClickListener {
                if (currentQuantity6 > 1) {
                    currentQuantity6--
                    currentTotalPrice6 = unitPrice6 * currentQuantity6
                    updateUI()
                }
            }
        } else {
            findViewById<LinearLayout>(R.id.favFoodLayout6).visibility = View.GONE
        }
        // Show dialog if no favorites exist
        if (favoriteCount == 0) {
            showNoFavoritesDialog()
        }
        updateOverallCheckState()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupNavigation(
        button: ImageView,
        label: String,
        defaultImage: Int,
        brownImage: Int
    ) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // Apply scale animation
                    applyScaleAnimation(button, 1.2f)
                    // Change the current button's image to the brown version
                    button.setImageResource(brownImage)
                    // Reset the last clicked button to its default image (if different)
                    lastClickedButton?.let {
                        if (it != button) {
                            it.setImageResource(getDefaultImage(it.id))
                            applyScaleAnimation(it, 1.0f) // Reset animation for the previous button
                        }
                    }
                    // Update the last clicked button
                    lastClickedButton = button

                    // Perform navigation or actions
                    navigateTo(label)
                }
                MotionEvent.ACTION_CANCEL -> {
                    // Reset animation if the touch is canceled
                    applyScaleAnimation(button, 1.0f)
                }
            }
            true
        }
    }

    private fun applyScaleAnimation(view: ImageView, scale: Float) {
        val scaleAnimation = ScaleAnimation(
            1.0f, scale, // Start and end scale for X
            1.0f, scale, // Start and end scale for Y
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // Pivot X
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f  // Pivot Y
        )
        scaleAnimation.duration = 200 // Animation duration
        scaleAnimation.fillAfter = true // Keep the final state after animation
        view.startAnimation(scaleAnimation)
    }

    private fun navigateTo(label: String) {
        when (label) {
            "Home" -> {
                val home = Intent(this, Main_Home_Page::class.java)
                startActivity(home)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Cart" -> {
                val cart = Intent(this, food_menu::class.java)
                startActivity(cart)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Favorite" -> {
                val favorite = Intent(this, favoriteNav::class.java)
                startActivity(favorite)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Notification" -> {
                val notification = Intent(this, Notifications::class.java)
                startActivity(notification)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Me" -> {
                val me = Intent(this, Profile::class.java)
                startActivity(me)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
        }
    }

    private fun getDefaultImage(buttonId: Int): Int {
        // Return the default image based on the button's ID
        return when (buttonId) {
            R.id.nav_home -> R.drawable.homes
            R.id.nav_cart -> R.drawable.menu
            R.id.nav_favorite -> R.drawable.fav
            R.id.nav_bag -> R.drawable.notif
            R.id.nav_notif -> R.drawable.me
            else -> R.drawable.homes // Fallback image
        }
    }

    //Empty cart
    private fun showNoFavoritesDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Prevent dismissing by clicking outside
            .create()

        val btnFindCupFe = dialogView.findViewById<Button>(R.id.btnFindCupFe)

        btnFindCupFe.setOnClickListener {
            val intent = Intent(this, food_menu::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onStart() {
        super.onStart()

        // Reset the checkbox states and the overall price when navigating back or to home
        resetFavorites()

        // Update the overall price and checkbox state
        updateOverallCheckState()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        // Reset the checkbox states and the overall price before navigating back
        resetFavorites()

        // Call the super method to retain the default back press behavior
        super.onBackPressedDispatcher.onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    private fun resetFavorites() {
        // Reset all the checkboxes to unchecked state
        checkboxesState = MutableList(6) { false }

        // Reset the checkboxes UI
        ivCheck1.setImageResource(R.drawable.circle_non_check)
        ivCheck2.setImageResource(R.drawable.circle_non_check)
        ivCheck3.setImageResource(R.drawable.circle_non_check)
        ivCheck4.setImageResource(R.drawable.circle_non_check)
        ivCheck5.setImageResource(R.drawable.circle_non_check)
        ivCheck6.setImageResource(R.drawable.circle_non_check)
        ivOverallCheck.setImageResource(R.drawable.circle_non_check)

        // Reset the overall price
        findViewById<TextView>(R.id.tvOverallPrice).text = "₱0.00"
    }

}