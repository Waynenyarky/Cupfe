package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import com.tamayo_aaron_b.cupfe_expresso.notificationFolder.NotificationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.Manifest

class Notifications : AppCompatActivity() {

    private var lastClickedButton: ImageView? = null // Track the last clicked button

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var currentEmail: String? = null  // Store the last searched ref number
    private var isFetching = false



    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE)
            }
        }

        // Get references to navigation buttons
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val ivCart = findViewById<ImageView>(R.id.ivCart)

        recyclerView = findViewById(R.id.recyclerViewNotification)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotificationAdapter(emptyList())
        recyclerView.adapter = adapter

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            currentEmail?.let {
                if (it.isNotEmpty()) {
                    fetchNotifications(it)
                }
            }
        }


        ivCart.setOnClickListener{
            val cart = Intent(this, AddToCart::class.java)
            startActivity(cart)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        ivBack.setOnClickListener{
            val back = Intent(this, Main_Home_Page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        navBag.setImageResource(R.drawable.notif_brown)
        //NAVIGATION
        // Add click listeners with animations and image change
        setupNavigation(navHome, "Home", R.drawable.homes, R.drawable.homes_brown)
        setupNavigation(navCart, "Cart", R.drawable.menu, R.drawable.menu_brown)
        setupNavigation(navFavorite, "Favorite", R.drawable.fav, R.drawable.fav_brown)
        setupNavigation(navBag, "Notification", R.drawable.notif, R.drawable.notif_brown)
        setupNavigation(navNotif, "Me", R.drawable.me, R.drawable.me_brown)

        val email = intent.getStringExtra("user_email")
        Log.d("DEBUG", "Received Email in Notifications: $email")

        // Start auto-refresh only if email is valid
        if (!email.isNullOrEmpty()) {
            currentEmail = email
            fetchNotifications(email)
        } else {
            Log.e("DEBUG", "Email is null or empty, cannot fetch notifications")
        }
    }

    private fun fetchNotifications(email: String) {
        if (isFetching) return  // Avoid redundant calls
        isFetching = true
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient.instance.getNotifications(email)
            .enqueue(object : Callback<List<NotificationResponse>> {
                override fun onResponse(
                    call: Call<List<NotificationResponse>>,
                    response: Response<List<NotificationResponse>>
                ) {
                    swipeRefreshLayout.isRefreshing = false
                    isFetching = false
                    if (response.isSuccessful) {
                        val notifications = response.body() ?: emptyList()
                        adapter = NotificationAdapter(notifications)
                        recyclerView.adapter = adapter

                        // Retrieve stored notification count
                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val storedCount = sharedPreferences.getInt("lastNotificationCount", 0)

                        // Check for new notifications
                        if (notifications.size > storedCount) {
                            showNotification()
                            sharedPreferences.edit().putInt("lastNotificationCount", notifications.size).apply()
                        }
                    } else {
                        Toast.makeText(this@Notifications, "Failed to get data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<NotificationResponse>>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                    isFetching = false
                    Log.e("API_ERROR", "Error: ${t.message}")
                    Toast.makeText(this@Notifications, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Function to show the notification
    private fun showNotification() {
        // Check if the app has permission to post notifications for SDK 33 and above (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("Notification", "Permission not granted, skipping notification")
                return // Skip showing the notification if permission is not granted
            }
        }

        val channelId = "cupfe_expresso_notifications"
        val notificationId = 1

        // Retrieve email from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", "") ?: ""

        // Create an intent to open the Notifications activity
        val intent = Intent(this, Notifications::class.java).apply {
            putExtra("user_email", email)  // Pass the email to the Notifications activity
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a PendingIntent for the notification to open the intent
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create notification channel for Android Oreo (SDK 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Cupfe Expresso Notifications"
            val descriptionText = "Channel for notification updates"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.new_notification)  // Set the small icon for the notification
            .setContentTitle("New Updates")  // Set the title of the notification
            .setContentText("You have new notifications in Cupfe Expresso.")  // Set the content text
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Set notification priority
            .setContentIntent(pendingIntent)  // Set the intent to open when tapped
            .setAutoCancel(true)  // Automatically remove the notification when tapped

        // Notify the user
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification.build())  // Send the notification with a unique ID
        }
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
                //NOTIFICATION
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Notification permission granted")
            } else {
                Log.e("Permission", "Notification permission denied")
            }
        }
    }

}