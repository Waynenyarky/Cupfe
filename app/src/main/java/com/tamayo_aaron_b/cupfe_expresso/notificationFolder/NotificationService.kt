package com.tamayo_aaron_b.cupfe_expresso.notificationFolder

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tamayo_aaron_b.cupfe_expresso.NotificationResponse
import com.tamayo_aaron_b.cupfe_expresso.Notifications
import com.tamayo_aaron_b.cupfe_expresso.R
import com.tamayo_aaron_b.cupfe_expresso.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Settings


class NotificationService(private val context: Context, private val userEmail: String) {

    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 5000L // Check every 5 seconds
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("CupfeExpressoPrefs", Context.MODE_PRIVATE)

    private val refreshRunnable = object : Runnable {
        override fun run() {
            checkForNewNotifications()
            handler.postDelayed(this, refreshInterval) // Schedule next check
        }
    }

    fun startChecking() {
        handler.post(refreshRunnable) // Start the loop
    }

    private fun checkForNewNotifications() {
        RetrofitClient.instance.getNotifications(userEmail)
            .enqueue(object : Callback<List<NotificationResponse>> {
                override fun onResponse(
                    call: Call<List<NotificationResponse>>,
                    response: Response<List<NotificationResponse>>
                ) {
                    if (response.isSuccessful) {
                        val notifications = response.body() ?: emptyList()
                        if (notifications.isNotEmpty()) {
                            val latestNotificationId = notifications.last().id // Assuming notifications have an ID

                            val lastSentNotificationId = sharedPreferences.getInt("lastNotificationId", -1)

                            if (latestNotificationId != lastSentNotificationId) {
                                showNotification() // Show a floating notification when new notifications are available
                                saveLastNotificationId(latestNotificationId) // Save latest notification ID
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<NotificationResponse>>, t: Throwable) {
                    Log.e("NotificationService", "Error fetching notifications: ${t.message}")
                }
            })
    }

    private fun showNotification() {
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            context.startActivity(intent)
        }

        val textView = TextView(context).apply {
            text = "You have new notifications in Cupfe Expresso."
            textSize = 18f
            setBackgroundColor(ContextCompat.getColor(context, R.color.light_brown))
            setTextColor(Color.WHITE)
            setPadding(50, 30, 50, 30)
            gravity = Gravity.CENTER
        }

        // Set layout parameters to make the view float at the top of the screen
        val layoutParams = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY // Requires SYSTEM_ALERT_WINDOW permission
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 100 // Adjust vertical position
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }

        // Add the custom view to the window
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(textView, layoutParams)

        // Remove the view after a delay (e.g., 3 seconds)
        Handler(Looper.getMainLooper()).postDelayed({
            windowManager.removeView(textView)
        }, 3000)


        // Vibrate the phone for 500 milliseconds (customize this as needed)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // For older devices, use the deprecated vibrate method
            vibrator.vibrate(5000)
        }

        // Continue showing the standard notification as well (optional)
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val channelId = "cupfe_expresso_notifications"
            val notificationId = 1

            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("user_email", "") ?: ""

            val intent = Intent(context, Notifications::class.java).apply {
                putExtra("user_email", email)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "New Notifications", NotificationManager.IMPORTANCE_DEFAULT)
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.cupfe_icon)
                .setContentTitle("New Updates")
                .setContentText("You have new notifications in Cupfe Expresso.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
    }

    private fun saveLastNotificationId(notificationId: Int) {
        sharedPreferences.edit().putInt("lastNotificationId", notificationId).apply()
    }
}
