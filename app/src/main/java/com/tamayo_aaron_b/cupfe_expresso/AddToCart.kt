package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tamayo_aaron_b.cupfe_expresso.cartFolder.CartAdapter
import com.tamayo_aaron_b.cupfe_expresso.cartFolder.CartItem

class AddToCart : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvOverallPrice: TextView
    private lateinit var ivOverallCheck: ImageView
    private lateinit var tvNumberCoffee: TextView
    private lateinit var checkOut: ImageView
    private var isSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_to_cart)

        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        tvOverallPrice = findViewById(R.id.tvOverallPrice)
        ivOverallCheck = findViewById(R.id.ivOverallCheck)
        tvNumberCoffee = findViewById(R.id.tvNumberCoffee)
        checkOut = findViewById(R.id.checkOut)

        sharedPreferences = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.recyclerViewAddCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, mutableListOf(), tvOverallPrice, tvNumberCoffee)
        recyclerView.adapter = cartAdapter

        loadCartData()

        // Attach swipe-to-delete functionality
        cartAdapter.attachSwipeToDelete(recyclerView,this)

        // Check if the cart should be updated
        if (intent.getBooleanExtra("update_cart", false)) {
            loadCartData()
        }


        ivBack.setOnClickListener {
            // Before navigating, save the updated cart data to SharedPreferences
            saveCartData()

            // Proceed with the navigation
            val back = Intent(this, Main_Home_Page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Toggle selection and compute total price
        ivOverallCheck.setOnClickListener {
            isSelected = !isSelected // Toggle selection state

            if (isSelected) {
                ivOverallCheck.setBackgroundResource(R.drawable.circle_checked)
            } else {
                ivOverallCheck.setBackgroundResource(R.drawable.circle_non_check)
            }

            cartAdapter.setAllItemsChecked(isSelected) // Update all radio buttons
        }

        checkOut.setOnClickListener {
            val selectedItems = cartAdapter.cartItems.filter { it.isSelected }

            if (selectedItems.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("No Items Selected")
                    .setMessage("Please select at least one item to checkout.")
                    .setPositiveButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            // Get the overall price from tvOverallPrice
            val overallPrice = tvOverallPrice.text.toString()

            // Create an intent to navigate to the Order_Summary activity
            val intent = Intent(this, Order_Summary::class.java)
            intent.putExtra("selected_cart_items", ArrayList(selectedItems)) // Pass selected items
            intent.putExtra("overall_price", overallPrice) // Pass overall price
            startActivity(intent)
        }




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

    private fun loadCartData() {
        val cartJson = sharedPreferences.getString("cart_items", "[]")
        val cartList: MutableList<CartItem> = Gson().fromJson(cartJson, object : TypeToken<MutableList<CartItem>>() {}.type) ?: mutableListOf()
        cartAdapter.updateCart(cartList)
        if (::cartAdapter.isInitialized) {
            cartAdapter.updateCart(cartList) // ✅ Use updateCart here
        } else {
            cartAdapter = CartAdapter(this, cartList, tvOverallPrice, tvNumberCoffee)
            recyclerView.adapter = cartAdapter
        }
    }

    private fun saveCartData() {
        val sharedPreferences = getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert cart items list to JSON string
        val cartJson = Gson().toJson(cartAdapter.cartItems)
        editor.putString("cart_items", cartJson)
        editor.apply()  // Save to SharedPreferences
    }

    fun showEmptyCartDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnFindCupFe).setOnClickListener {
            val intent = Intent(this, food_menu::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        dialog.show()
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
                var email = intent.getStringExtra("user_email")
                if (email.isNullOrEmpty()) {
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    email = sharedPreferences.getString("user_email", "") ?: ""
                }


                val home = Intent(this,Main_Home_Page::class.java)
                intent.putExtra("user_email", email) // Send email
                startActivity(home)
                Log.d("DEBUG", "Email Sent to Notifications: $email")
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Cart" -> {
                var email = intent.getStringExtra("user_email")
                if (email.isNullOrEmpty()) {
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    email = sharedPreferences.getString("user_email", "") ?: ""
                }

                val cart = Intent(this, food_menu::class.java)
                intent.putExtra("user_email", email) // Send email
                startActivity(cart)
                Log.d("DEBUG", "Email Sent to Notifications: $email")
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Favorite" -> {
                var email = intent.getStringExtra("user_email")
                if (email.isNullOrEmpty()) {
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    email = sharedPreferences.getString("user_email", "") ?: ""
                }

                val favorite = Intent(this, favoriteNav::class.java)
                intent.putExtra("user_email", email) // Send email
                startActivity(favorite)
                Log.d("DEBUG", "Email Sent to Notifications: $email")
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Notification" -> {
                var email = intent.getStringExtra("user_email")
                if (email.isNullOrEmpty()) {
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    email = sharedPreferences.getString("user_email", "") ?: ""
                }

                val intent = Intent(this, Notifications::class.java)
                intent.putExtra("user_email", email) // Send email
                startActivity(intent)
                Log.d("DEBUG", "Email Sent to Notifications: $email")
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Me" -> {
                var email = intent.getStringExtra("user_email")
                if (email.isNullOrEmpty()) {
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    email = sharedPreferences.getString("user_email", "") ?: ""
                }


                val me = Intent(this, Profile::class.java)
                intent.putExtra("user_email", email) // Send email
                startActivity(me)
                Log.d("DEBUG", "Email Sent to Notifications: $email")
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


}