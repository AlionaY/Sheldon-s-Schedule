package com.pti.sheldons_schedule.data

import kotlinx.serialization.Serializable

@Serializable
enum class Repeat {
    None,
    Daily,
    WeekADay,
    Weekly,
    Monthly,
    Annually,
    Custom
}
