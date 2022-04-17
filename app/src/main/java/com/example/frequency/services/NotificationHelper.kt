package com.example.frequency.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.frequency.app.FrequencyApplication.Companion.FREQUENCY_MUSIC_CHANNEL
import com.example.frequency.presentation.ui.MainActivity
import com.example.frequency.R

class NotificationHelper(
    private val context: Context,
) {

    private lateinit var remoteView: RemoteViews

    init {
        createNotification()
    }

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

    private fun createNotification(){
        remoteView = RemoteViews(context.packageName, R.layout.station_info_notification)
        remoteView.setImageViewResource(R.id.not_station_icon_iv, R.drawable.ic_audiotrack_24)
        remoteView.setTextViewText(R.id.station_title, "Title of the notification")
        remoteView.setTextViewText(R.id.station_info, "Description of the notification")
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, FREQUENCY_MUSIC_CHANNEL)
            .setCustomContentView(remoteView)
            .setContentTitle(context.getString(R.string.app_name))
            .setSound(null)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_auth_logo)
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