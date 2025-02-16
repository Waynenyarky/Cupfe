package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Reservation : AppCompatActivity() {

    private lateinit var timeLayout: LinearLayout
    private lateinit var timeSelect: LinearLayout
    private lateinit var selectedTimeText: TextView
    private var lastSelectedTime: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation)

        val calendarLayout = findViewById<LinearLayout>(R.id.Calendar)
        val textViewDate = calendarLayout.findViewById<TextView>(R.id.textViewDate)
        timeLayout = findViewById(R.id.Time)
        timeSelect = findViewById(R.id.timeSelect)
        selectedTimeText = findViewById(R.id.tvSelectedTime)
        val peopleLayout = findViewById<LinearLayout>(R.id.People)
        val tvPeople = findViewById<TextView>(R.id.tvPeople)




        //CALENDAR
        calendarLayout.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.BrownDatePickerDialog,  // Apply custom theme
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    textViewDate.text = formattedDate
                },
                year, month, day
            )

            datePickerDialog.show()
        }



        //TIME
        timeLayout.setOnClickListener {
            timeSelect.visibility = if (timeSelect.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        }
        // Iterate through all time options
        for (i in 0 until timeSelect.childCount) {
            val column = timeSelect.getChildAt(i)
            if (column is LinearLayout) {
                for (j in 0 until column.childCount) {
                    val timeOption = column.getChildAt(j) as TextView
                    timeOption.setOnClickListener {
                        // Reset previous selection
                        lastSelectedTime?.apply {
                            setBackgroundResource(R.drawable.reservation_bg_time)
                            setTextColor(resources.getColor(R.color.black, theme))
                        }

                        // Set new selection
                        timeOption.setBackgroundResource(R.drawable.reservation_time_brown)  // Highlight selected option
                        timeOption.setTextColor(resources.getColor(R.color.white, theme)) // Change text color for contrast

                        // Update selected time in main view
                        selectedTimeText.text = timeOption.text.toString()

                        // Store the last selected time
                        lastSelectedTime = timeOption
                    }
                }
            }
        }
        peopleLayout.setOnClickListener {
            showPeopleSelectionDialog(tvPeople)
        }
    }
    //People
    private fun showPeopleSelectionDialog(tvPeople: TextView) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.people_selection)

        val etPeopleCount = dialog.findViewById<EditText>(R.id.etPeopleCount)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<TextView>(R.id.btnConfirm)

        // Prevent dismissing the dialog when clicking outside
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // Make the background transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            val enteredText = etPeopleCount.text.toString().trim()

            if (enteredText.isNotEmpty()) {
                val enteredNumber = enteredText.toIntOrNull()

                if (enteredNumber != null && enteredNumber in 1..10) {
                    tvPeople.text = enteredText
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


}
