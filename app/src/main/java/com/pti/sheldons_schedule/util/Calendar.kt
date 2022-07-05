package com.pti.sheldons_schedule.util

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formatDate(format: String): String {
    this.firstDayOfWeek = Calendar.MONDAY
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(this.time)
}

fun Calendar.updateTime(
    hour: Int,
    minutes: Int,
    minutesToAdd: Int? = null
) = (this.clone() as Calendar).apply {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minutes)
    if (minutesToAdd != null) add(Calendar.MINUTE, minutesToAdd)
}

fun Calendar.updateDate(
    year: Int,
    month: Int,
    dayOfMonth: Int
) = (this.clone() as Calendar).apply {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, dayOfMonth)
}