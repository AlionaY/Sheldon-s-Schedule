package com.pti.sheldons_schedule.util

import com.pti.sheldons_schedule.data.TimeState
import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formatDate(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.US)
    return formatter.format(this.time)
}

fun TimeState.formatTime(format: String) = String.format(format, hour, minutes)