package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
        }


        //Sign up to Sign in
        tvSignIn.setOnClickListener{
            val intent = Intent(this, sign_up_page::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //Back Btn
        btnBack.setOnClickListener{
            val back = Intent(this, create_sign_in_up_page::class.java )
            startActivity(back)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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
}