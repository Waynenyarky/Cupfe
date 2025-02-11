package com.tamayo_aaron_b.cupfe_expresso

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class create_sign_in_up_page : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sign_in_up_page)

        val sign_in = findViewById<Button>(R.id.sign_in)
        val sign_up = findViewById<Button>(R.id.sign_up)
        val tvGuestLink2 = findViewById<TextView>(R.id.guest_link2)
        val content = getString(R.string.guest_text)

        tvGuestLink2.setOnClickListener{
            val guest = Intent(this, Guest_Main_Home_Page::class.java)
            startActivity(guest)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        sign_in.setOnClickListener{
            sign_in.animate()
                .translationY(-10f) // Move up slightly for the pop effect
                .setDuration(190) // Quick pop-up
                .setInterpolator(android.view.animation.DecelerateInterpolator()) // Smooth easing
                .withEndAction {
                    sign_in.animate()
                        .translationY(0f) // Move back down smoothly
                        .setDuration(190)
                        .setInterpolator(android.view.animation.DecelerateInterpolator()) // Smooth easing
                        .withEndAction {
                            // Start the Sign-In page with fade-in effect
                            val intent = Intent(this, sign_in_page::class.java)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // Smooth transition
                        }
                }
        }

        sign_up.setOnClickListener{
            sign_up.animate()
                .translationY(-10f) // Move up slightly for the pop effect
                .setDuration(190) // Quick pop up
                .setInterpolator(android.view.animation.DecelerateInterpolator())
                .withEndAction {
                    sign_up.animate()
                        .translationY(0f) // Move back down smoothly
                        .setDuration(190)
                        .setInterpolator(android.view.animation.DecelerateInterpolator())
                        .withEndAction {
                            // Start the Sign-Up page with fade-in effect
                            val click2 = Intent(this, sign_up_page::class.java)
                            startActivity(click2)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // Smooth transition
                        }
                }
        }



        val underlineSpan = SpannableString(content).apply {
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        tvGuestLink2.text = underlineSpan


    }
}