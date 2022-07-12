package com.pti.sheldons_schedule.util

import java.text.SimpleDateFormat
import java.util.*

fun String.toCalendar(): Calendar {
    val formatter = SimpleDateFormat(Constants.DATE_FORMAT_ISO_8601, Locale.getDefault())
    val parsedDate = formatter.parse(this)
    return Calendar.getInstance().apply {
        if (parsedDate != null) time = parsedDate
    }
}