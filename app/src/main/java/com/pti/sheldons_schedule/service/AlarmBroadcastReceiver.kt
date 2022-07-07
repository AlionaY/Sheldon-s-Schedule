package com.pti.sheldons_schedule.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Options.Repeat
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants.REMINDER_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import java.util.Calendar.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationController: NotificationController

    @Inject
    lateinit var repository: EventRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val eventId = intent?.getLongExtra(REMINDER_ID, 0)

        CoroutineScope(IO).launch {
            val event = eventId?.let { repository.getEvent(it) }

            notificationController.createNotification(event)
            repeatEvent(event, context)
        }
    }

    private fun repeatEvent(event: Event?, context: Context?) {
        val currentTime = getInstance()
        val nextEventDate = getNextEventDate(event, currentTime)

        setAlarmManager(
            eventId = event?.creationDate,
            remindAt = nextEventDate?.timeInMillis ?: 0,
            context = context
        )
    }

    private fun setAlarmManager(eventId: Long?, remindAt: Long, context: Context?) {
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
        return when (pickedDate.get(DAY_OF_WEEK)) {
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, SUNDAY -> (pickedDate.clone() as Calendar).apply {
                add(DAY_OF_MONTH, 1)
            }
            FRIDAY, SATURDAY -> (pickedDate.clone() as Calendar).apply {
                add(WEEK_OF_YEAR, 1)
                firstDayOfWeek = MONDAY
                set(DAY_OF_WEEK, MONDAY)
            }
            else -> {
                (pickedDate.clone() as Calendar).apply {
                    add(DAY_OF_MONTH, 0)
                }
            }
        }
    }
}