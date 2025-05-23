package com.tamayo_aaron_b.cupfe_expresso

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Help : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_help)

        val ivBack = findViewById<ImageView>(R.id.ivBack)

        // Back Home
        ivBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

    }
}