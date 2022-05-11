package com.pti.sheldons_schedule.util

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formatDate(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.US)
    return formatter.format(this.time)
}