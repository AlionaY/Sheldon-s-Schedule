package com.pti.sheldons_schedule.data

import androidx.room.*
import com.pti.sheldons_schedule.data.Options.*
import com.pti.sheldons_schedule.db.OptionsTypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "EventTable")
@TypeConverters(OptionsTypeConverter::class)
data class Event(
    @PrimaryKey(autoGenerate = false)
    val creationDate: String,
    val title: String = "",
    val description: String = "",
    val startDate: String,
    val endDate: String,
    val duration: Long? = null,
    @SerialName("repeat")
    val repeat: Repeat? = null,
    @SerialName("priority")
    val priority: Priority = Priority.Low,
    @SerialName("reminder")
    val reminder: Reminder? = null
)