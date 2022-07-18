package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "reminder_table")
data class Reminder(
    @PrimaryKey(autoGenerate = false)
    val eventId: Long,
    val remind: String
)