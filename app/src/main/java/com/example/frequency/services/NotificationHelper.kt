package com.example.frequency.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.frequency.MainActivity
import com.example.frequency.MainActivity.Companion.FREQUENCY_CHANNEL
import com.example.frequency.R

class NotificationHelper(
    context: Context,

) {

    private val contentIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, FREQUENCY_CHANNEL)
            .setContentTitle(context.getString(R.string.app_name))
            .setSound(null)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_audiotrack_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    fun updateNotification(notificationText: String? = null) {
        notificationText?.let { notificationBuilder.setContentText(it) }
        //notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }


}