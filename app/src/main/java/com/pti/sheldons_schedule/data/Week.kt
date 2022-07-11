package com.pti.sheldons_schedule.data

data class Week(
    val week: List<DayOfWeek>
)

data class DayOfWeek(
    val dayOfMonth: Int,
    val weekDayName: String,
    val isCurrent: Boolean = false
)