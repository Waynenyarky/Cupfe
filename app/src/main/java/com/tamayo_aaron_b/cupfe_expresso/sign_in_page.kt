package com.tamayo_aaron_b.cupfe_expresso

import android.app.Dialog
import android.content.Context
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
import android.util.Log
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

        val paddingEnd = 50  // Adjust the right padding to fit the icon properly
        etEmail.setPadding(etEmail.paddingLeft, etEmail.paddingTop, paddingEnd,etEmail.paddingBottom)

        tvForgotPass.setOnClickListener{
            val forgotPassword = Intent(this, ForgotPassword::class.java)
            startActivity(forgotPassword)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        tvGuestLink.setOnClickListener{
            val guest = Intent(this, Guest_Main_Home_Page::class.java)
            startActivity(guest)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Retrieve from Intent
        var username = intent.getStringExtra("USERNAME")

        // If Intent doesn't have it, get it from SharedPreferences
        if (username.isNullOrEmpty()) {
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            username = sharedPreferences.getString("USERNAME", "")
        }

        Log.d("DEBUG", "Name 123 Saved: $username")

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




        // Verify OTP & Login button
        btnSignInAcc.setOnClickListener {
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


            // Disable button to prevent multiple clicks
            btnSignInAcc.isEnabled = false
            btnSignInAcc.text = "Verifying..."
            verifyOTPLogin(email, password)
            etEmail.text.clear()
            etPass.text.clear()
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


    private fun verifyOTPLogin(email: String, password: String) {
        val request = OTPRequest(email = email, username = "", password = password)

        RetrofitClient1.instance.verifyOTPSignIn(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                runOnUiThread {
                    btnSignInAcc.isEnabled = true
                    btnSignInAcc.text = "Sign In"

                    val responseBody = response.body()
                    val errorBody = response.errorBody()?.string()

                    if (response.isSuccessful && responseBody != null) {
                        val token = responseBody.token ?: "" // Assuming `key1` contains the token
                        saveUserDetails(email, token) // Save token to SharedPreferences
                        showSuccessDialog()
                    } else {
                        val errorMessage = errorBody ?: "Unknown error"
                        Toast.makeText(this@sign_in_page, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                        Log.e("API_ERROR", "Error response: $errorMessage")
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

    private fun saveUserDetails(email: String, token: String) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)  // Save user email
        editor.putString("auth_token", token)  // Save authentication token
        editor.apply()

        Log.d("DEBUG", "Email Saved: $email")
        Log.d("DEBUG", "Token Saved: $token")

    }


    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_success)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnOkay = dialog.findViewById<Button>(R.id.btnOkay)
        btnOkay.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@sign_in_page, Main_Home_Page::class.java)
            startActivity(intent)
            Toast.makeText(this, "Sign In Successfully", Toast.LENGTH_SHORT).show()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish() // Close the sign-in page
        }

        dialog.show()
    }
}