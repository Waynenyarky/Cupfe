package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Random

class PackageA : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_package)


        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)
        val btnGetPackage = findViewById<TextView>(R.id.getPackage)
        val packageName = findViewById<TextView>(R.id.packageName).text.toString()
        val price = findViewById<TextView>(R.id.tvPriceBundle).text.toString()
        val person = findViewById<TextView>(R.id.tvPerson).text.toString()
        val includes = findViewById<TextView>(R.id.tvIncludes).text.toString()

        btnGetPackage.setOnClickListener {
            val transactionId = generateTransactionId()
            val intent = Intent(this, Reservation::class.java)
            intent.putExtra("transactionId", transactionId)
            intent.putExtra("packageName", packageName)
            intent.putExtra("price", price)
            intent.putExtra("person", person)
            intent.putExtra("includes", includes)
            startActivity(intent)
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

        ivBackHome.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }

    }

    private fun generateTransactionId(): String {
        val random = Random()
        val letter = ('A'..'Z').random() // Random uppercase letter
        val numbers = (1..9).map { random.nextInt(10) }.joinToString("") // 9 random numbers
        return "$letter$numbers"
    }
}