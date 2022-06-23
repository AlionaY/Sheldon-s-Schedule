package com.pti.sheldons_schedule.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.pti.sheldons_schedule.MainActivity
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.FROM
import com.pti.sheldons_schedule.util.Constants.NOTIFICATION
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationController @Inject constructor(
    @ApplicationContext val context: Context,
    private val onGetEventId: (Long?) -> Event,
) : Service() {

    var isServiceRunning = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        isServiceRunning = true
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val reminderId = intent?.getLongExtra(REMINDER_ID, 0)
        //        todo: get necessary event from db
        val event = onGetEventId(reminderId)
        createNotification(event)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
    }

    private fun createNotificationChannel(id: Long) {
        val name = context.getString(R.string.notification_channel_name)
        val descriptionText = context.getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id.toString(), name, importance).apply {
            description = descriptionText
        }

        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }

    private fun createNotification(event: Event) {
        createNotificationChannel(event.creationDate)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(REMINDER_ID, event.creationDate)
            putExtra(FROM, NOTIFICATION)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_schedule_24)
            .setContentTitle(event.title)
            .setContentText("Date")
            .setSubText(event.description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(event.creationDate.toInt(), builder.build())
        }
    }
}