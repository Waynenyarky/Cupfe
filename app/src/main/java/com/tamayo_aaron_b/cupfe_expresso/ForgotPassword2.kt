package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password2)

        val etOTP = findViewById<EditText>(R.id.etOTP)
        val btnResetPass = findViewById<Button>(R.id.btnResetPass)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener{
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        val email = intent.getStringExtra("EMAIL")
        tvEmail.text = email

        // Reset Password
        btnResetPass.setOnClickListener {
            val email = intent.getStringExtra("EMAIL")
            val otp = etOTP.text.toString().trim()
            val newPassword = intent.getStringExtra("NEW_PASSWORD")

            if (email.isNullOrEmpty() || newPassword.isNullOrEmpty()) {
                Toast.makeText(this, "Invalid request. Please try again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (otp.isEmpty()) {
                etOTP.error = "OTP is required."
                etOTP.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.resetPassword(ResetPasswordRequest(email, otp, newPassword))
                .enqueue(object : Callback<ResetPasswordResponse> {
                    override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ForgotPassword2, "Password Reset Successfully", Toast.LENGTH_SHORT).show()
                            // Redirect to Sign-in page
                            val intent = Intent(this@ForgotPassword2, sign_in_page::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        } else {
                            Toast.makeText(this@ForgotPassword2, "Failed to Reset Password", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                        Toast.makeText(this@ForgotPassword2, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }


    }
}