package com.pti.sheldons_schedule.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Options.Repeat
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import com.pti.sheldons_schedule.util.convertToCalendar
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
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

    override fun onReceive(context: Context?, intent: Intent?) {
        val eventId = intent?.getLongExtra(REMINDER_ID, 0)
        val nextEventDate = intent?.getLongExtra("next_event", 0)

        CoroutineScope(IO).launch {
            val event = eventId?.let { repository.getEvent(it) }

            notificationController.createNotification(event)
            repeatEvent(event, context, nextEventDate)
        }
    }

    private fun repeatEvent(event: Event?, context: Context?, nextEvent: Long?) {
        val parsed = event?.startDate?.convertToCalendar()
        parsed?.let { date ->
            val pickedDate = getInstance().apply {
                timeInMillis = nextEvent ?: date.timeInMillis
            }
            val nextEventDate = getNextEventDate(event, pickedDate)
            val remindAt = nextEventDate?.timeInMillis?.minus(
                TimeUnit.MINUTES.toMillis(
                    event.reminder?.value ?: 0
                )
            ) ?: 0

            Log.d("$$$", "picked date ${pickedDate.formatDate(DATE_FORMAT_ISO_8601)}, " +
                    "next event date ${nextEventDate?.formatDate(DATE_FORMAT_ISO_8601)}")
                setAlarmManager(
                    eventId = event.creationDate,
                    remindAt = remindAt,
                    context = context,
                    nextEventDate = nextEventDate?.timeInMillis ?: 0
                )
        }
    }

    private fun setAlarmManager(eventId: Long?, remindAt: Long, context: Context?, nextEventDate: Long) {
        Log.d("%%%", "alarm next ${
            getInstance().apply { 
            timeInMillis = nextEventDate
        }.formatDate(DATE_FORMAT_ISO_8601)}")

        val alarmManager = context?.getSystemService<AlarmManager>()
        val reminderIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REMINDER_ID, eventId)
            putExtra("next_event", nextEventDate)
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

    private fun getNextEventDate(event: Event?, pickedDate: Calendar): Calendar? {
        val nextEventDate = when (event?.repeat) {
            Repeat.Daily -> (pickedDate.clone() as Calendar).apply {
                add(DAY_OF_MONTH, 1)
            }

            Repeat.Weekday -> getWeekdayRepeatTime(pickedDate)

            Repeat.Weekly -> (pickedDate.clone() as Calendar).apply {
                add(WEEK_OF_YEAR, 1)
            }

            Repeat.Monthly -> (pickedDate.clone() as Calendar).apply {
                add(MONTH, 1)
            }

            Repeat.Annually -> (pickedDate.clone() as Calendar).apply {
                add(YEAR, 1)
            }

            else -> null
        }

        return nextEventDate
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
            add(MINUTE, 2)
        }
    }
}