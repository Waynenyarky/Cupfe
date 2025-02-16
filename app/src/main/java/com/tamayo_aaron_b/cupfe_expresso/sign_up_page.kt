package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
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

class sign_up_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val guest_link = findViewById<TextView>(R.id.guest_link)
        val content = getString(R.string.sign_in)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etFName = findViewById<EditText>(R.id.etFName)
        val etPass = findViewById<EditText>(R.id.etPass)
        val etCPass = findViewById<EditText>(R.id.etCPass)
        val btnSignInAcc = findViewById<Button>(R.id.btnSignUpAcc)

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

        btnSignInAcc.setOnClickListener {
            val email = etEmail.text.toString()
            val name = etFName.text.toString()
            val password = etPass.text.toString()
            val c_password = etCPass.text.toString()

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
            if (name.isEmpty()) {
                etFName.error = "Please complete the text field."
                etFName.requestFocus()
                return@setOnClickListener
            }
            if (!isValidName(name)) {
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

            if (password != c_password) {
                etCPass.error = "Passwords do not match."
                etCPass.requestFocus()
                return@setOnClickListener
            }
        }



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
        val nameRegex = "^[A-Za-z]{1,20}(?: [A-Za-z]{1,20})*(?: (II|III|IV|V|VI|VII|VIII|IX|X))?$".toRegex()
        return name.matches(nameRegex)
    }

    // PASSWORD VALIDATION
    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&+=!])(?!.*[\\s\\p{So}]).{8,}\$".toRegex()
        return password.matches(passwordRegex)
    }
}