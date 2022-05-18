package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.R
import kotlinx.serialization.Serializable

@Serializable
enum class Repeat(val alias: Int) : Nameable {
    Daily(R.string.repeat_daily),
    WeekDay(R.string.repeat_week_day),
    Weekly(R.string.repeat_weekly),
    Monthly(R.string.repeat_monthly),
    Annually(R.string.repeat_annually),
    Custom(R.string.repeat_custom);

    override fun getKey(): Int = alias
}
