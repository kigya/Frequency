package com.example.frequency

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FrequencyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.common_app_noti_channel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(FREQUENCY_CHANNEL, name, importance).apply {
                description = descriptionText
                setSound(null, null)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        @JvmStatic
        val FREQUENCY_CHANNEL = "FREQUENCY_CHANNEL"

        @JvmStatic
        val FREQUENCY_MUSIC_CHANNEL = "FREQUENCY_MUSIC_CHANNEL"

        @JvmStatic
        val ACTION_PLAY = "A_PLAY"

        @JvmStatic
        val ACTION_STOP = "A_STOP"

        @JvmStatic
        val ACTION_PAUSE = "A_PAUSE"
    }

}