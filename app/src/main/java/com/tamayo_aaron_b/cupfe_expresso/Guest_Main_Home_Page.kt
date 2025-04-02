package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Guest_Main_Home_Page : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    private val handler = android.os.Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_main_home_page)

        // Get references to navigation buttons
        val Guest_navHome = findViewById<ImageView>(R.id.Guest_nav_home)
        val Guest_navCart = findViewById<ImageView>(R.id.Guest_nav_cart)
        val Guest_navFavorite = findViewById<ImageView>(R.id.Guest_nav_favorite)
        val Guest_navBag = findViewById<ImageView>(R.id.Guest_nav_bag)
        val Guest_navNotif = findViewById<ImageView>(R.id.Guest_nav_notif)
        val Guest_item1 = findViewById<ImageView>(R.id.Guest_item1)
        val Guest_cart = findViewById<ImageView>(R.id.Guest_cart)
        val floatingText = findViewById<TextView>(R.id.floatingText)

        val Guest_viewFlipper = findViewById<ViewFlipper>(R.id.Guest_viewFlipper)
        // Set flipping interval (e.g., 1 second)
        Guest_viewFlipper.flipInterval = 5000



        // Ensure it stays in the center
        floatingText.post {
            val parentHeight = (floatingText.parent as View).height
            val parentWidth = (floatingText.parent as View).width

            floatingText.x = (parentWidth - floatingText.width) / 220f
            floatingText.y = (parentHeight - floatingText.height) / 2 + 1085f

            floatingText.visibility = View.VISIBLE
        }

        // Delay flipping start by 2 seconds
        handler.postDelayed({
            Guest_viewFlipper.startFlipping()
        }, 6000)

        floatingText.setOnClickListener {
            val floatBg = Intent(this, sign_in_page::class.java)
            startActivity(floatBg)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        Guest_item1.setOnClickListener {
            val food1 = Intent(this, sign_in_page::class.java)
            startActivity(food1)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        Guest_cart.setOnClickListener{
            val cart = Intent(this, sign_in_page::class.java)
            startActivity(cart)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }



        // Highlight Home button in brown by default on HomePage
        lastClickedButton = Guest_navHome
        Guest_navHome.setImageResource(R.drawable.homes_brown)

        //NAVIGATION
        // Add click listeners with animations and image change
        setupNavigation1(Guest_navHome, "Guest_Home", R.drawable.homes, R.drawable.homes_brown)
        setupNavigation1(Guest_navCart, "Guest_Cart", R.drawable.menu, R.drawable.menu_brown)
        setupNavigation1(Guest_navFavorite, "Guest_Favorite", R.drawable.fav, R.drawable.fav_brown)
        setupNavigation1(Guest_navBag, "Guest_Notification", R.drawable.notif, R.drawable.notif_brown)
        setupNavigation1(Guest_navNotif, "Guest_Me", R.drawable.me, R.drawable.me_brown)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupNavigation1(
        button: ImageView,
        label: String,
        defaultImage: Int,
        brownImage: Int
    ) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // Apply scale animation
                    applyScaleAnimation1(button, 1.0f)

                    // Change the current button's image to the brown version
                    button.setImageResource(brownImage)

                    // Reset the last clicked button to its default image (if different)
                    lastClickedButton?.let {
                        if (it != button) {
                            it.setImageResource(getDefaultImage1(it.id))
                            applyScaleAnimation1(it, 1.0f) // Reset animation for the previous button
                        }
                    }

                    // Update the last clicked button
                    lastClickedButton = button

                    // Perform navigation or actions
                    navigateTo1(label)
                }
                MotionEvent.ACTION_CANCEL -> {
                    // Reset animation if the touch is canceled
                    applyScaleAnimation1(button, 1.0f)
                }
            }
            true
        }
    }

    private fun applyScaleAnimation1(view: ImageView, scale: Float) {
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

    private fun navigateTo1(label: String) {
        when (label) {
            "Guest_Home" -> {
                // Navigate to Home screen

            }
            "Guest_Cart" -> {
                val cart = Intent(this, sign_in_page::class.java)
                startActivity(cart)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Guest_Favorite" -> {
                val favorite = Intent(this, sign_in_page::class.java)
                startActivity(favorite)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Guest_Notification" -> {
                val notification = Intent(this, sign_in_page::class.java)
                startActivity(notification)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)

            }
            "Guest_Me" -> {
                val me = Intent(this, sign_in_page::class.java)
                startActivity(me)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
        }
    }

    private fun getDefaultImage1(buttonId: Int): Int {
        // Return the default image based on the button's ID
        return when (buttonId) {
            R.id.Guest_nav_home -> R.drawable.homes
            R.id.Guest_nav_cart -> R.drawable.menu
            R.id.Guest_nav_favorite -> R.drawable.fav
            R.id.Guest_nav_bag -> R.drawable.notif
            R.id.Guest_nav_notif -> R.drawable.me
            else -> R.drawable.homes // Fallback image
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Prevent memory leaks
    }

    override fun onResume() {
        super.onResume()

        // If the user is back on the home page, ensure the home button is highlighted
        lastClickedButton?.setImageResource(getDefaultImage1(lastClickedButton!!.id))
        lastClickedButton = findViewById(R.id.Guest_nav_home)
        lastClickedButton?.setImageResource(R.drawable.homes_brown)
    }
}