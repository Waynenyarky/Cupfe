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

class favoriteNav : AppCompatActivity() {
    private var lastClickedButton: ImageView? = null // Track the last clicked button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var ivCheck1: ImageView
    private lateinit var ivCheck2: ImageView
    private lateinit var ivCheck3: ImageView
    private lateinit var ivCheck4: ImageView
    private lateinit var ivCheck5: ImageView
    private lateinit var ivCheck6: ImageView
    private lateinit var ivOverallCheck: ImageView

    private var checkboxesState = mutableListOf(false, false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_nav)

        // Get references to navigation buttons
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val heart1 = findViewById<ImageView>(R.id.heart1)
        val heart2 = findViewById<ImageView>(R.id.heart2)
        val heart3 = findViewById<ImageView>(R.id.heart3)
        val heart4 = findViewById<ImageView>(R.id.heart4)
        val heart5 = findViewById<ImageView>(R.id.heart5)
        val heart6 = findViewById<ImageView>(R.id.heart6)
        val ivCart = findViewById<ImageView>(R.id.ivCart)
        ivCheck1 = findViewById(R.id.ivCheck1)
        ivCheck2 = findViewById(R.id.ivCheck2)
        ivCheck3 = findViewById(R.id.ivCheck3)
        ivCheck4 = findViewById(R.id.ivCheck4)
        ivCheck5 = findViewById(R.id.ivCheck5)
        ivCheck6 = findViewById(R.id.ivCheck6)
        ivOverallCheck = findViewById(R.id.ivOverallCheck)


        // Set up click listeners for hearts
        heart1.setOnClickListener { showUnfavoriteDialog(1) }
        heart2.setOnClickListener { showUnfavoriteDialog(2) }
        heart3.setOnClickListener { showUnfavoriteDialog(3) }
        heart4.setOnClickListener { showUnfavoriteDialog(4) }
        heart5.setOnClickListener { showUnfavoriteDialog(5) }
        heart6.setOnClickListener { showUnfavoriteDialog(6) }

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



        // Set up click listeners for individual checkboxes
        ivCheck1.setOnClickListener { toggleCheckbox(0, ivCheck1) }
        ivCheck2.setOnClickListener { toggleCheckbox(1, ivCheck2) }
        ivCheck3.setOnClickListener { toggleCheckbox(2, ivCheck3) }
        ivCheck4.setOnClickListener { toggleCheckbox(3, ivCheck4) }
        ivCheck5.setOnClickListener { toggleCheckbox(4, ivCheck5) }
        ivCheck6.setOnClickListener { toggleCheckbox(5, ivCheck6) }


        ivOverallCheck.setOnClickListener { toggleOverallCheckbox() }



        sharedPreferences = getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)
        loadFavorites()
        // Ensure overall check state is updated correctly
        saveCheckboxState()
        updateOverallCheckState()
        // Call updateTotalPrice initially

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



    private fun toggleCheckbox(index: Int, checkbox: ImageView) {
        // Toggle the state of the specific checkbox
        checkboxesState[index] = !checkboxesState[index]

        // Update the checkbox image using setCheckboxState function
        setCheckboxState(index, checkboxesState[index], checkbox)

        // Save the state of checkboxes
        saveCheckboxState()

        // Calculate the overall price based on checked items
        var overallPrice = 0
        if (checkboxesState[0]) overallPrice += sharedPreferences.getInt("totalPrice", 0)
        if (checkboxesState[1]) overallPrice += sharedPreferences.getInt("totalPrice2", 0)
        if (checkboxesState[2]) overallPrice += sharedPreferences.getInt("totalPrice3", 0)
        if (checkboxesState[3]) overallPrice += sharedPreferences.getInt("totalPrice4", 0)
        if (checkboxesState[4]) overallPrice += sharedPreferences.getInt("totalPrice5", 0)
        if (checkboxesState[5]) overallPrice += sharedPreferences.getInt("totalPrice6", 0)

        // Update the overall price TextView
        findViewById<TextView>(R.id.tvOverallPrice).text = "₱$overallPrice.00"

        // Ensure overall checkbox updates correctly
        updateOverallCheckState()
    }


    private fun toggleOverallCheckbox() {
        // Determine the new state: If all are checked, uncheck all; otherwise, check all
        val newState = !checkboxesState.all { it }

        // Update all checkboxes
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

        // Update the overall checkbox
        ivOverallCheck.setImageResource(if (newState) R.drawable.circle_check else R.drawable.circle_non_check)

        // Save state
        saveCheckboxState()

        // Recompute the overall price after updating checkboxes
        var overallPrice = 0
        if (checkboxesState[0]) overallPrice += sharedPreferences.getInt("totalPrice", 0)
        if (checkboxesState[1]) overallPrice += sharedPreferences.getInt("totalPrice2", 0)
        if (checkboxesState[2]) overallPrice += sharedPreferences.getInt("totalPrice3", 0)
        if (checkboxesState[3]) overallPrice += sharedPreferences.getInt("totalPrice4", 0)
        if (checkboxesState[4]) overallPrice += sharedPreferences.getInt("totalPrice5", 0)
        if (checkboxesState[5]) overallPrice += sharedPreferences.getInt("totalPrice6", 0)

        // Update the overall price TextView
        findViewById<TextView>(R.id.tvOverallPrice).text = "₱$overallPrice.00"
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
        sharedPreferences.edit().putBoolean("ivOverallCheck", allChecked).apply()
    }

    private fun saveCheckboxState() {
        val editor = sharedPreferences.edit()
        checkboxesState.forEachIndexed { index, isChecked ->
            editor.putBoolean("isChecked$index", isChecked)
        }
        editor.putBoolean("isOverallChecked", checkboxesState.all { it })

        // Save the overall price in SharedPreferences
        var overallPrice = 0
        if (checkboxesState[0]) overallPrice += sharedPreferences.getInt("totalPrice", 0)
        if (checkboxesState[1]) overallPrice += sharedPreferences.getInt("totalPrice2", 0)
        if (checkboxesState[2]) overallPrice += sharedPreferences.getInt("totalPrice3", 0)
        if (checkboxesState[3]) overallPrice += sharedPreferences.getInt("totalPrice4", 0)
        if (checkboxesState[4]) overallPrice += sharedPreferences.getInt("totalPrice5", 0)
        if (checkboxesState[5]) overallPrice += sharedPreferences.getInt("totalPrice6", 0)

        editor.putInt("overallPrice", overallPrice)  // Save overall price
        editor.apply()
    }

    private fun showUnfavoriteDialog(heartId: Int) {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.favorite_alert_dialog, null)

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
            unfavoriteItem(heartId)
            alert.dismiss()
        }

        btnNo.setOnClickListener {
            alert.dismiss()
        }

        alert.show()
    }

    // Function to handle unfavoriting the item
    private fun unfavoriteItem(heartId: Int) {
        val editor = sharedPreferences.edit()

        // Unfavorite logic for each heart icon, using heartId to determine which one
        when (heartId) {
            1 -> {
                editor.remove("coffeeName")
                editor.remove("selectedSize")
                editor.remove("quantity")
                editor.remove("totalPrice")
                findViewById<LinearLayout>(R.id.favFoodLayout).visibility = View.GONE
            }
            2 -> {
                editor.remove("coffeeName2")
                editor.remove("selectedSize2")
                editor.remove("quantity2")
                editor.remove("totalPrice2")
                findViewById<LinearLayout>(R.id.favFoodLayout2).visibility = View.GONE
            }
            3 -> {
                editor.remove("coffeeName3")
                editor.remove("selectedSize3")
                editor.remove("quantity3")
                editor.remove("totalPrice3")
                findViewById<LinearLayout>(R.id.favFoodLayout3).visibility = View.GONE
            }
            4 -> {
                editor.remove("coffeeName4")
                editor.remove("selectedSize4")
                editor.remove("quantity4")
                editor.remove("totalPrice4")
                findViewById<LinearLayout>(R.id.favFoodLayout4).visibility = View.GONE
            }
            5 -> {
                editor.remove("coffeeName5")
                editor.remove("selectedSize5")
                editor.remove("quantity5")
                editor.remove("totalPrice5")
                findViewById<LinearLayout>(R.id.favFoodLayout5).visibility = View.GONE
            }
            6 -> {
                editor.remove("coffeeName6")
                editor.remove("selectedSize6")
                editor.remove("quantity6")
                editor.remove("totalPrice6")
                findViewById<LinearLayout>(R.id.favFoodLayout6).visibility = View.GONE
            }
        }

        editor.apply()  // Commit changes to SharedPreferences
        loadFavorites()  // Reload favorites after removal
    }

    private fun loadFavorites() {

        checkboxesState.forEachIndexed { index, _ ->
            checkboxesState[index] = sharedPreferences.getBoolean("isChecked$index", false)
        }


        val coffeeName = sharedPreferences.getString("coffeeName", "N/A")
        val selectedSize = sharedPreferences.getString("selectedSize", "N/A")
        val quantity = sharedPreferences.getInt("quantity", 0)
        val totalPrice = sharedPreferences.getInt("totalPrice", 0)

        val coffeeName2 = sharedPreferences.getString("coffeeName2", "N/A")
        val selectedSize2 = sharedPreferences.getString("selectedSize2", "N/A")
        val quantity2 = sharedPreferences.getInt("quantity2", 0)
        val totalPrice2 = sharedPreferences.getInt("totalPrice2", 0)

        val coffeeName3 = sharedPreferences.getString("coffeeName3", "N/A")
        val selectedSize3 = sharedPreferences.getString("selectedSize3", "N/A")
        val quantity3 = sharedPreferences.getInt("quantity3", 0)
        val totalPrice3 = sharedPreferences.getInt("totalPrice3", 0)

        val coffeeName4 = sharedPreferences.getString("coffeeName4", "N/A")
        val selectedSize4 = sharedPreferences.getString("selectedSize4", "N/A")
        val quantity4 = sharedPreferences.getInt("quantity4", 0)
        val totalPrice4 = sharedPreferences.getInt("totalPrice4", 0)

        val coffeeName5 = sharedPreferences.getString("coffeeName5", "N/A")
        val selectedSize5 = sharedPreferences.getString("selectedSize5", "N/A")
        val quantity5 = sharedPreferences.getInt("quantity5", 0)
        val totalPrice5 = sharedPreferences.getInt("totalPrice5", 0)

        val coffeeName6 = sharedPreferences.getString("coffeeName6", "N/A")
        val selectedSize6 = sharedPreferences.getString("selectedSize6", "N/A")
        val quantity6 = sharedPreferences.getInt("quantity6", 0)
        val totalPrice6 = sharedPreferences.getInt("totalPrice6", 0)

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

        // Check if the first favorite coffee exists 1
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
            findViewById<TextView>(R.id.tvFavQuantityValue).text = "Quantity: $quantity"
            findViewById<TextView>(R.id.tvFavPriceValue).text = "₱$totalPrice.00"
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
            findViewById<TextView>(R.id.tvFavQuantityValue2).text = "Quantity: $quantity2"
            findViewById<TextView>(R.id.tvFavPriceValue2).text = "₱$totalPrice2.00"
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
            findViewById<TextView>(R.id.tvFavQuantityValue3).text = "Quantity: $quantity3"
            findViewById<TextView>(R.id.tvFavPriceValue3).text = "₱$totalPrice3.00"
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
            findViewById<TextView>(R.id.tvFavQuantityValue4).text = "Quantity: $quantity4"
            findViewById<TextView>(R.id.tvFavPriceValue4).text = "₱$totalPrice4.00"
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
            findViewById<TextView>(R.id.tvFavQuantityValue5).text = "Quantity: $quantity5"
            findViewById<TextView>(R.id.tvFavPriceValue5).text = "₱$totalPrice5.00"
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
            findViewById<TextView>(R.id.tvFavQuantityValue6).text = "Quantity: $quantity6"
            findViewById<TextView>(R.id.tvFavPriceValue6).text = "₱$totalPrice6.00"
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
                // Navigate to Favorite screen
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
        val dialogView = layoutInflater.inflate(R.layout.dialog_no_favorites, null)
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