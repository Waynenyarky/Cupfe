package com.tamayo_aaron_b.cupfe_expresso

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.tamayo_aaron_b.cupfe_expresso.menu.Coffee
import com.tamayo_aaron_b.cupfe_expresso.menu.CoffeeAdapter
import com.tamayo_aaron_b.cupfe_expresso.notificationFolder.NotificationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Main_Home_Page : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    private val handler = android.os.Handler(Looper.getMainLooper())
    private lateinit var imageProfile: ShapeableImageView
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerViewCoffees: RecyclerView
    private lateinit var coffeeAdapter: CoffeeAdapter

    private lateinit var fabScrollTop: FloatingActionButton
    private lateinit var scrollView: NestedScrollView
    private var isAtBottom = false

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home_page)

        val email = intent.getStringExtra("user_email") ?: ""
        if (email.isNotEmpty()) {
            NotificationService(this, email).startChecking()
        }

        // Get references to navigation buttons
        val ivReservation = findViewById<ImageView>(R.id.ivReservation)
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val homeName = findViewById<TextView>(R.id.homeName)
        val viewFlipper = findViewById<ViewFlipper>(R.id.viewFlipper)
        val ivCart = findViewById<ImageView>(R.id.ivCart)
        val search = findViewById<EditText>(R.id.search)
        val receipts = findViewById<ImageView>(R.id.receipts)
        val trackOrder = findViewById<ImageView>(R.id.trackOrder)
        imageProfile = findViewById(R.id.imageProfile)
        viewFlipper.flipInterval = 5000
        fabScrollTop = findViewById(R.id.fabScrollTop)
        scrollView = findViewById(R.id.scrollView)

        // Listen for scroll changes to determine if at the bottom of the scroll
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollViewHeight = scrollView.getChildAt(0).height
            val scrollPosition = scrollView.height + scrollView.scrollY
            isAtBottom = scrollPosition >= scrollViewHeight

            if (isAtBottom) {
                // Show the FAB when at the bottom
                fabScrollTop.visibility = View.VISIBLE
            } else {
                // Hide the FAB when not at the bottom
                fabScrollTop.visibility = View.GONE
            }
        }

        // Set up the FAB to scroll to the top when clicked
        fabScrollTop.setOnClickListener {
            val animator = ObjectAnimator.ofInt(scrollView, "scrollY", 0)
            animator.duration = 1200 // Adjust duration for smoother effect
            animator.start()
        }

        // Make FAB "jump" every 3 seconds
        val handler = Handler(Looper.getMainLooper())
        val jumpRunnable = object : Runnable {
            override fun run() {
                if (fabScrollTop.visibility == View.VISIBLE) {
                    fabScrollTop.animate()
                        .translationY(-20f) // Move up
                        .setDuration(300)
                        .withEndAction {
                            fabScrollTop.animate()
                                .translationY(0f) // Move back down
                                .setDuration(300)
                        }
                }
                handler.postDelayed(this, 2000) // Repeat every 3 seconds
            }
        }

        // Start the jump animation loop
        handler.postDelayed(jumpRunnable, 2000)

        //SEARCH ITEM NAME (not case sensitive)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchCoffees(query)
                } else {
                    fetchCoffees() // Load all items when search is empty
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        handler.postDelayed({
            viewFlipper.startFlipping()
        }, 6000)

        // Get the image URI from EditProfile
        val imageUri = intent.getStringExtra("IMAGE_URI") ?: getSavedImageUri()

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Retrieve saved name
        val savedName = sharedPreferences.getString("USERNAME", "User")

        if (!savedName.isNullOrEmpty()) {
            homeName.text = "$savedName"
        }

        imageUri?.let {
            imageProfile.setImageURI(Uri.parse(it)) // Display image in Profile
        }


        ivCart.setOnClickListener {
            val cart = Intent(this, AddToCart::class.java)
            startActivity(cart)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        ivReservation.setOnClickListener {
            val reserve = Intent(this, Reservation::class.java)
            startActivity(reserve)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        receipts.setOnClickListener {
            val resibo = Intent(this, ReceiptsForAll::class.java)
            startActivity(resibo)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        trackOrder.setOnClickListener {
            val order = Intent(this, Track_Order::class.java)
            startActivity(order)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        // Highlight Home button in brown by default on HomePage
        lastClickedButton = navHome
        navHome.setImageResource(R.drawable.homes_brown)

        //NAVIGATION
        setupNavigation(navHome, "Home", R.drawable.homes, R.drawable.homes_brown)
        setupNavigation(navCart, "Cart", R.drawable.menu, R.drawable.menu_brown)
        setupNavigation(navFavorite, "Favorite", R.drawable.fav, R.drawable.fav_brown)
        setupNavigation(navBag, "Notification", R.drawable.notif, R.drawable.notif_brown)
        setupNavigation(navNotif, "Me", R.drawable.me, R.drawable.me_brown)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerViewCoffees = findViewById(R.id.recyclerViewCoffees)
        recyclerViewCoffees.layoutManager = GridLayoutManager(this,2)

        fetchCoffees()
        // Set Refresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            search.text.clear()
            fetchCoffees()
        }
    }

    private fun fetchCoffees() {
        swipeRefreshLayout.isRefreshing = true

        RetrofitClient.instance.getCoffees().enqueue(object : Callback<List<Coffee>> {
            override fun onResponse(call: Call<List<Coffee>>, response: Response<List<Coffee>>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val coffees = response.body() ?: emptyList()
                    coffeeAdapter = CoffeeAdapter(coffees)
                    recyclerViewCoffees.adapter = coffeeAdapter
                } else {
                    Log.e("MainActivity", "API call failed: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<Coffee>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                t.printStackTrace()
            }
        })
    }

    private fun getSavedImageUri(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("saved_image_uri", null)
    }



    private fun searchCoffees(query: String) {
        RetrofitClient.instance.searchCoffees(query).enqueue(object : Callback<List<Coffee>> {
            override fun onResponse(call: Call<List<Coffee>>, response: Response<List<Coffee>>) {
                if (response.isSuccessful) {
                    val coffees = response.body() ?: emptyList()
                    coffeeAdapter = CoffeeAdapter(coffees)
                    recyclerViewCoffees.adapter = coffeeAdapter
                } else {
                    Log.e("MainActivity", "Search API failed: ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<List<Coffee>>, t: Throwable) {
                Log.e("MainActivity", "Search API call failed", t)
            }
        })
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Prevent memory leaks
    }
}