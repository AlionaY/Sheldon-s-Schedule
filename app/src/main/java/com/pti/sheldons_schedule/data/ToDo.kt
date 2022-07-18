package com.pti.sheldons_schedule.data

import kotlinx.serialization.Serializable

@Serializable
data class ToDo(
    val eventId: Long,
    val title: String,
    val completed: Boolean = false
)
