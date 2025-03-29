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

class PackageB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_package_b)

        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)
        val btnGetPackage = findViewById<TextView>(R.id.getPackageB)
        val packageNameB = findViewById<TextView>(R.id.packageNameB).text.toString()
        val priceB = findViewById<TextView>(R.id.tvPriceBundleB).text.toString()
        val personB = findViewById<TextView>(R.id.tvPersonB).text.toString()
        val includesB = findViewById<TextView>(R.id.tvIncludesB).text.toString()

        btnGetPackage.setOnClickListener {
            val transactionId1 = generateTransactionId()
            val intent = Intent(this, Reservation::class.java)
            intent.putExtra("transactionId1", transactionId1)
            intent.putExtra("packageNameB", packageNameB)
            intent.putExtra("priceB", priceB)
            intent.putExtra("personB", personB)
            intent.putExtra("includesB", includesB)
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
        val numbers = (1..7).map { random.nextInt(10) }.joinToString("") // 9 random numbers
        return "TAB$numbers"
    }
}