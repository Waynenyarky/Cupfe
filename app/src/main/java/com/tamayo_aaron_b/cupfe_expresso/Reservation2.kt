package com.tamayo_aaron_b.cupfe_expresso

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Reservation2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reservation2)




        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tvDateTime = findViewById<TextView>(R.id.tvDateTime)
        val tvPeople = findViewById<TextView>(R.id.tvPeople)
        val dateTime = intent.getStringExtra("DATE_TIME") ?: "No Date & Time Selected"
        val people = intent.getStringExtra("PEOPLE") ?: "No Guests Selected"

        tvDateTime.text = dateTime
        tvPeople.text = people


        btnBack.setOnClickListener{
            finish()
        }

    }
}