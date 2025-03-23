package com.tamayo_aaron_b.cupfe_expresso

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class sign_up_page2 : AppCompatActivity() {

    private lateinit var etOTP: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page2)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnSignInAcc = findViewById<Button>(R.id.btnSignInAcc)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        etOTP = findViewById(R.id.etOTP)


        btnBack.setOnClickListener{
            val back = Intent(this, sign_up_page::class.java)
            startActivity(back)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
        }

        val email = intent.getStringExtra("EMAIL")
        tvEmail.text = email


        btnSignInAcc.setOnClickListener { verifyUserInput() }
    }

    private fun verifyUserInput() {
        val email = intent.getStringExtra("EMAIL") ?: ""
        val username = intent.getStringExtra("USERNAME") ?: ""
        val password = intent.getStringExtra("PASSWORD") ?: ""
        val otp = etOTP.text.toString().trim()
        val role = "customer"

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Invalid request. Please try again.", Toast.LENGTH_SHORT).show()
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
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && !apiResponse.error) {
                        showSuccessDialog()
                    } else {
                        Toast.makeText(applicationContext, apiResponse?.message ?: "OTP verification failed", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_success2)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnOkay = dialog.findViewById<Button>(R.id.btnOkay)
        btnOkay.setOnClickListener {
            dialog.dismiss()
            navigateToSignInPage()
        }

        dialog.show()
    }

    private fun navigateToSignInPage() {
        val username = intent.getStringExtra("USERNAME") ?: ""

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("USERNAME", username).apply()
        Log.d("DEBUG", "Username Saved: $username")

        val intent = Intent(this, sign_in_page::class.java).apply{
            putExtra("USERNAME", username)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
        Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show()
    }

}