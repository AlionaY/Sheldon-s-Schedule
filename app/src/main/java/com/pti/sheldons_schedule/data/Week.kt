package com.pti.sheldons_schedule.data

data class DayOfWeek(
    val dayOfMonth: Int,
    val weekDayName: String,
    val isCurrent: Boolean = false,
    val day: String
)

data class WeekEvents(
    val week: List<DayOfWeek>,
    val events: List<Event>
)