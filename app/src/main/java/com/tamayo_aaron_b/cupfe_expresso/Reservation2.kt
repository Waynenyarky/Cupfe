package com.tamayo_aaron_b.cupfe_expresso

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tamayo_aaron_b.cupfe_expresso.reservation.ReservationConnect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Reservation2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation2)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tvTransaction = findViewById<TextView>(R.id.tvTransaction)
        val tvTime = findViewById<TextView>(R.id.tvTime)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etcpNumber = findViewById<EditText>(R.id.etcpNumber)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val ReserveNow = findViewById<TextView>(R.id.ReserveNow)
        val checkButton = findViewById<ImageView>(R.id.Check)
        val checkButton2 = findViewById<ImageView>(R.id.Check2)
        val btnHome = findViewById<ImageView>(R.id.btnHome)

        btnHome.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.force_home, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            dialog.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
            val btnNo = dialogView.findViewById<Button>(R.id.btn_no)

            btnYes.setOnClickListener {
                // Navigate to home
                startActivity(Intent(this, Main_Home_Page::class.java))
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
                finish()
                dialog.dismiss()
            }

            btnNo.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }


        addValidationWithIcon(etFullName) { isValidName(it) }
        addValidationWithIcon(etcpNumber) { isValidPhoneNumber(it) }
        addValidationWithIcon(etEmail) { isValidEmail(it) }


        checkButton.setOnClickListener {
            Toast.makeText(applicationContext, "Not enough saved Points", Toast.LENGTH_SHORT).show()
        }

        checkButton2.setOnClickListener {
            Toast.makeText(applicationContext, "Not available this offer", Toast.LENGTH_SHORT).show()
        }
        val transactionId = intent.getStringExtra("finalPackageID") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "No Date & Time Selected"
        val time = intent.getStringExtra("TIME") ?: "No Date & Time Selected"
        val packageName = intent.getStringExtra("packageName") ?: ""
        val price = intent.getStringExtra("price") ?: ""
        val person = intent.getStringExtra("person") ?: ""
        val packageNameB = intent.getStringExtra("packageNameB")?.takeIf { it.isNotEmpty() }
        val priceB = intent.getStringExtra("priceB")?.takeIf { it.isNotEmpty() }
        val personB = intent.getStringExtra("personB")?.takeIf { it.isNotEmpty() }
        val packageNameC = intent.getStringExtra("packageNameC")?.takeIf { it.isNotEmpty() }
        val priceC = intent.getStringExtra("priceC")?.takeIf { it.isNotEmpty() }
        val personC = intent.getStringExtra("personC")?.takeIf { it.isNotEmpty() }

        val finalPackageName = listOfNotNull(packageName, packageNameB, packageNameC).joinToString(" ")

        val finalPrice = listOfNotNull(price, priceB, priceC).joinToString(" ")

        val finalPerson = listOfNotNull(person, personB, personC).joinToString(" ")

        // Set values to the same IDs, ensuring no overwriting occurs
        findViewById<TextView>(R.id.tvPackageName).text = finalPackageName
        findViewById<TextView>(R.id.tvPrice).text = finalPrice
        findViewById<TextView>(R.id.tvPerson).text = finalPerson
        tvTime.text = time
        tvDate.text = date
        tvTransaction.text = "Ref No. $transactionId"


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
            confirmationReserve(name, email, cellphone)
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



        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_top_to_bottom, R.anim.slide_out_bottom)
        }
    }

    private fun confirmationReserve(name: String, email: String, cellphone: String){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirmation_in_reservation)

        val tvName = dialog.findViewById<TextView>(R.id.name)
        val number = dialog.findViewById<TextView>(R.id.number)
        val tvEmail = dialog.findViewById<TextView>(R.id.tvEmail)
        val tvTransaction = dialog.findViewById<TextView>(R.id.tvTransaction)
        val tvDate = dialog.findViewById<TextView>(R.id.tvDate)
        val tvTime = dialog.findViewById<TextView>(R.id.tvTime)
        val tvPackageName = dialog.findViewById<TextView>(R.id.tvPackageName)
        val tvPrice = dialog.findViewById<TextView>(R.id.tvPrice)
        val tvPerson = dialog.findViewById<TextView>(R.id.tvPerson)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<TextView>(R.id.btnConfirm)
        val btnCopy = dialog.findViewById<ImageView>(R.id.btnCopy)


        val transactionId = intent.getStringExtra("finalPackageID") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "No Date & Time Selected"
        val time = intent.getStringExtra("TIME") ?: "No Date & Time Selected"
        val packageName = intent.getStringExtra("packageName")?:""
        val price = intent.getStringExtra("price")?.replace("₱", "") ?: ""
        val person = intent.getStringExtra("person")?:""
        val packageNameB = intent.getStringExtra("packageNameB")
        val priceB = intent.getStringExtra("priceB")?.replace("₱", "")
        val personB = intent.getStringExtra("personB")
        val packageNameC = intent.getStringExtra("packageNameC")
        val priceC = intent.getStringExtra("priceC")?.replace("₱", "")
        val personC = intent.getStringExtra("personC")

        // Combine package names, ensuring they are not empty and adding separators
        val finalPackageName = listOfNotNull(packageName, packageNameB, packageNameC)
            .filter { it.isNotEmpty() }
            .joinToString(", ")

        // Combine prices
        val finalPrice = listOfNotNull(price, priceB, priceC)
            .filter { it.isNotEmpty() }
            .joinToString(", ")

        // Combine person count
        val finalPerson = listOfNotNull(person, personB, personC)
            .filter { it.isNotEmpty() }
            .joinToString(", ")


        // Set values to the same IDs, ensuring no overwriting occurs
        tvTransaction.text = "Ref No. $transactionId"
        tvName.text = "Name: $name"
        number.text = "Number: $cellphone"
        tvEmail.text = "Email: $email"
        tvPackageName.text = finalPackageName
        tvPrice.text = finalPrice
        tvPerson.text = finalPerson
        tvTime.text = time
        tvDate.text = date

        // Initially disable confirm button
        var isTransactionCopied = false

        // Copy transaction ID functionality
        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Transaction ID", transactionId)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Transaction ID copied!", Toast.LENGTH_SHORT).show()

            // Enable confirm button after copying
            isTransactionCopied = true
        }


        btnCancel.setOnClickListener { dialog.dismiss() }

        btnConfirm.setOnClickListener {
            if (!isTransactionCopied) {
                Toast.makeText(this, "Please copy the reference number before proceeding!", Toast.LENGTH_SHORT).show()
            } else {
                val reservationTable = ReservationConnect(
                    transactionId,
                    name,
                    email,
                    cellphone,
                    finalPackageName,
                    finalPrice,
                    date,
                    time
                )

                RetrofitClient.instance.reserveTables(reservationTable)
                    .enqueue(object : Callback<ReservationResponse> {
                        override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                            if (response.isSuccessful && response.body() != null) {
                                val responseBody = response.body()
                                if (responseBody != null && responseBody.message == "Reservation created. Proceed to payment.") {
                                    Toast.makeText(this@Reservation2, "Redirecting to payment...", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.1.16/expresso-cafe/api/stripePayment/payment_form.php"))
                                    startActivity(intent)
                                    findViewById<EditText>(R.id.etFullName).text.clear()
                                    findViewById<EditText>(R.id.etcpNumber).text.clear()
                                    findViewById<EditText>(R.id.etEmail).text.clear()
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(this@Reservation2, "Failed: ${responseBody?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@Reservation2, "Server Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                            Toast.makeText(this@Reservation2, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }


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


    // NAME VALIDATION
    private fun isValidName(name: String): Boolean {
        val nameRegex = "^[A-Za-z]{3,20}(?: [A-Za-z]{1,20})*(?: (II|III|IV|V|VI|VII|VIII|IX|X))?$".toRegex()
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