package com.pti.sheldons_schedule.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.pti.sheldons_schedule.MainActivity
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.FROM
import com.pti.sheldons_schedule.util.Constants.NOTIFICATION
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID

class NotificationController : Service() {

    companion object {
        const val CHANNEL_NAME = "Events notification channel"
        const val NOTIFICATION_DESCRIPTION = "Notification description"
    }

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
//        val event = onGetEventId(reminderId)

        createNotification(context = this)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
    }

    private fun createNotificationChannel(id: Long) {
        Log.d("%%%", "notif createNotificationChannel id $id")
        val name = CHANNEL_NAME
        val descriptionText = NOTIFICATION_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id.toString(), name, importance).apply {
            description = descriptionText
        }

        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }

    private fun createNotification(context: Context) {
        createNotificationChannel(0)
//        Log.d("%%%", "notif createNotification id ${event.creationDate}")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(REMINDER_ID, 0)
            putExtra(FROM, NOTIFICATION)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_schedule_24)
            .setContentTitle("event.title")
            .setContentText("Date")
            .setSubText("event.description")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }
}