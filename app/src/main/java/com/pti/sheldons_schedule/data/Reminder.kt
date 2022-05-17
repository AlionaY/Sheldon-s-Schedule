package com.pti.sheldons_schedule.data

import kotlinx.serialization.Serializable

@Serializable
enum class Reminder(val millis: Long) {
    Remind10Min(600000),
    Remind15Min(900000),
    Remind30Min(1800000),
    Remind1Hour(3600000)
}