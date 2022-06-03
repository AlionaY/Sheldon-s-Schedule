package com.pti.sheldons_schedule.util

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formatDate(format: String): String {
    this.firstDayOfWeek = Calendar.MONDAY
    val formatter = SimpleDateFormat(format, Locale.UK)
    return formatter.format(this.time)
}