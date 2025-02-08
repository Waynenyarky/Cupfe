package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.MotionEvent
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.logging.Handler

class Main_Home_Page : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    private val handler = android.os.Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_page)

        // Get references to navigation buttons
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val item1 = findViewById<ImageView>(R.id.item1)
        val item2 = findViewById<ImageView>(R.id.item2)
        val item3 = findViewById<ImageView>(R.id.item3)
        val item4 = findViewById<ImageView>(R.id.item4)
        val item5 = findViewById<ImageView>(R.id.item5)
        val item6 = findViewById<ImageView>(R.id.item6)
        val viewFlipper = findViewById<ViewFlipper>(R.id.viewFlipper)
        // Set flipping interval (e.g., 1 second)
        viewFlipper.flipInterval = 5000

        // Delay flipping start by 2 seconds
        handler.postDelayed({
            viewFlipper.startFlipping()
        }, 6000)

        item1.setOnClickListener {
            val food1 = Intent(this, details_food1::class.java)
            startActivity(food1)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        item2.setOnClickListener{
            val food2 = Intent(this, details_food2::class.java)
            startActivity(food2)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        item3.setOnClickListener{
            val food3 = Intent(this, details_food3::class.java)
            startActivity(food3)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        item4.setOnClickListener{
            val food4 = Intent(this, details_food4::class.java)
            startActivity(food4)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        item5.setOnClickListener{
            val food5 = Intent(this, details_food5::class.java)
            startActivity(food5)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        item6.setOnClickListener{
            val food6 = Intent(this, details_food6::class.java)
            startActivity(food6)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }



        // Highlight Home button in brown by default on HomePage
        lastClickedButton = navHome
        navHome.setImageResource(R.drawable.home_brown)

        //NAVIGATION
        // Add click listeners with animations and image change
        setupNavigation(navHome, "Home", R.drawable.home, R.drawable.home_brown)
        setupNavigation(navCart, "Cart", R.drawable.food, R.drawable.food_brown)
        setupNavigation(navFavorite, "Favorite", R.drawable.heart, R.drawable.heart_brown)
        setupNavigation(navBag, "Bag", R.drawable.bag, R.drawable.bag_brown)
        setupNavigation(navNotif, "Notification", R.drawable.notification, R.drawable.notification_brown)
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
                // Navigate to Home screen

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
            "Bag" -> {

            }
            "Notification" -> {

            }
        }
    }

    private fun getDefaultImage(buttonId: Int): Int {
        // Return the default image based on the button's ID
        return when (buttonId) {
            R.id.nav_home -> R.drawable.home
            R.id.nav_cart -> R.drawable.food
            R.id.nav_favorite -> R.drawable.heart
            R.id.nav_bag -> R.drawable.bag
            R.id.nav_notif -> R.drawable.notification
            else -> R.drawable.home // Fallback image
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Prevent memory leaks
    }
}