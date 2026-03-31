package com.medicare.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.medicare.app.R

object NotificationHelper {

    const val MEDICINE_CHANNEL_ID = "medicine_channel"
    const val FOOD_CHANNEL_ID = "food_channel"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val audioAttr = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val medicineChannel = NotificationChannel(
                MEDICINE_CHANNEL_ID, "Medicine Reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for medicine time"
                enableVibration(true)
                setSound(Uri.parse("android.resource://${context.packageName}/${R.raw.alert_sound}"), audioAttr)
            }

            val foodChannel = NotificationChannel(
                FOOD_CHANNEL_ID, "Food Reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for meal time"
                enableVibration(true)
                setSound(Uri.parse("android.resource://${context.packageName}/${R.raw.alert_sound}"), audioAttr)
            }

            nm.createNotificationChannel(medicineChannel)
            nm.createNotificationChannel(foodChannel)
        }
    }

    fun showMedicineNotification(context: Context, title: String, message: String, notifId: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, MEDICINE_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(Uri.parse("android.resource://${context.packageName}/${R.raw.alert_sound}"))
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        nm.notify(notifId, notification)
    }

    fun showFoodNotification(context: Context, title: String, message: String, notifId: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, FOOD_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(Uri.parse("android.resource://${context.packageName}/${R.raw.alert_sound}"))
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        nm.notify(notifId + 10000, notification)
    }
}