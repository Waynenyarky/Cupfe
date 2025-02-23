package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class sign_in_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in_page)

        val tvSignIn = findViewById<TextView>(R.id.tvSignIn)
        val content = getString(R.string.sign_up)
        val tvForgotPass = findViewById<TextView>(R.id.tvForgotPass)
        val forgot = getString(R.string.forgot)
        val tvGuestLink = findViewById<TextView>(R.id.guest_link)
        val content1 = getString(R.string.guest_text)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnSignInAcc = findViewById<Button>(R.id.btnSignInAcc)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPass = findViewById<EditText>(R.id.etPass)
        val ivEye = findViewById<ImageView>(R.id.ivEye)

        tvGuestLink.setOnClickListener{
            val guest = Intent(this, Guest_Main_Home_Page::class.java)
            startActivity(guest)
        }

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


        //Email and Password Correction
        btnSignInAcc.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPass.text.toString()

            if (email.isEmpty()){
                etEmail.error = "Please enter your email"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                etPass.error = "Please enter your password"
                etPass.requestFocus()
                return@setOnClickListener
            }

            btnSignInAcc.setOnClickListener{
                val guest = Intent(this, Main_Home_Page::class.java)
                startActivity(guest)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                // Clear fields
                etEmail.text.clear()
                etPass.text.clear()
            }
        }


        // Add emoji detection to all text fields
        listOf(etEmail, etPass).forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val newText = removeEmojis(it.toString())
                        if (newText != it.toString()) {
                            Toast.makeText(this@sign_in_page, "Can't input an Emoji", Toast.LENGTH_SHORT).show()
                            editText.setText(newText)
                            editText.setSelection(newText.length) // Move cursor to the end
                        }
                    }
                }
            })
        }

        // Prevent Enter key press on each EditText
        listOf(etEmail, etPass).forEach { editText ->
            editText.setOnEditorActionListener { _, _, _ ->
                Toast.makeText(this, "Can't enter a new line!", Toast.LENGTH_SHORT).show()
                true // Return true to consume the event
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
            false // Allow other keys to work normally
        }


        //Sign up to Sign in
        tvSignIn.setOnClickListener{
            val intent = Intent(this, sign_up_page::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //Back Btn
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
        val underlineSpan2 = SpannableString(forgot).apply {
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        val underlineSpan3 = SpannableString(content1).apply {
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        tvSignIn.text = underlineSpan
        tvForgotPass.text = underlineSpan2
        tvGuestLink.text = underlineSpan3


    }
    // Removes emojis from a given string
    private fun removeEmojis(input: String): String {
        return input.replace(Regex("[\\p{So}\\p{Cn}]"), "")
    }
}