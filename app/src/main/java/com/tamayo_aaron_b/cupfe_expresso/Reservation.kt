package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.EditText
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
    private lateinit var tvPeople : TextView
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
        val peopleLayout = findViewById<LinearLayout>(R.id.People)
        tvPeople = findViewById(R.id.tvPeople)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val FindSlot = findViewById<TextView>(R.id.FindSlot)
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navCart = findViewById<ImageView>(R.id.nav_cart)
        val navFavorite = findViewById<ImageView>(R.id.nav_favorite)
        val navBag = findViewById<ImageView>(R.id.nav_bag)
        val navNotif = findViewById<ImageView>(R.id.nav_notif)


        btnBack.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }


        calendarLayout.setOnClickListener {
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
            // Check if there is an error message
            if (timeText.visibility == View.VISIBLE && timeText.text == "Please select a Date, Time and People") {
                // Hide the error message and show the time selection
                timeText.visibility = View.INVISIBLE
                timeSelect.visibility = View.VISIBLE
            } else {
                // Toggle visibility normally if there is no error message
                val isVisible = timeText.visibility == View.VISIBLE
                timeText.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
                timeSelect.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
            }
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

        peopleLayout.setOnClickListener {
            showPeopleSelectionDialog()
        }

        // NAVIGATE TO Reservation2
        FindSlot.setOnClickListener {
            // Get the selected date, time, and number of people
            val selectedDate = textViewDate.text.toString().trim()
            val selectedTime = selectedTimeText.text.toString().trim()
            val selectedPeople = tvPeople.text.toString().trim()

            // Validate Date (check if it's in the format YYYY-MM-DD)
            val dateRegex = Regex("\\d{4}/\\d{2}/\\d{2}")
            if (!dateRegex.matches(selectedDate)) {
                timeText.text = "Please select a Date, Time and People"
                timeText.visibility = View.VISIBLE
                timeText.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }

            // Validate Time (check if any time is selected)
            if (selectedTime.isEmpty()) {
                timeText.text = "Please select a Date, Time and People"
                timeText.visibility = View.VISIBLE
                timeText.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }

            // Validate People (check if the user entered a valid number)
            val peopleCount = selectedPeople.toIntOrNull()
            if (peopleCount == null || peopleCount < 1) {
                timeText.text = "Please select a Date, Time and People"
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

            // If all inputs are valid, proceed to the next screen
            val selectedPeopleText = "$selectedPeople Guests"

            val price = selectedTables * 40
            val table = "$selectedTables Indoor Table"
            val intent = Intent(this, Reservation2::class.java).apply {
                putExtra("DATE_TIME", formattedDate)  // Send formatted date and time
                putExtra("PEOPLE", selectedPeopleText)
                putExtra("PRICE", price)
                putExtra("TABLE", table)
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

    private var selectedTables = 0
    private var selectedPeople = 0

    private fun showPeopleSelectionDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.people_selection)

        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<TextView>(R.id.btnConfirm)
        val tvCalculatedPrice = dialog.findViewById<TextView>(R.id.tvCalculatedPrice)

        val ivMinus1 = dialog.findViewById<ImageView>(R.id.ivMinus1)
        val tvNumber1 = dialog.findViewById<TextView>(R.id.tvNumber1)
        val ivAdd1 = dialog.findViewById<ImageView>(R.id.ivAdd1)

        val ivMinus2 = dialog.findViewById<ImageView>(R.id.ivMinus2)
        val tvNumber2 = dialog.findViewById<TextView>(R.id.tvNumber2)
        val ivAdd2 = dialog.findViewById<ImageView>(R.id.ivAdd2)

        val etTableCount = dialog.findViewById<EditText>(R.id.etTableCount)
        val etPeopleCount = dialog.findViewById<EditText>(R.id.etPeopleCount)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var currentTables = selectedTables
        var currentPeople = selectedPeople

        // Pricing logic
        fun calculatePrice(tables: Int): Int {
            return tables * 40
        }

        // Max people allowed per table logic
        fun maxPeopleAllowed(): Int {
            return currentTables * 6
        }

        // Enable/disable `tvNumber2` based on table selection
        fun checkTableSelection() {
            if (currentTables > 0) {
                tvNumber2.isEnabled = true
                tvNumber2.setTextColor(Color.BLACK) // Enable and set to normal color
            } else {
                tvNumber2.isEnabled = false
                tvNumber2.setTextColor(Color.GRAY) // Disable and make it gray
                etPeopleCount.visibility = View.GONE // Hide input if disabled
            }
        }

        // Update UI
        fun updateUI() {
            tvNumber1.text = currentTables.toString()
            tvNumber2.text = currentPeople.toString()
            tvCalculatedPrice.text = "₱${calculatePrice(currentTables)}.00"
            checkTableSelection() // Ensure correct state of `tvNumber2`
        }

        // Handle Table Increment
        ivAdd1.setOnClickListener {
            if (currentTables < 10) {
                currentTables++
                if (currentPeople > maxPeopleAllowed()) {
                    currentPeople = maxPeopleAllowed() // Adjust people count if needed
                }
                updateUI()
            }
        }

        // Handle Table Decrement
        ivMinus1.setOnClickListener {
            if (currentTables > 1) {
                currentTables--
                if (currentPeople > maxPeopleAllowed()) {
                    currentPeople = maxPeopleAllowed()
                }
                updateUI()
            }
        }

        // Handle People Increment
        ivAdd2.setOnClickListener {
            if (currentTables > 0) { // Ensure at least 1 table is selected
                if (currentPeople < maxPeopleAllowed()) {
                    currentPeople++
                    updateUI()
                }
            } else {
                Toast.makeText(dialog.context, "Please add a table first!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle People Decrement
        ivMinus2.setOnClickListener {
            if (currentPeople > 1) {
                currentPeople--
                updateUI()
            }
        }

        // Handle Table Count Input
        etTableCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val enteredValue = s.toString().toIntOrNull()
                if (enteredValue != null && enteredValue in 1..10) {
                    currentTables = enteredValue
                    if (currentPeople > maxPeopleAllowed()) {
                        currentPeople = maxPeopleAllowed()
                    }
                    updateUI()
                } else {
                    etTableCount.text.clear()
                    Toast.makeText(dialog.context, "Max tables: 10", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Handle People Count Input
        etPeopleCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val enteredValue = s.toString().toIntOrNull()
                if (enteredValue != null && enteredValue in 1..maxPeopleAllowed()) {
                    currentPeople = enteredValue
                    updateUI()
                } else {
                    etPeopleCount.text.clear()
                    Toast.makeText(dialog.context, "Max people: ${maxPeopleAllowed()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Show/hide Table Count input
        tvNumber1.setOnClickListener {
            etTableCount.visibility = if (etTableCount.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Show/hide People Count input only if tables are selected
        tvNumber2.setOnClickListener {
            if (currentTables > 0) {
                etPeopleCount.visibility = if (etPeopleCount.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            } else {
                Toast.makeText(dialog.context, "Please select a table first!", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            val tvPeople = findViewById<TextView>(R.id.tvPeople)

            // Reset to "People" only if both table and person are 0
            if (selectedTables == 0 && selectedPeople == 0) {
                tvPeople.text = "People"
            }

            dialog.dismiss()
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            if (currentTables == 0 || currentPeople == 0) {
                Toast.makeText(dialog.context, "Table and Person can't be 0!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop further execution
            }

            selectedTables = currentTables
            selectedPeople = currentPeople

            val tvPeople = findViewById<TextView>(R.id.tvPeople)
            tvPeople.text = tvNumber2.text
            dialog.dismiss()
        }

        updateUI() // Initialize UI
        dialog.show()
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
