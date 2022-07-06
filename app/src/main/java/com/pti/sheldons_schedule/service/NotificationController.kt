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
import com.pti.sheldons_schedule.data.Options.Repeat.*
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.convertToCalendar
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationController @Inject constructor(
    @ApplicationContext val context: Context?
) {

    companion object {
        const val CHANNEL_NAME = "Events notification channel"
        const val NOTIFICATION_DESCRIPTION = "Notification description"
    }

    private var nextEvent: Calendar? = null

    private fun createNotificationChannel(id: Long) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id.toString(), CHANNEL_NAME, importance).apply {
            description = NOTIFICATION_DESCRIPTION
        }

        val notificationManager = context?.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }

    fun createNotification(event: Event?) {
        context?.let { context ->
            createNotificationChannel(event?.creationDate ?: 0)
//            repeatEvent(event)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(REMINDER_ID, event?.creationDate)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                event?.creationDate?.toInt() ?: 0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val parsed = event?.startDate?.convertToCalendar()
            parsed?.let {
                val pickedDate = getInstance().apply {
                    time = parsed.time
                }
                val content = pickedDate.formatDate(TIME_FORMAT) + event.description

                val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_schedule_24)
                    .setContentTitle(event.title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)


                with(NotificationManagerCompat.from(context)) {
                    notify(event.creationDate.toInt(), builder.build())
                }
            }
        }
    }

    private fun repeatEvent(event: Event?) {
        val parsed = event?.startDate?.convertToCalendar()
        parsed?.let { date ->
            val pickedDate = getInstance().apply {
                time = date.time
            }
            val remindAt = if (nextEvent == null) {
                getNextEventDate(event, pickedDate)
            } else {
                nextEvent?.let { getNextEventDate(event, it) }
            }

            setAlarmManager(event.creationDate, remindAt ?: 0)

        }
    }

    private fun setAlarmManager(eventId: Long?, remindAt: Long) {
        val alarmManager = context?.getSystemService<AlarmManager>()
        val reminderIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REMINDER_ID, eventId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId?.toInt() ?: 0,
            reminderIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, remindAt, pendingIntent
        )
    }

    private fun getNextEventDate(event: Event?, pickedDate: Calendar): Long {
        val nextEventDate = when (event?.repeat) {
            Daily -> (pickedDate.clone() as Calendar).apply {
                add(DAY_OF_MONTH, 1)
            }

            Weekday -> getWeekdayRepeatTime(pickedDate)

            Weekly -> (pickedDate.clone() as Calendar).apply {
                add(WEEK_OF_YEAR, 1)
            }

            Monthly -> (pickedDate.clone() as Calendar).apply {
                add(MONTH, 1)
            }

            Annually -> (pickedDate.clone() as Calendar).apply {
                add(YEAR, 1)
            }

            else -> null
        }

        nextEvent = nextEventDate

        return nextEvent?.timeInMillis?.minus(
            TimeUnit.MINUTES.toMillis(
                event?.reminder?.value ?: 0
            )
        ) ?: 0
    }

    private fun getWeekdayRepeatTime(pickedDate: Calendar): Calendar {
        val offset = when (pickedDate.get(DAY_OF_WEEK)) {
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, SUNDAY -> 1
            FRIDAY -> 3
            SATURDAY -> 2
            else -> 0
        }

        return (pickedDate.clone() as Calendar).apply {
//            add(DAY_OF_MONTH, offset)
            add(MINUTE, 1)
        }
    }
}