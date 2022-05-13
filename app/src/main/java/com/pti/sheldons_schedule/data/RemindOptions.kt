package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.R

enum class RemindOptions(val stringRes: Int) {
    Remind10Min(R.string.remind_10_minutes),
    Remind1Hour(R.string.remind_1_hour_before),
    Remind1Day(R.string.remind_1_day_before)
}