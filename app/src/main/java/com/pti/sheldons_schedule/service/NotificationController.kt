package com.pti.sheldons_schedule.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.pti.sheldons_schedule.MainActivity
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationController @Inject constructor(
    @ApplicationContext val context: Context
) {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.notification_channel_name)
        val descriptionText = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }


    fun createNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
//        todo: add title and content to notification
        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_schedule_24)
            .setContentTitle("Title")
            .setContentText("Content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}