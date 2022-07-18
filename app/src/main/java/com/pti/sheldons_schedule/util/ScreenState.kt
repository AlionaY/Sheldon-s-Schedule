package com.pti.sheldons_schedule.util

import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.ScreenState

fun ScreenState.toEvent(creationDate: Long, duration: Long) = Event(
    creationDate = creationDate,
    title = this.title,
    description = this.description,
    startDate = this.startDateISO,
    endDate = this.endDateISO,
    duration = duration,
    repeat = this.repeat,
    priority = this.priority
)