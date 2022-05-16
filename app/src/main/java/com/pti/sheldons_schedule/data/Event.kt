package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String = "",
    val creationDate: String,
    val startDate: String,
    val endDate: String,
    val duration: Long? = null,
    val repeat: Repeat,
    val priority: Priority,
    val reminder: Reminder
)