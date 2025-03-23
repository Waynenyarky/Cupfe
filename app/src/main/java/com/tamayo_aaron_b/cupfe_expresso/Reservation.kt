package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Reservation : AppCompatActivity() {
    private lateinit var timeLayout: LinearLayout
    private lateinit var timeSelect: LinearLayout
    private lateinit var timeText : TextView
    private lateinit var selectedTimeText: TextView
    private var lastSelectedTime: TextView? = null
    private var lastClickedButton: ImageView? = null // Track the last clicked button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation)

        val calendarLayout = findViewById<LinearLayout>(R.id.Calendar)
        val textViewDate = calendarLayout.findViewById<TextView>(R.id.textViewDate)
        timeLayout = findViewById(R.id.Time)
        timeText = findViewById(R.id.timeText)
        timeSelect = findViewById(R.id.timeSelect)
        selectedTimeText = findViewById(R.id.tvSelectedTime)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val FindSlot = findViewById<TextView>(R.id.FindSlot)
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)
        val packageA = findViewById<ImageView>(R.id.packageA)
        val packageB = findViewById<ImageView>(R.id.packageB)
        val packageC = findViewById<ImageView>(R.id.packageC)
        val tvPackageName = findViewById<TextView>(R.id.tvPackageName)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvPerson = findViewById<TextView>(R.id.tvPerson)
        val tvIncludes = findViewById<TextView>(R.id.tvIncludes)
        val summary = findViewById<LinearLayout>(R.id.Summary)
        val tvPackageNameB = findViewById<TextView>(R.id.tvPackageNameB)
        val tvPriceB = findViewById<TextView>(R.id.tvPriceB)
        val tvPersonB = findViewById<TextView>(R.id.tvPersonB)
        val tvIncludesB = findViewById<TextView>(R.id.tvIncludesB)
        val bundle1 = findViewById<LinearLayout>(R.id.bundle1)
        val bundle2 = findViewById<LinearLayout>(R.id.bundle2)
        val seats1 = findViewById<LinearLayout>(R.id.seats1)
        val seats2 = findViewById<LinearLayout>(R.id.seats2)
        val price1 = findViewById<LinearLayout>(R.id.price1)
        val price2 = findViewById<LinearLayout>(R.id.price2)

        btnBack.setOnClickListener {
            val intent = Intent(this, Main_Home_Page::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        // Retrieve data from intent
        val packageName = intent.getStringExtra("packageName")
        val price = intent.getStringExtra("price")
        val person = intent.getStringExtra("person")
        val includes = intent.getStringExtra("includes")
        val packageNameB = intent.getStringExtra("packageNameB")
        val priceB = intent.getStringExtra("priceB")
        val personB = intent.getStringExtra("personB")
        val includesB = intent.getStringExtra("includesB")

        if (packageName.isNullOrEmpty() && price.isNullOrEmpty() && person.isNullOrEmpty() && includes.isNullOrEmpty() &&
            packageNameB.isNullOrEmpty() && priceB.isNullOrEmpty() && personB.isNullOrEmpty() && includesB.isNullOrEmpty()) {
            summary.visibility = View.GONE
        } else {
            tvPackageName.text = packageName ?: ""
            tvPrice.text = price ?: ""
            tvPerson.text = person ?: ""
            tvIncludes.text = includes ?: ""
            tvPackageNameB.text = packageNameB ?: ""
            tvPriceB.text = priceB ?: ""
            tvPersonB.text = personB ?: ""
            tvIncludesB.text = includesB ?: ""
        }

        if (packageName.isNullOrEmpty() && price.isNullOrEmpty() && person.isNullOrEmpty() && includes.isNullOrEmpty() ) {
            bundle1.visibility = View.GONE
            seats1.visibility = View.GONE
            price1.visibility = View.GONE
            tvIncludes.visibility = View.GONE
        } else {
            tvPackageName.text = packageName ?: ""
            tvPrice.text = price ?: ""
            tvPerson.text = person ?: ""
            tvIncludes.text = includes ?: ""
        }

        if (packageNameB.isNullOrEmpty() && priceB.isNullOrEmpty() && personB.isNullOrEmpty() && includesB.isNullOrEmpty()) {
            bundle2.visibility = View.GONE
            seats2.visibility = View.GONE
            price2.visibility = View.GONE
            tvIncludesB.visibility = View.GONE
        } else {
            tvPackageNameB.text = packageNameB ?: ""
            tvPriceB.text = priceB ?: ""
            tvPersonB.text = personB ?: ""
            tvIncludesB.text = includesB ?: ""
        }

        tvPackageName.text = packageName
        tvPrice.text = price
        tvPerson.text = person
        tvIncludes.text = includes
        tvPackageNameB.text = packageNameB
        tvPriceB.text = priceB
        tvPersonB.text = personB
        tvIncludesB.text = includesB


        packageA.setOnClickListener{
            val a = Intent(this, PackageA::class.java)
            startActivity(a)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        packageB.setOnClickListener{
            val b = Intent(this, PackageB::class.java)
            startActivity(b)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        packageC.setOnClickListener{
            val c = Intent(this, PackageC::class.java)
            startActivity(c)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }


        calendarLayout.setOnClickListener {

            if (summary.visibility == View.GONE) {
                timeText.text = "Please select Package A or B"
                timeText.visibility = View.VISIBLE
                timeText.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this, R.style.BrownDatePickerDialog,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                    // Check if the selected date is in the past
                    if (selectedCalendar.before(calendar)) {
                        Toast.makeText(this, "Select a Date from today onwards", Toast.LENGTH_SHORT).show()
                    } else {
                        // Store date in numeric format (YYYY-MM-DD) in textViewDate
                        val numericDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay)
                        textViewDate.text = numericDate
                    }
                },
                year, month, day
            )
            datePickerDialog.datePicker.minDate = calendar.timeInMillis // Prevent past date selection
            datePickerDialog.show()
        }


        // TIME SELECTION
        timeLayout.setOnClickListener {

            if (summary.visibility == View.GONE) {
                timeText.text = "Please select Package A or B"
                timeText.visibility = View.VISIBLE
                timeText.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }



            // Check if there is an error message
            val isVisible = timeText.visibility == View.VISIBLE
            timeText.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
            timeSelect.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
        }

        for (i in 0 until timeSelect.childCount) {
            val column = timeSelect.getChildAt(i)
            if (column is LinearLayout) {
                for (j in 0 until column.childCount) {
                    val timeOption = column.getChildAt(j) as TextView
                    timeOption.setOnClickListener {
                        lastSelectedTime?.apply {
                            setBackgroundResource(R.drawable.reservation_bg_time)
                            setTextColor(resources.getColor(R.color.black, theme))
                        }

                        timeOption.setBackgroundResource(R.drawable.reservation_time_brown)
                        timeOption.setTextColor(resources.getColor(R.color.white, theme))
                        selectedTimeText.text = timeOption.text.toString()
                        lastSelectedTime = timeOption
                    }
                }
            }
        }
        // NAVIGATE TO Reservation2
        FindSlot.setOnClickListener {
            // Get the selected date, time, and number of people
            val selectedDate = textViewDate.text.toString().trim()
            val selectedTime = selectedTimeText.text.toString().trim()

            // Validate Date (check if it's in the format YYYY-MM-DD)
            val dateRegex = Regex("\\d{4}/\\d{2}/\\d{2}")
            if (!dateRegex.matches(selectedDate)) {
                timeText.text = "Please select a Date and Time"
                timeText.visibility = View.VISIBLE
                timeText.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }

            // Validate Time (check if any time is selected)
            val timeRegex = Regex("^\\d{1,2}:\\d{2}$") // Matches HH:mm or H:mm
            if (!timeRegex.matches(selectedTime)) {
                timeText.text = "Please select a Date and Time"
                timeText.visibility = View.VISIBLE
                timeText.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }


            // Determine AM or PM
            val timeParts = selectedTime.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()

            var period = "" // Set dynamically based on the conditions

            if ((hour == 11 && minute in 30..55) || hour < 11) {
                period = "AM"
            } else if ((hour == 12 && minute in 0..59) || (hour == 13 && minute == 0)) {
                period = "PM"
            }

            // Format the time
            val formattedTime = String.format("%02d:%02d", hour, minute)

            // Format the date
            val dateParts = selectedDate.split("/")
            val day = dateParts[2]
            val month = getMonthName(dateParts[1].toInt() - 1) // Convert month number to string
            val year = dateParts[0]
            val formattedDate = "$day $month $year | $formattedTime $period" // Include AM/PM


            val intent = Intent(this, Reservation2::class.java).apply {
                putExtra("DATE_TIME", formattedDate)
                putExtra("packageName", packageName)
                putExtra("price", price)
                putExtra("person", person)
                putExtra("packageNameB", packageNameB)
                putExtra("priceB", priceB)
                putExtra("personB", personB)
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
        }

        //NAVIGATION
        // Add click listeners with animations and image change
        setupNavigation(navHome, "Home", R.drawable.homes, R.drawable.homes_brown)
        setupNavigation(navCart, "Cart", R.drawable.menu, R.drawable.menu_brown)
        setupNavigation(navFavorite, "Favorite", R.drawable.fav, R.drawable.fav_brown)
        setupNavigation(navBag, "Notification", R.drawable.notif, R.drawable.notif_brown)
        setupNavigation(navNotif, "Me", R.drawable.me, R.drawable.me_brown)
    }

    private fun getMonthName(month: Int): String {
        return arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )[month]
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
            "Me" -> {
                val me = Intent(this, Profile::class.java)
                startActivity(me)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)


            }
        }
    }
    private fun getDefaultImage(buttonId: Int): Int {
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
