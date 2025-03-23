package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)


        val tvReturn = findViewById<TextView>(R.id.tvReturn)
        val content = getString(R.string.forgotPass)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnOTP = findViewById<Button>(R.id.btnOTP)
        val etNewPass = findViewById<EditText>(R.id.etNewPass)
        val ivEye = findViewById<ImageView>(R.id.ivEye)
        val etRePass = findViewById<EditText>(R.id.etRePass)
        val ivEye2 = findViewById<ImageView>(R.id.ivEye2)

        var isPasswordVisible = false
        ivEye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                etNewPass.transformationMethod = null // Show password
                ivEye.setImageResource(R.drawable.eye_show)
            } else {
                etNewPass.transformationMethod = PasswordTransformationMethod.getInstance() // Hide password
                ivEye.setImageResource(R.drawable.eye_hide)
            }

            etNewPass.setSelection(etNewPass.text.length) // Maintain cursor position
        }

        var isPasswordVisible2 = false

        ivEye2.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2

            if (isPasswordVisible2) {
                etRePass.transformationMethod = null // Show password
                ivEye2.setImageResource(R.drawable.eye_show)
            } else {
                etRePass.transformationMethod = PasswordTransformationMethod.getInstance() // Hide password
                ivEye2.setImageResource(R.drawable.eye_hide)
            }

            etRePass.setSelection(etRePass.text.length) // Maintain cursor position
        }



        val underlineSpan = SpannableString(content).apply {
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        tvReturn.text = underlineSpan


        btnBack.setOnClickListener{
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        tvReturn.setOnClickListener{
            val go = Intent(this, sign_up_page::class.java)
            startActivity(go)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Add emoji detection to all text fields
        listOf(etEmail, etNewPass).forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val newText = removeEmojis(it.toString())
                        if (newText != it.toString()) {
                            Toast.makeText(this@ForgotPassword, "Can't input an Emoji", Toast.LENGTH_SHORT).show()
                            editText.setText(newText)
                            editText.setSelection(newText.length) // Move cursor to the end
                        }
                    }
                }
            })
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

        addValidationWithIcon1(etEmail) { isValidEmail(it) }
        addValidationWithIcon(etNewPass) { isValidPassword(it) }
        addValidationWithIcon(etRePass) { isValidPassword(it) }

        // Send OTP
        btnOTP.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val newPassword = etNewPass.text.toString().trim()
            val confirmPassword = etRePass.text.toString().trim()

            if (!isValidEmail(email)) {
                etEmail.error = "Please enter you valid email."
                etEmail.requestFocus()
                return@setOnClickListener
            }


            if (!isValidPassword(newPassword)) {
                etNewPass.error = "Please enter your new password."
                etNewPass.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword != newPassword) {
                etNewPass.error = "Passwords do not match."
                etNewPass.requestFocus()
                return@setOnClickListener
            }


            btnOTP.isEnabled = false
            btnOTP.text = "Verifying..."

            RetrofitClient.instance.generatePasswordChangeOTP(OTPRequests(email))
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ForgotPassword, "OTP sent successfully", Toast.LENGTH_SHORT).show()

                            // Start ForgotPassword2 activity and pass email & new password
                            val intent = Intent(this@ForgotPassword, ForgotPassword2::class.java)
                            intent.putExtra("EMAIL", email)
                            intent.putExtra("NEW_PASSWORD", newPassword)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        } else {
                            Toast.makeText(this@ForgotPassword, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                        }
                        btnOTP.isEnabled = true
                        btnOTP.text = "Send OTP"
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        btnOTP.isEnabled = true
                        btnOTP.text = "Send OTP"
                        Toast.makeText(this@ForgotPassword, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }




    }
    // Removes emojis from a given string
    private fun removeEmojis(input: String): String {
        return input.replace(Regex("[\\p{So}\\p{Cn}]"), "")
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

    private fun isValidEmail(email: String): Boolean {
        val allowedDomains = listOf("@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com", "@icloud.com", "@aol.com", "@protonmail.com", "@zoho.com")
        val regex = "^[a-z]{8,}[a-z0-9._%+-]*@[a-z]+\\.[a-z]{2,6}\$".toRegex()
        val atIndex = email.indexOf("@")

        if (atIndex < 8 || !regex.matches(email) || email.contains(" ") || email.any { it.isUpperCase() } || email.any { !it.isLetterOrDigit() && it !in ".@_-"}) {
            return false
        }

        return allowedDomains.any { email.endsWith(it, ignoreCase = true) }
    }
}