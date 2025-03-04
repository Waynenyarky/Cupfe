package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class sign_in_page : AppCompatActivity() {
    private lateinit var btnOTP: Button
    private lateinit var btnSignInAcc: Button
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
        btnSignInAcc = findViewById(R.id.btnSignInAcc)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPass = findViewById<EditText>(R.id.etPass)
        val ivEye = findViewById<ImageView>(R.id.ivEye)
        val etOTP = findViewById<EditText>(R.id.etOTP)
        btnOTP = findViewById(R.id.btnOTP)

        val paddingEnd = 50  // Adjust the right padding to fit the icon properly
        etEmail.setPadding(etEmail.paddingLeft, etEmail.paddingTop, paddingEnd,etEmail.paddingBottom)

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


        // Send OTP button
        btnOTP.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPass.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Please enter your email"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPass.error = "Please enter your password"
                etPass.requestFocus()
                return@setOnClickListener
            }

            sendOTP(email, password)
        }


        // Verify OTP & Login button
        btnSignInAcc.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPass.text.toString().trim()
            val otp = etOTP.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Please enter your email"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPass.error = "Please enter your password"
                etPass.requestFocus()
                return@setOnClickListener
            }

            if (otp.isEmpty()) {
                etOTP.error = "Please enter OTP"
                etOTP.requestFocus()
                return@setOnClickListener
            }

            // Disable button to prevent multiple clicks
            btnSignInAcc.isEnabled = false
            btnSignInAcc.text = "Verifying..."
            verifyOTPLogin(email, password, otp)
            etEmail.text.clear()
            etPass.text.clear()
            etOTP.text.clear()
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

    private fun sendOTP(email: String, password: String) {
        val request = LoginRequest(email, password)

        // Disable button and show loading
        btnOTP.isEnabled = false
        btnOTP.text = "Sending..."

        RetrofitClient1.instance.sendOTP(request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body()
                val errorBody = response.errorBody()?.string()

                println("Raw API Response: $errorBody")  // Debug log

                if (response.isSuccessful && responseBody != null) {
                    Toast.makeText(this@sign_in_page, "OTP sent successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@sign_in_page, "Error: ${response.message()} - ${errorBody ?: "No details"}", Toast.LENGTH_SHORT).show()
                }
                // Enable button and restore text after response
                btnOTP.isEnabled = true
                btnOTP.text = "Send OTP"
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                // Enable button and restore text on failure
                btnOTP.isEnabled = true
                btnOTP.text = "Send OTP"

                t.printStackTrace()
                Toast.makeText(this@sign_in_page, "Failed to send OTP: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }



    private fun verifyOTPLogin(email: String, password: String, otp: String) {
        val request = VerifyOTPRequest("", email, password, otp)

        RetrofitClient1.instance.verifyOTPSignIn(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                runOnUiThread {
                    btnSignInAcc.isEnabled = true
                    btnSignInAcc.text = "Sign In"

                    if (response.isSuccessful) {
                        showSuccessDialog()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Toast.makeText(this@sign_in_page, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                runOnUiThread {
                    btnSignInAcc.isEnabled = true
                    btnSignInAcc.text = "Sign In"

                    Toast.makeText(this@sign_in_page, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
                t.printStackTrace()
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
            val intent = Intent(this@sign_in_page, Main_Home_Page::class.java)
            startActivity(intent)
            finish() // Close the sign-in page
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}