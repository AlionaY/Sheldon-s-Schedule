package com.pti.sheldons_schedule.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
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
import com.pti.sheldons_schedule.util.Constants.EVENT
import com.pti.sheldons_schedule.util.Constants.FROM
import com.pti.sheldons_schedule.util.Constants.NOTIFICATION
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
            repeatEvent(event)

            val intent = Intent(it, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
        val dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val date = LocalDate.parse(event?.startDate, dateFormatter)
        val parsedTime = event?.startTime?.split(":")

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, date.year)
            set(Calendar.MONTH, date.month.value)
            set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, parsedTime?.get(0)?.toInt() ?: 0)
            set(Calendar.MINUTE, parsedTime?.get(1)?.toInt() ?: 0)

        }
        val repeatTime = event?.repeat?.let { getRepeatTimeInMillis(it, calendar) } ?: 0

        if (repeatTime != 0L) {
            alarmManager?.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, repeatTime, pendingIntent
            )
        }
    }

    private fun getRepeatTimeInMillis(repeat: Options.Repeat, calendar: Calendar) =
        when (repeat) {
//            todo: set right time to repeat
            Daily -> AlarmManager.INTERVAL_DAY
            WeekDay -> AlarmManager.INTERVAL_DAY * 5 /* будні */
            Weekly -> (calendar.clone() as Calendar).apply {
                add(Calendar.WEEK_OF_YEAR, 1)
            }.timeInMillis
            Monthly -> (calendar.clone() as Calendar).apply {
                add(Calendar.MONTH, 1)
            }.timeInMillis
            Annually -> (calendar.clone() as Calendar).apply {
                add(Calendar.YEAR, 1)
            }.timeInMillis
            Custom -> 0L
            DontRepeat -> 0L
        }
}