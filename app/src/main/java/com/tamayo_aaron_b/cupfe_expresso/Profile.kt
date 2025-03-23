package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    private lateinit var imageProfile: ShapeableImageView
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var authToken: String
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Get references to navigation buttons
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val applicationTerms = findViewById<LinearLayout>(R.id.applicationTerms)
        val privacyNotice = findViewById<LinearLayout>(R.id.privacyNotice)
        val help = findViewById<LinearLayout>(R.id.help)
        val delete = findViewById<LinearLayout>(R.id.delete)
        val feedback = findViewById<LinearLayout>(R.id.feedback)
        val personalInfo = findViewById<LinearLayout>(R.id.personalInfo)
        val btnSigOut = findViewById<Button>(R.id.btnSigOut)
        val profileName = findViewById<TextView>(R.id.profileName)
        imageProfile = findViewById(R.id.imageProfile)

        // Get the image URI from EditProfile
        val imageUri = intent.getStringExtra("IMAGE_URI") ?: getSavedImageUri()
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)


        imageUri?.let {
            imageProfile.setImageURI(Uri.parse(it)) // Display image in Profile
        }

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        // Get name from Intent or SharedPreferences
        val username = intent.getStringExtra("USERNAME")

        if (!username.isNullOrEmpty()) {
            // Save name in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("USERNAME", username)
            editor.apply()
        }

        // Display saved name
        val usernames = sharedPreferences.getString("USERNAME", "User")
        profileName.text = usernames

        btnSigOut.setOnClickListener{showSuccessDialog()}

        userEmail = sharedPreferences.getString("user_email", "") ?: ""
        authToken = sharedPreferences.getString("auth_token", "") ?: ""

        Log.d("DEBUG", "Name 123 Saved: $userEmail")
        Log.d("DEBUG", "Name 123 Saved: $authToken")


        applicationTerms.setOnClickListener{
            val terms = Intent(this, Application_Terms::class.java)
            startActivity(terms)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        personalInfo.setOnClickListener{
            val info = Intent(this, EditProfile::class.java)
            startActivity(info)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        privacyNotice.setOnClickListener{
            val notice = Intent(this, Privacy_Notice::class.java)
            startActivity(notice)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        feedback.setOnClickListener{
            val feed = Intent(this, Feedback::class.java)
            startActivity(feed)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        help.setOnClickListener{
            val helps = Intent(this, Help::class.java)
            startActivity(helps)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        ivBack.setOnClickListener{
            val back = Intent(this, Main_Home_Page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        delete.setOnClickListener { confirmationDelete() }




        navNotif.setImageResource(R.drawable.me_brown)

        //NAVIGATION
        // Add click listeners with animations and image change
        setupNavigation(navHome, "Home", R.drawable.homes, R.drawable.homes_brown)
        setupNavigation(navCart, "Cart", R.drawable.menu, R.drawable.menu_brown)
        setupNavigation(navFavorite, "Favorite", R.drawable.fav, R.drawable.fav_brown)
        setupNavigation(navBag, "Notification", R.drawable.notif, R.drawable.notif_brown)
        setupNavigation(navNotif, "Profile", R.drawable.me, R.drawable.me_brown)
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_sign_out)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnOkay = dialog.findViewById<Button>(R.id.btnOkay)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        btnOkay.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, create_sign_in_up_page::class.java)
            startActivity(intent)
            Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        btnCancel.setOnClickListener{dialog.dismiss()}

        dialog.show()
    }

    private fun getSavedImageUri(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("saved_image_uri", null)
    }

    private fun confirmationDelete(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.delete_acc)

        val btnNo = dialog.findViewById<TextView>(R.id.btnNo)
        val btnYes = dialog.findViewById<TextView>(R.id.btnYes)

        btnNo.setOnClickListener { dialog.dismiss() }

        btnYes.setOnClickListener{
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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