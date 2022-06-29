package com.pti.sheldons_schedule.service

import android.app.AlarmManager
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
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.EVENT
import com.pti.sheldons_schedule.util.Constants.FROM
import com.pti.sheldons_schedule.util.Constants.NOTIFICATION
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationController @Inject constructor(
    @ApplicationContext val context: Context?
) {

    companion object {
        const val CHANNEL_NAME = "Events notification channel"
        const val NOTIFICATION_DESCRIPTION = "Notification description"
    }


    private fun createNotificationChannel(id: Long) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id.toString(), CHANNEL_NAME, importance).apply {
            description = NOTIFICATION_DESCRIPTION
        }

        val notificationManager = context?.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }

    fun createNotification(event: Event?) {
        context?.let {
            createNotificationChannel(event?.creationDate ?: 0)

            val intent = Intent(it, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(REMINDER_ID, event?.creationDate ?: 0)
                putExtra(EVENT, event)
                putExtra(FROM, NOTIFICATION)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                event?.creationDate?.toInt() ?: 0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val content = "${event?.startTime}  ${event?.description.orEmpty()}"

            val builder = NotificationCompat.Builder(it, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_schedule_24)
                    .setContentTitle(event?.title.orEmpty())
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)


            with(NotificationManagerCompat.from(it)) {
                notify(event?.creationDate?.toInt() ?: 0, builder.build())
            }
        }
    }
}