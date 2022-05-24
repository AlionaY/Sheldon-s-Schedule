package com.pti.sheldons_schedule.data

import java.util.*

data class WeekState(
    val calendar: Calendar,
    val currentDay : Calendar,
    val monday: Calendar = Calendar.getInstance(),
    val tuesday: Calendar = Calendar.getInstance(),
    val wednesday: Calendar = Calendar.getInstance(),
    val thursday: Calendar = Calendar.getInstance(),
    val friday: Calendar = Calendar.getInstance(),
    val saturday: Calendar = Calendar.getInstance(),
    val sunday: Calendar = Calendar.getInstance(),
    val updatedDate: Calendar = Calendar.getInstance()
)