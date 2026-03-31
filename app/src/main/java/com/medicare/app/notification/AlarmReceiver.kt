package com.medicare.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.medicare.app.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra("type") ?: return
        val name = intent.getStringExtra("name") ?: ""
        val extra = intent.getStringExtra("extra") ?: ""
        val id = intent.getIntExtra("id", 0)

        // Play alert sound
        try {
            val mp = MediaPlayer.create(context, R.raw.alert_sound)
            mp.setOnCompletionListener { it.release() }
            mp.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        when (type) {
            "medicine" -> {
                NotificationHelper.showMedicineNotification(
                    context,
                    "💊 Medicine Time!",
                    "Time to take $name — $extra",
                    id
                )
            }
            "food" -> {
                NotificationHelper.showFoodNotification(
                    context,
                    "🍽️ Meal Time!",
                    "$extra meal: $name",
                    id
                )
            }
        }
    }
}