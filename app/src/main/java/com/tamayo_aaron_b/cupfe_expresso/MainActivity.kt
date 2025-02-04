package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val GetStarted = findViewById<Button>(R.id.GetStarted)

        GetStarted.setOnClickListener{
            GetStarted.animate()
                .translationY(-10f) // Move up slightly for the pop effect
                .setDuration(190) // Quick pop-up
                .setInterpolator(android.view.animation.DecelerateInterpolator()) // Smooth easing
                .withEndAction {
                    GetStarted.animate()
                        .translationY(0f) // Move back down smoothly
                        .setDuration(190)
                        .setInterpolator(android.view.animation.DecelerateInterpolator()) // Smooth easing
                        .withEndAction {
                            val click = Intent(this, create_sign_in_up_page::class.java)
                            startActivity(click)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // Smooth transition
                        }
                }
        }

    }
}