package com.pti.sheldons_schedule.util

import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.EventWithReminder
import com.pti.sheldons_schedule.data.Reminder

fun Event.toEventWithReminder(reminder: String) = EventWithReminder(
    event = this,
    remind = Reminder(this.creationDate, reminder)
)