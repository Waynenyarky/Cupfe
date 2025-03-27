package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tamayo_aaron_b.cupfe_expresso.menu.Coffee
import com.tamayo_aaron_b.cupfe_expresso.menu.CoffeeAdapter
import com.tamayo_aaron_b.cupfe_expresso.menu.FoodAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class food_menu : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    @SuppressLint("ClickableViewAccessibility")

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerViewMenuCoffee: RecyclerView
    private lateinit var recyclerViewAllCoffee: RecyclerView
    private lateinit var recyclerViewFoods: RecyclerView


    private lateinit var menuCoffeeAdapter: FoodAdapter
    private lateinit var allCoffeeAdapter: FoodAdapter
    private lateinit var foodsAdapter: FoodAdapter

    @SuppressLint("ClickableViewAccessibility")
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
        val popular1 = findViewById<ImageView>(R.id.popular1)
        val foodss1 = findViewById<ImageView>(R.id.foodss1)
        val foodss2 = findViewById<ImageView>(R.id.foodss2)
        val floatingText = findViewById<LinearLayout>(R.id.floatingText)
        val scrollView = findViewById<NestedScrollView>(R.id.scrollView)
        val handler = android.os.Handler()
        val ivCart = findViewById<ImageView>(R.id.ivCart)

        ivCart.setOnClickListener{
            val cart = Intent(this, AddToCart::class.java)
            startActivity(cart)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        scrollView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    floatingText.animate()
                        .alpha(0f)  // Fade out
                        .setDuration(300) // Animation duration in milliseconds
                        .withEndAction {
                            floatingText.visibility = View.INVISIBLE
                        }
                        .start()

                    handler.removeCallbacksAndMessages(null) // Remove existing callbacks
                }

                MotionEvent.ACTION_UP -> {
                    handler.postDelayed({
                        floatingText.visibility = View.VISIBLE
                        floatingText.animate()
                            .alpha(1f)  // Fade in
                            .setDuration(500) // Animation duration
                            .start()
                    }, 2500)
                }
            }
            false
        }


        floatingText.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                floatingText.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val parentView = floatingText.parent as View
                val parentWidth = parentView.width
                val parentHeight = parentView.height

                val floatWidth = floatingText.width
                val floatHeight = floatingText.height

                floatingText.x = (parentWidth - floatWidth) / 3f
                floatingText.y = (parentHeight - floatHeight) / 2 + 850f

                floatingText.visibility = View.VISIBLE
            }
        })



        ivBack.setOnClickListener{
            val back = Intent(this, Main_Home_Page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }


        foodss1.setOnClickListener{
            showPopup2()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        foodss2.setOnClickListener{
            showPopup3()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }


        popular1.setOnClickListener {
            val food1 = Intent(this, details_food1::class.java)
            startActivity(food1)
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



        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerViewMenuCoffee = findViewById(R.id.menuCoffee)
        recyclerViewMenuCoffee.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewAllCoffee = findViewById(R.id.AllCoffee)
        recyclerViewAllCoffee.layoutManager = GridLayoutManager(this, 3)

        recyclerViewFoods = findViewById(R.id.recyclerViewFoods)
        recyclerViewFoods.layoutManager = GridLayoutManager(this, 3)

        fetchCoffees()
        fetchFoods()
        fetchFoodss()

        // Set Refresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            fetchCoffees()
            fetchFoods()
            fetchFoodss()
        }

    }

    private fun fetchCoffees() {
        swipeRefreshLayout.isRefreshing = true
        RetrofitClient.instance.getCoffees().enqueue(object : Callback<List<Coffee>> {
            override fun onResponse(call: Call<List<Coffee>>, response: Response<List<Coffee>>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val coffees = response.body() ?: emptyList()
                    menuCoffeeAdapter = FoodAdapter(coffees)
                    recyclerViewMenuCoffee.adapter = menuCoffeeAdapter
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

    private fun fetchFoods() {
        swipeRefreshLayout.isRefreshing = true
        RetrofitClient.instance.getAllCoffees("Coffee").enqueue(object : Callback<List<Coffee>> {
            override fun onResponse(call: Call<List<Coffee>>, response: Response<List<Coffee>>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val coffeeList = response.body() ?: emptyList()
                    allCoffeeAdapter = FoodAdapter(coffeeList)
                    recyclerViewAllCoffee.adapter = allCoffeeAdapter
                } else {
                    Toast.makeText(this@food_menu, "Failed to load coffee items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Coffee>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@food_menu, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchFoodss() {
        swipeRefreshLayout.isRefreshing = true
        RetrofitClient.instance.getAllFoods("Food").enqueue(object : Callback<List<Coffee>> {
            override fun onResponse(call: Call<List<Coffee>>, response: Response<List<Coffee>>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val coffeeList = response.body() ?: emptyList()
                    foodsAdapter = FoodAdapter(coffeeList)
                    recyclerViewFoods.adapter = foodsAdapter
                } else {
                    Toast.makeText(this@food_menu, "Failed to load coffee items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Coffee>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@food_menu, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Find the close button inside the popup
        val close = dialog.findViewById<ImageView>(R.id.close)

        // Close the dialog when clicking the close button
        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



    private fun showPopup2() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.athome2)

        // Prevent dismissing the dialog when clicking outside
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // Make the background transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(), // 85% of screen width
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Find and set click listener for "OK" button
        val btnOk = dialog.findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showPopup3() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.athome2)

        // Prevent dismissing the dialog when clicking outside
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // Make the background transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(), // 85% of screen width
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Find and set click listener for "OK" button
        val btnOk = dialog.findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener {
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
                val notification = Intent(this, Notifications::class.java)
                startActivity(notification)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)

            }
            "Profile" -> {
                val profile = Intent(this, Profile::class.java)
                startActivity(profile)
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