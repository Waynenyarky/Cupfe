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

class PackageC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_package_c)


        val ivBackHome = findViewById<ImageView>(R.id.ivBackHome)
        val btnGetPackage = findViewById<TextView>(R.id.getPackageC)
        val packageNameC = findViewById<TextView>(R.id.packageNameC).text.toString()
        val priceC = findViewById<TextView>(R.id.tvPriceBundleC).text.toString()
        val personC = findViewById<TextView>(R.id.tvPersonC).text.toString()
        val includesC = findViewById<TextView>(R.id.tvIncludesC).text.toString()


        btnGetPackage.setOnClickListener {
            val transactionId2 = generateTransactionId()
            val intent = Intent(this, Reservation::class.java)
            intent.putExtra("transactionId2", transactionId2)
            intent.putExtra("packageNameC", packageNameC)
            intent.putExtra("priceC", priceC)
            intent.putExtra("personC", personC)
            intent.putExtra("includesC", includesC)
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