package com.tamayo_aaron_b.cupfe_expresso

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
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


        btnBack.setOnClickListener{
            finish()
        }


        calendarLayout.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this, R.style.BrownDatePickerDialog,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Store date in numeric format (YYYY-MM-DD) in textViewDate
                    val numericDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    textViewDate.text = numericDate // Display numeric format in Reservation
                },
                year, month, day
            )
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
            val dateRegex = Regex("\\d{4}-\\d{2}-\\d{2}")
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

            // If all inputs are valid, proceed to the next screen
            val selectedDateTime = "$selectedDate | $selectedTime"
            val selectedPeopleText = "$selectedPeople Guests"

            val intent = Intent(this, Reservation2::class.java).apply {
                putExtra("DATE_TIME", selectedDateTime)
                putExtra("PEOPLE", selectedPeopleText)
            }
            startActivity(intent)
        }



    }

    private fun showPeopleSelectionDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.people_selection)

        val etPeopleCount = dialog.findViewById<EditText>(R.id.etPeopleCount)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<TextView>(R.id.btnConfirm)

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnConfirm.setOnClickListener {
            val enteredText = etPeopleCount.text.toString().trim()

            if (enteredText.isNotEmpty()) {
                val enteredNumber = enteredText.toIntOrNull()

                if (enteredNumber != null && enteredNumber in 1..10) {
                    tvPeople.text = enteredNumber.toString()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Please enter a number between 1 and 10 only", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun getMonthName(month: Int): String {
        return arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )[month]
    }


}
