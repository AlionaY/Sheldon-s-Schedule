package com.pti.sheldons_schedule.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pti.sheldons_schedule.data.Options.*
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "EventTable")
data class Event(
    @PrimaryKey(autoGenerate = false)
    val creationDate: String,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "startDate")
    val startDate: String,
    @ColumnInfo(name = "endDate")
    val endDate: String,
    @ColumnInfo(name = "duration")
    val duration: Long? = null,
    @ColumnInfo(name = "repeat")
    val repeat: String? = null,
    @ColumnInfo(name = "priority")
    val priority: String = Priority.Low.alias,
    @ColumnInfo(name = "reminder")
    val reminder: String? = null
)