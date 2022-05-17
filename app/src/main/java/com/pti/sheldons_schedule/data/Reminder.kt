package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.R
import kotlinx.serialization.Serializable

@Serializable
enum class Reminder(val alias: Int, val millis: Long) {
    Remind10Min(alias = R.string.remind_10_minutes, millis = 600000),
    Remind15Min(alias = R.string.remind_15_min_before, millis = 900000),
    Remind30Min(alias = R.string.remind_30_min_before, millis = 1800000),
    Remind1Hour(alias = R.string.remind_1_hour_before, millis = 3600000)
}