package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class food_menu : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_menu)

        // Get references to navigation buttons
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val item1 = findViewById<ImageView>(R.id.item1)
        val popular1 = findViewById<ImageView>(R.id.popular1)
        val popular2 = findViewById<ImageView>(R.id.popular2)
        val popular3 = findViewById<ImageView>(R.id.popular3)
        val popular4 = findViewById<ImageView>(R.id.popular4)
        val popular5 = findViewById<ImageView>(R.id.popular5)
        val popular6 = findViewById<ImageView>(R.id.popular6)




        ivBack.setOnClickListener{
            val back = Intent(this, Main_Home_Page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        item1.setOnClickListener{
            showPopup()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }


        popular1.setOnClickListener {
            val food1 = Intent(this, details_food1::class.java)
            startActivity(food1)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        popular2.setOnClickListener{
            val food2 = Intent(this, details_food2::class.java)
            startActivity(food2)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        popular3.setOnClickListener{
            val food3 = Intent(this, details_food3::class.java)
            startActivity(food3)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        popular4.setOnClickListener{
            val food4 = Intent(this, details_food4::class.java)
            startActivity(food4)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        popular5.setOnClickListener{
            val food5 = Intent(this, details_food5::class.java)
            startActivity(food5)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        popular6.setOnClickListener{
            val food6 = Intent(this, details_food6::class.java)
            startActivity(food6)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }









        navCart.setImageResource(R.drawable.menu_brown)

        //NAVIGATION
        // Add click listeners with animations and image change
        setupNavigation(navHome, "Home", R.drawable.homes, R.drawable.homes_brown)
        setupNavigation(navCart, "Cart", R.drawable.menu, R.drawable.menu_brown)
        setupNavigation(navFavorite, "Favorite", R.drawable.fav, R.drawable.fav_brown)
        setupNavigation(navBag, "Notification", R.drawable.notif, R.drawable.notif_brown)
        setupNavigation(navNotif, "Profile", R.drawable.me, R.drawable.me_brown)
    }

    private fun showPopup() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_hot_coffees) // Create a separate XML layout for the popup

        // Prevent dismissing the dialog when clicking outside
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)


        // Make the background transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Find the close button inside the popup
        val close = dialog.findViewById<ImageView>(R.id.close)

        // Close the dialog when clicking the close button
        close.setOnClickListener {
            dialog.dismiss()
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
                val home = Intent(this,Main_Home_Page::class.java)
                startActivity(home)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)

            }
            "Cart" -> {


            }
            "Favorite" -> {
                val favorite = Intent(this, favoriteNav::class.java)
                startActivity(favorite)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }
            "Notification" -> {

            }
            "Profile" -> {

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