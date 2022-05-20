package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
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

//todo: calculate duration
fun CreateEventScreenState.toEvent(creationDate: String, duration: Long? = null) = Event(
    creationDate = creationDate,
    title = this.title,
    description = this.description,
    startDate = this.formattedStartDate,
    endDate = this.formattedEndDate,
    duration = duration,
    repeat = this.repeat,
    priority = this.priority,
    reminder = this.remind
)