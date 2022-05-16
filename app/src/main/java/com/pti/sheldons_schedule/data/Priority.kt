package com.pti.sheldons_schedule.data

import kotlinx.serialization.Serializable

@Serializable
enum class Priority(val value: Int) {
    Low(1),
    Medium(2),
    High(3)
}