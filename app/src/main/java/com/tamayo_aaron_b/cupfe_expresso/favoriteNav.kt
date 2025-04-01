package com.tamayo_aaron_b.cupfe_expresso

import FavoriteItem
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamayo_aaron_b.cupfe_expresso.favorite.FavoriteAdapter
import com.tamayo_aaron_b.cupfe_expresso.notificationFolder.NotificationService
import com.tamayo_aaron_b.cupfe_expresso.summary.OrderAdapter

class favoriteNav : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var ivOverallCheck: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_nav)

        val email = intent.getStringExtra("user_email") ?: ""
        if (email.isNotEmpty()) {
            NotificationService(this, email).startChecking()
        }

        // Get references to navigation buttons
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val ivCart = findViewById<ImageView>(R.id.ivCart)
        ivOverallCheck = findViewById(R.id.ivOverallCheck)


        val favoriteItem: FavoriteItem? = intent.getParcelableExtra("favoriteItem")

        val recyclerView: RecyclerView = findViewById(R.id.favorite_layout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        favoriteItem?.let {
            val favoriteList = listOf(it) // Wrap in a list
            recyclerView.adapter = FavoriteAdapter(favoriteList)
        }




        ivBack.setOnClickListener {
            val back = Intent(this, Main_Home_Page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        ivCart.setOnClickListener{
            val cart = Intent(this, AddToCart::class.java)
            startActivity(cart)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }



        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)
        // Ensure overall check state is updated correctly

        // Set the heart icon to brown when on the favoriteNav page
        navFavorite.setImageResource(R.drawable.fav_brown)

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
                // Navigate to Favorite screen
            }
            "Notification" -> {
                // Retrieve email from intent or SharedPreferences
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



    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {

        // Call the super method to retain the default back press behavior
        super.onBackPressedDispatcher.onBackPressed()
    }

}