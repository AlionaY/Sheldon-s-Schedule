package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.R

enum class Options(val string: List<Int>) {
    None(emptyList<Int>()),
    Repeat(
        listOf(
            R.string.repeat_daily,
            R.string.repeat_week_day,
            R.string.repeat_weekly,
            R.string.repeat_monthly,
            R.string.repeat_annually,
            R.string.repeat_custom
            )
    ),
    Priority(
        listOf(
            R.string.priority_low,
            R.string.priority_medium,
            R.string.priority_high
        )
    ),
    Remind(
        listOf(
            R.string.remind_10_minutes,
            R.string.remind_1_hour_before,
            R.string.remind_1_day_before
        )
    )
}