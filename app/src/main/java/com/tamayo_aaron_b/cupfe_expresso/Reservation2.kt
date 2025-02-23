package com.tamayo_aaron_b.cupfe_expresso

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class Reservation2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation2)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tvDateTime = findViewById<TextView>(R.id.tvDateTime)
        val tvPeople = findViewById<TextView>(R.id.tvPeople)
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etcpNumber = findViewById<EditText>(R.id.etcpNumber)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val ReserveNow = findViewById<TextView>(R.id.ReserveNow)
        val checkButton = findViewById<ImageView>(R.id.Check)
        val checkButton2 = findViewById<ImageView>(R.id.Check2)
        val tvPrizeTotal = findViewById<TextView>(R.id.tvPrizeTotal)
        val tvIndoor = findViewById<TextView>(R.id.tvIndoor)

        addValidationWithIcon(etFullName) { isValidName(it) }
        addValidationWithIcon(etcpNumber) { isValidPhoneNumber(it) }
        addValidationWithIcon(etEmail) { isValidEmail(it) }


        checkButton.setOnClickListener {
            Toast.makeText(applicationContext, "Not enough saved Points", Toast.LENGTH_SHORT).show()
        }

        checkButton2.setOnClickListener {
            Toast.makeText(applicationContext, "Not available this offer", Toast.LENGTH_SHORT).show()
        }


        val dateTime = intent.getStringExtra("DATE_TIME") ?: "No Date & Time Selected"
        val people = intent.getStringExtra("PEOPLE") ?: "No Guests Selected"
        val price = intent.getIntExtra("PRICE", 0) // Default to 0 if not found
        val table = intent.getStringExtra("TABLE") ?: "No Table Selected"


        tvIndoor.text = table
        tvPrizeTotal.text = "₱$price.00"

        ReserveNow.setOnClickListener {
            val email = etEmail.text.toString()
            val name = etFullName.text.toString()
            val cellphone = etcpNumber.text.toString()

            // NAME VALIDATION
            if (name.isEmpty()) {
                etFullName.error = "Please complete the text field."
                etFullName.requestFocus()
                return@setOnClickListener
            }
            if (!isValidName(name)) {
                etFullName.error = "Invalid name format."
                etFullName.requestFocus()
                return@setOnClickListener
            }

            // PHONE NUMBER VALIDATION
            if (cellphone.isEmpty()) {
                etcpNumber.error = "Please enter your cellphone number."
                etcpNumber.requestFocus()
                return@setOnClickListener
            }
            if (!isValidPhoneNumber(cellphone)) {
                etcpNumber.error = "Must start with 09 and be 11 digits long."
                etcpNumber.requestFocus()
                return@setOnClickListener
            }

            // EMAIL VALIDATION
            if (email.isEmpty()) {
                etEmail.error = "Please complete the text field."
                etEmail.requestFocus()
                return@setOnClickListener
            }
            if (!isValidEmail(email)) {
                etEmail.error = "Invalid email format."
                etEmail.requestFocus()
                return@setOnClickListener
            }
            confirmationReserve()
        }

        // Prevent Emojis in Inputs
        listOf(etEmail, etFullName, etcpNumber).forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val newText = removeEmojis(it.toString())
                        if (newText != it.toString()) {
                            Toast.makeText(this@Reservation2, "Can't input an Emoji", Toast.LENGTH_SHORT).show()
                            editText.setText(newText)
                            editText.setSelection(newText.length) // Move cursor to the end
                        }
                    }
                }
            })
        }

        // PHONE NUMBER FILTERS
        etcpNumber.filters = arrayOf(InputFilter.LengthFilter(11))

        etcpNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty() && !isValidPhoneNumber(s.toString())) {
                    etcpNumber.error = "Must start with 09 and be 11 digits long"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // NAME FILTER (Only letters and spaces, allowing spaces but not at the start)
        val nameFilter = InputFilter { source, _, _, dest, dstart, _ ->
            if (source.matches(Regex("[a-zA-Z. ]*")) && !(source == " " && (dstart == 0 || dest.isEmpty()))) {
                source
            } else { "" }
        }
        etFullName.filters = arrayOf(nameFilter)

        tvDateTime.text = dateTime
        tvPeople.text = people

        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }
    }

    private fun confirmationReserve(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirmation_in_reservation)

        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<TextView>(R.id.btnConfirm)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnConfirm.setOnClickListener{ dialog.dismiss() }

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

    }


    private fun addValidationWithIcon(editText: EditText, validator: (String) -> Boolean) {
        val checkIcon: Drawable? = ContextCompat.getDrawable(editText.context, R.drawable.ic_check)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }

        val paddingEnd = 50  // Adjust the right padding to fit the icon properly

        editText.setPadding(editText.paddingLeft, editText.paddingTop, paddingEnd, editText.paddingBottom)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                editText.post {
                    if (validator(input)) {
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, checkIcon, null)
                        editText.compoundDrawablePadding = 20 // Adjust padding between text and icon
                        editText.error = null
                    } else {
                        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }








    // PHONE NUMBER VALIDATION (Must start with 09 and be 12 digits)
    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^09\\d{9}$"))
    }


    // NAME VALIDATION (Only letters, spaces, and optional suffixes like II, III)
    private fun isValidName(name: String): Boolean {
        val nameRegex = "^[A-Za-z]+(?:\\.?[A-Za-z]+)*(?: [A-Za-z]+(?:\\.?[A-Za-z]+)*)*(?: (II|III|IV|V|VI|VII|VIII|IX|X))?$".toRegex()
        return name.matches(nameRegex)
    }

    // EMAIL VALIDATION (Must contain @ and follow common email rules)
    private fun isValidEmail(email: String): Boolean {
        val allowedDomains = listOf(
            "@gmail.com", "@yahoo.com", "@outlook.com",
            "@hotmail.com", "@icloud.com", "@aol.com",
            "@protonmail.com", "@zoho.com"
        )
        val regex = "^[a-z]{8,}[a-z0-9._%+-]*@[a-z]+\\.[a-z]{2,6}\$".toRegex()
        val atIndex = email.indexOf("@")

        if (atIndex < 8 || !regex.matches(email) || email.contains(" ") ||
            email.any { it.isUpperCase() } || email.any { !it.isLetterOrDigit() && it !in ".@_-" }
        ) {
            return false
        }

        return allowedDomains.any { email.endsWith(it, ignoreCase = true) }
    }

    // Remove Emojis from Input
    private fun removeEmojis(input: String): String {
        return input.replace(Regex("[\\p{So}\\p{Cn}]"), "")
    }
}