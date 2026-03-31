package com.medicare.app

import android.app.Application
import com.medicare.app.notification.NotificationHelper

class MediCareApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannels(this)
    }
}