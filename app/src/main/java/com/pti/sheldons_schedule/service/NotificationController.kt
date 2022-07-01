package com.pti.sheldons_schedule.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.pti.sheldons_schedule.MainActivity
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.data.Options.Repeat.*
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.EVENT
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationController @Inject constructor(
    @ApplicationContext val context: Context?
) {

    companion object {
        const val CHANNEL_NAME = "Events notification channel"
        const val NOTIFICATION_DESCRIPTION = "Notification description"
    }

    private var nextEvent : Calendar? = null

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
            repeatEvent(event)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(EVENT, event)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                event?.creationDate?.toInt() ?: 0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val dateFormat = SimpleDateFormat(DATE_FORMAT_ISO_8601, Locale.UK)
            val parsedDate = dateFormat.parse(event?.startDate.orEmpty())
            parsedDate?.let {
                val pickedDate = Calendar.getInstance().apply {
                    time = parsedDate
                }
                val content = pickedDate.formatDate(TIME_FORMAT) + event?.description.orEmpty()

                val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_schedule_24)
                    .setContentTitle(event?.title.orEmpty())
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)


                with(NotificationManagerCompat.from(context)) {
                    notify(event?.creationDate?.toInt() ?: 0, builder.build())
                }
            }
        }
    }

    private fun repeatEvent(event: Event?) {
        val alarmManager = context?.getSystemService<AlarmManager>()
        val reminderIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(EVENT, event as Parcelable)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event?.creationDate?.toInt() ?: 0,
            reminderIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val dateFormat = SimpleDateFormat(DATE_FORMAT_ISO_8601, Locale.UK)
        val parsedDate = dateFormat.parse(event?.startDate.orEmpty())
        parsedDate?.let { date ->
            val pickedDate = Calendar.getInstance().apply {
                time = parsedDate
            }
            val nextEventDate = event?.repeat?.let { event ->
                if (nextEvent == null) {
                    getNextEventDate(event, pickedDate)
                } else {
                    nextEvent?.let { getNextEventDate(event, it) }
                }
            }
            val remindAt = nextEventDate?.timeInMillis?.minus(TimeUnit.MINUTES.toMillis(event.reminder?.value ?: 0)) ?: 0

            alarmManager?.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, remindAt, pendingIntent
            )
            nextEvent = nextEventDate
        }
    }

    private fun getNextEventDate(repeat: Options.Repeat, pickedDate: Calendar) =
        when (repeat) {
            Daily -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            Weekday -> getWeekdayRepeatTime(pickedDate)

            Weekly -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.WEEK_OF_YEAR, 1)
            }

            Monthly -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.MONTH, 1)
            }

            Annually -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.YEAR, 1)
            }

            Custom -> pickedDate

            DontRepeat -> pickedDate
        }

    private fun getWeekdayRepeatTime(pickedDate: Calendar) =
        when (pickedDate.get(Calendar.DAY_OF_WEEK)) {
//                    mon, tue, wed, thur
            2, 3, 4, 5 -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, 1)
            }
//                    fri
            6 -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, 3)
            }
//                    sat
            7 -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, 2)
            }
//                    sun
            1 -> (pickedDate.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            else -> pickedDate
        }
}