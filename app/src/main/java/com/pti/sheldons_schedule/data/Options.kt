package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Options(val title: Int, val nameId: Int) {
    @Serializable
    sealed class Priority(val name: Int, val alias: String) :
        Options(title = R.string.priority, nameId = name) {
        object Low : Priority(name = R.string.priority_low, alias = "low")
        object Medium : Priority(R.string.priority_medium, "medium")
        object High : Priority(R.string.priority_high, "high")

        companion object {
            fun values(): List<Priority> = listOf(Low, Medium, High)
        }
    }

    @Serializable
    sealed class Reminder(val name: Int, val alias: String, val value: Long) :
        Options(R.string.remind, name) {
        object Min10 : Reminder(R.string.remind_10_minutes, "min_10", 10)
        object Min15 : Reminder(R.string.remind_15_min_before, "min_15", 15)
        object Min30 : Reminder(R.string.remind_30_min_before, "min_30", 30)
        object Min60 : Reminder(R.string.remind_1_hour_before, "min_60", 60)
        object DontRemind : Reminder(R.string.dont_remind, "dont_remind", 0)

        companion object {
            fun values(): List<Reminder> = listOf(
                Min10,
                Min15,
                Min30,
                Min60,
                DontRemind
            )
        }
    }

    @Serializable
    sealed class Repeat(val name: Int, val alias: String) : Options(R.string.repeat, name) {
        object Daily : Repeat(R.string.repeat_daily, "daily")
        object Weekday : Repeat(R.string.repeat_week_day, "week_day")
        object Weekly : Repeat(R.string.repeat_weekly, "weekly")
        object Monthly : Repeat(R.string.repeat_monthly, "monthly")
        object Annually : Repeat(R.string.repeat_annually, "annually")
        object Custom : Repeat(R.string.repeat_custom, "custom")
        object DontRepeat : Repeat(R.string.dont_repeat, "dont_repeat")

        companion object {
            fun values(): List<Repeat> = listOf(
                Daily,
                Weekday,
                Weekly,
                Monthly,
                Annually,
                Custom,
                DontRepeat
            )
        }
    }
}