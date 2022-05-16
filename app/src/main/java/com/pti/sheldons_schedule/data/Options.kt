package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.R

enum class Options(val title: Int?, val optionsList: List<Int>) {
    Repeat(
        title = R.string.repeat,
        optionsList = listOf(
            R.string.repeat_daily,
            R.string.repeat_week_day,
            R.string.repeat_weekly,
            R.string.repeat_monthly,
            R.string.repeat_annually,
            R.string.repeat_custom
        )
    ),
    Priority(
        title = R.string.priority,
        optionsList = listOf(
            R.string.priority_low,
            R.string.priority_medium,
            R.string.priority_high
        )
    ),
    Remind(
        title = R.string.remind,
        optionsList = listOf(
            R.string.remind_10_minutes,
            R.string.remind_1_hour_before,
            R.string.remind_1_day_before
        )
    )
}