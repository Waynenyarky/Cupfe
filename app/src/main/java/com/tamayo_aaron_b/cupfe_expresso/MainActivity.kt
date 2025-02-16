package com.tamayo_aaron_b.cupfe_expresso

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.VideoView
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

        val videoView = findViewById<VideoView>(R.id.videoView)
        // Hide system UI for fullscreen effect
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        val videoPath = "android.resource://$packageName/${R.raw.intro_cupfe}"
        videoView.setVideoURI(Uri.parse(videoPath))

        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true // Loop the video
            mp.setVolume(0f, 0f) // Mute if needed
            videoView.start() // Start video automatically

            // Get screen width and set VideoView width dynamically
            val screenWidth = resources.displayMetrics.widthPixels
            videoView.layoutParams.width = (screenWidth * 2.50).toInt() // 85% of screen width
            videoView.requestLayout()

            // Scale the video properly
            val videoRatio = mp.videoWidth.toFloat() / mp.videoHeight.toFloat()
            val screenRatio = videoView.width.toFloat() / videoView.height.toFloat()
            if (videoRatio < screenRatio) {
                videoView.scaleX = screenRatio / videoRatio
            } else {
                videoView.scaleY = videoRatio / screenRatio
            }
        }

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