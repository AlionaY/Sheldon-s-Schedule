package com.pti.sheldons_schedule.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.data.Options.Repeat
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationController: NotificationController

    @Inject
    lateinit var repository: EventRepository

    private var nextEvent : Calendar? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val eventId = intent?.getLongExtra(REMINDER_ID, 0)
        CoroutineScope(IO).launch {
            val event = eventId?.let { repository.getEvent(it) }

            notificationController.createNotification(event)
            repeatEvent(event, context)
        }
    }

    private fun repeatEvent(event: Event?, context: Context?) {
        val alarmManager = context?.getSystemService<AlarmManager>()
        val reminderIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REMINDER_ID, event?.creationDate)
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
            Log.d("$$$", "picked date ${pickedDate.formatDate(DATE_FORMAT_ISO_8601)}, " +
                    "next ${nextEventDate?.formatDate(DATE_FORMAT_ISO_8601)}, " +
                    "next event ${nextEvent?.formatDate(DATE_FORMAT_ISO_8601)}")

            val remindAt = nextEventDate?.timeInMillis?.minus(
                TimeUnit.MINUTES.toMillis(
                    event.reminder?.value ?: 0
                )
            ) ?: 0

            alarmManager?.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, remindAt, pendingIntent
            )
            nextEvent = nextEventDate
        }
    }

    private fun setAlarmManager(
        context: Context?,
        event: Event?,
        remindAt: Long
    ) {
        val alarmManager = context?.getSystemService<AlarmManager>()
        val reminderIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REMINDER_ID, event?.creationDate)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event?.creationDate?.toInt() ?: 0,
            reminderIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, remindAt, pendingIntent
        )
    }

    private fun getNextEventDate(
        event: Options.Repeat,
        date: Calendar?
    ): Calendar? {
        Log.d("$$$", "next event ${date?.formatDate(DATE_FORMAT_ISO_8601)}")
        return when (event) {
            Repeat.Daily -> (date?.clone() as Calendar).apply {
                add(DAY_OF_MONTH, 1)
            }

            Repeat.Weekday -> date?.let { getWeekdayRepeatTime(it) }

            Repeat.Weekly -> (date?.clone() as Calendar).apply {
                add(WEEK_OF_YEAR, 1)
            }

            Repeat.Monthly -> (date?.clone() as Calendar).apply {
                add(MONTH, 1)
            }

            Repeat.Annually -> (date?.clone() as Calendar).apply {
                add(YEAR, 1)
            }

            else -> null
        }
    }

    private fun getWeekdayRepeatTime(pickedDate: Calendar): Calendar {
        val offset = when (pickedDate.get(DAY_OF_WEEK)) {
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY -> 1
            FRIDAY -> 3
            SATURDAY -> 2
            SUNDAY -> 1
            else -> 0
        }

        return (pickedDate.clone() as Calendar).apply {
            add(DAY_OF_MONTH, offset)
//            add(MINUTE, 2)
        }
    }
}