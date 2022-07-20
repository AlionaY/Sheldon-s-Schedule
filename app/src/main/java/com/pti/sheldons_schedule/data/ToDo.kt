package com.pti.sheldons_schedule.data

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class ToDo(
    @PrimaryKey
    val eventId: Long,
    val title: String,
    val completed: Boolean = false
)
