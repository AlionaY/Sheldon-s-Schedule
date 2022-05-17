package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.R
import kotlinx.serialization.Serializable

@Serializable
enum class Priority(val alias: Int) {
    Low(R.string.priority_low),
    Medium(R.string.priority_medium),
    High(R.string.priority_high)
}