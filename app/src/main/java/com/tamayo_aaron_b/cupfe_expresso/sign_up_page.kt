package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class sign_up_page : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etFName: EditText
    private lateinit var etPass: EditText
    private lateinit var etCPass: EditText
    private lateinit var etOTP: EditText
    private lateinit var btnOTP: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val guest_link = findViewById<TextView>(R.id.guest_link)
        val content = getString(R.string.sign_in)
        etEmail = findViewById(R.id.etEmail)
        etFName = findViewById(R.id.etFName)
        etPass = findViewById(R.id.etPass)
        etCPass = findViewById(R.id.etCPass)
        etOTP = findViewById(R.id.etOTP)
        val btnSignInAcc = findViewById<Button>(R.id.btnSignUpAcc)
        val ivEye = findViewById<ImageView>(R.id.ivEye)

        btnOTP = findViewById(R.id.btnOTP)

        // Send OTP when clicking btnOTP
        btnOTP.setOnClickListener {
            sendOTP()
        }


        addValidationWithIcon1(etEmail) { isValidEmail(it) }
        addValidationWithIcon1(etFName) { isValidName(it) }
        addValidationWithIcon(etPass) { isValidPassword(it) }

        var isPasswordVisible = false

        ivEye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                etPass.transformationMethod = null // Show password
                ivEye.setImageResource(R.drawable.eye_show)
            } else {
                etPass.transformationMethod = PasswordTransformationMethod.getInstance() // Hide password
                ivEye.setImageResource(R.drawable.eye_hide)
            }

            etPass.setSelection(etPass.text.length) // Maintain cursor position
        }


        // Add emoji detection to all text fields
        listOf(etEmail, etFName, etPass, etCPass).forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val newText = removeEmojis(it.toString())
                        if (newText != it.toString()) {
                            Toast.makeText(this@sign_up_page, "Can't input an Emoji", Toast.LENGTH_SHORT).show()
                            editText.setText(newText)
                            editText.setSelection(newText.length) // Move cursor to the end
                        }
                    }
                }
            })
        }

        // Prevent Enter key press on each EditText
        listOf(etEmail, etFName, etPass, etCPass).forEach { editText ->
            editText.setRawInputType(android.text.InputType.TYPE_CLASS_TEXT) // Force single-line input
            editText.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(this, "Can't enter a new line!", Toast.LENGTH_SHORT).show()
                    return@setOnKeyListener true // Consume the event and prevent new line
                }
                false // Allow other keys to work normally
            }
        }

        // Disable new line (Enter key) for etEmail
        etEmail.setSingleLine(true) // Forces single-line input
        etEmail.setRawInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) // Ensures email format
        etEmail.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(this, "Can't enter a new line!", Toast.LENGTH_SHORT).show()
                return@setOnKeyListener true // Consume the event to disable Enter
            }
            false
        }

        btnSignInAcc.setOnClickListener {
            val email = etEmail.text.toString()
            val username = etFName.text.toString()
            val password = etPass.text.toString()
            val confirmPassword = etCPass.text.toString()

            // EMAIL
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

            // NAME
            if (username.isEmpty()) {
                etFName.error = "Please complete the text field."
                etFName.requestFocus()
                return@setOnClickListener
            }
            if (!isValidName(username)) {
                etFName.error = "Invalid name format."
                etFName.requestFocus()
                return@setOnClickListener
            }

            // PASSWORD
            if (!isValidPassword(password)) {
                etPass.error = "Password must be strong and not contain spaces or emojis."
                etPass.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                etCPass.error = "Passwords do not match."
                etCPass.requestFocus()
                return@setOnClickListener
            }
            verifyUserInput()
        }

        val checkIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_check)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        val paddingEnd = 50  // Adjust the right padding to fit the icon properly
        etCPass.setPadding(etCPass.paddingLeft, etCPass.paddingTop, paddingEnd,etCPass.paddingBottom)
        etCPass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = etPass.text.toString()
                val confirmPassword = s.toString()



                etCPass.post {
                    if (confirmPassword == password && confirmPassword.isNotEmpty()) {
                        etCPass.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, checkIcon, null)
                        etCPass.compoundDrawablePadding = 20
                        etCPass.error = null
                    } else {
                        etCPass.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })




        guest_link.setOnClickListener{
            val intent = Intent(this, sign_in_page::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }



        btnBack.setOnClickListener{
            if (!isFinishing) {
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }


        //Underline
        val underlineSpan = SpannableString(content).apply {
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        guest_link.text = underlineSpan

        }

    // Removes emojis from a given string
    private fun removeEmojis(input: String): String {
        return input.replace(Regex("[\\p{So}\\p{Cn}]"), "")
    }

    // EMAIL VALIDATION
    private fun isValidEmail(email: String): Boolean {
        val allowedDomains = listOf("@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com", "@icloud.com", "@aol.com", "@protonmail.com", "@zoho.com")
        val regex = "^[a-z]{8,}[a-z0-9._%+-]*@[a-z]+\\.[a-z]{2,6}\$".toRegex()
        val atIndex = email.indexOf("@")

        if (atIndex < 8 || !regex.matches(email) || email.contains(" ") || email.any { it.isUpperCase() } || email.any { !it.isLetterOrDigit() && it !in ".@_-"}) {
            return false
        }

        return allowedDomains.any { email.endsWith(it, ignoreCase = true) }
    }

    // NAME VALIDATION
    private fun isValidName(name: String): Boolean {
        val nameRegex = "^[A-Za-z]{3,20}(?: [A-Za-z]{1,20})*(?: (II|III|IV|V|VI|VII|VIII|IX|X))?$".toRegex()
        return name.matches(nameRegex)
    }

    // PASSWORD VALIDATION
    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&+=!])(?!.*[\\s\\p{So}]).{8,}\$".toRegex()
        return password.matches(passwordRegex)
    }

    //VALID or INVALID
    private fun addValidationWithIcon(editText: EditText, validator: (String) -> Boolean) {
        val checkIcon: Drawable? = ContextCompat.getDrawable(editText.context, R.drawable.ic_check)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }

        val paddingEnd = 140  // Adjust the right padding to fit the icon properly

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

    private fun addValidationWithIcon1(editText: EditText, validator: (String) -> Boolean) {
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



    // Function to send OTP
    private fun sendOTP() {
        val email = etEmail.text.toString().trim()
        val username = etFName.text.toString().trim()
        val password = etPass.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return
        }

        if (username.isEmpty()) {
            etFName.error = "Username is required"
            etFName.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPass.error = "Password is required"
            etPass.requestFocus()
            return
        }

        val request = OTPRequest(email, username, password, "customer")

        // Disable button and show loading
        btnOTP.isEnabled = false
        btnOTP.text = "Sending..."

        RetrofitClient.instance.sendOTP(request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    response.body()
                    Toast.makeText(applicationContext, "OTP sent successfully", Toast.LENGTH_LONG).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Server Error", "Error: $errorBody")
                    Toast.makeText(applicationContext, "Server Error: $errorBody", Toast.LENGTH_LONG).show()
                }
                // Enable button and restore text after response
                btnOTP.isEnabled = true
                btnOTP.text = "Send OTP"
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Enable button and restore text after response
                btnOTP.isEnabled = true
                btnOTP.text = "Send OTP"

                Log.e("sendOTP", "Network Error", t)
                Toast.makeText(applicationContext, "Network error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }




    // Function to verify OTP and create account
    private fun verifyUserInput() {
        val email = etEmail.text.toString().trim()
        val username = etFName.text.toString().trim()
        val password = etPass.text.toString()
        val confirmPassword = etCPass.text.toString()
        val otp = etOTP.text.toString().trim()
        val role = "customer" // Default role

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return
        }

        if (username.isEmpty()) {
            etFName.error = "Name is required"
            etFName.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPass.error = "Password is required"
            etPass.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            etCPass.error = "Confirm Password is required"
            etCPass.requestFocus()
            return
        }

        if (password != confirmPassword) {
            etCPass.error = "Passwords do not match"
            etCPass.requestFocus()
            return
        }

        if (otp.isEmpty()) {
            etOTP.error = "OTP is required"
            etOTP.requestFocus()
            return
        }

        verifyOTP(username, email, password, otp, role)
    }

    private fun verifyOTP(username: String, email: String, password: String, otp: String, role: String) {
        val request = VerifyOTPRequest(username, email, password, otp, role)

        RetrofitClient.instance.verifyOTP(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                val apiResponse = response.body()
                if (response.isSuccessful && apiResponse != null && !apiResponse.error) {
                    showSuccessDialog()
                } else {
                    Toast.makeText(applicationContext, apiResponse?.message ?: "OTP verification failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun showSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnOkay = dialogView.findViewById<Button>(R.id.btnOkay)
        btnOkay.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@sign_up_page, sign_in_page::class.java)
            startActivity(intent)
            finish() // Close the sign-in page
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}