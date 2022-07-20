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
import com.pti.sheldons_schedule.data.EventWithReminder
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import com.pti.sheldons_schedule.util.toCalendar
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
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

    fun createNotification(event: EventWithReminder?) {
        context?.let { context ->
            createNotificationChannel(event?.event?.creationDate ?: 0)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(REMINDER_ID, event?.event?.creationDate)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                event?.event?.creationDate?.toInt() ?: 0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val parsed = event?.event?.startDate?.toCalendar()
            parsed?.let {
                val pickedDate = Calendar.getInstance().apply {
                    time = parsed.time
                }
                val content = pickedDate.formatDate(TIME_FORMAT) + event.event.description

                val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_schedule_24)
                    .setContentTitle(event.event.title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)


                with(NotificationManagerCompat.from(context)) {
                    notify(event.event.creationDate.toInt(), builder.build())
                }
            }
        }
    }
}