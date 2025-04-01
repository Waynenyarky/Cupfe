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
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

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
                                showNotification()
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
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val channelId = "cupfe_expresso_notifications"
            val notificationId = 1

            // Vibrate the phone for 500 milliseconds (customize this as needed)
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                // For older devices, use the deprecated vibrate method
                vibrator.vibrate(5000)
            }

            // Retrieve email from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("user_email", "") ?: ""

            // Pass email as an extra in the intent
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
                .setSmallIcon(R.drawable.new_notification)
                .setContentTitle("New Updates")
                .setContentText("You have new notifications in Cupfe Expresso.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }
    }

    private fun saveLastNotificationId(notificationId: Int) {
        sharedPreferences.edit().putInt("lastNotificationId", notificationId).apply()
    }
}