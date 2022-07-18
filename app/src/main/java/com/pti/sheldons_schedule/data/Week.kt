package com.pti.sheldons_schedule.data

data class DayOfWeek(
    val dayOfMonth: Int,
    val dayName: String,
    val isCurrent: Boolean = false,
    val day: String
)

data class EventsOfDay(
    val day: DayOfWeek,
    val events: List<EventWithReminder> = emptyList()
)

data class Week(
    val week: List<EventsOfDay>,
)