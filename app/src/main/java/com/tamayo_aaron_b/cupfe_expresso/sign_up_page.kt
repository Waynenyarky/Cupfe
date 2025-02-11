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

        btnSignInAcc.setOnClickListener{
            val email = etEmail.text.toString()
            val name = etFName.text.toString()
            val password = etPass.text.toString()
            val c_password = etCPass.text.toString()

            if (email.isEmpty()){
                etEmail.error = "Please complete the text field."
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (name.isEmpty()){
                etFName.error = "Please complete the text field."
                etFName.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                etPass.error = "Please complete the text field."
                etPass.requestFocus()
                return@setOnClickListener
            }

            if (c_password.isEmpty()){
                etCPass.error = "Please complete the text field."
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
}