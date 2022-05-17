package com.pti.sheldons_schedule.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "EventTable")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "creationDate")
    val creationDate: String,
    @ColumnInfo(name = "startDate")
    val startDate: String,
    @ColumnInfo(name = "endDate")
    val endDate: String,
    @ColumnInfo(name = "duration")
    val duration: Long? = null,
    @ColumnInfo(name = "repeat")
    val repeat: Repeat? = null,
    @ColumnInfo(name = "priority")
    val priority: Priority = Priority.Low,
    @ColumnInfo(name = "reminder")
    val reminder: Reminder? = null
)