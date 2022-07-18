package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pti.sheldons_schedule.data.Options.Priority
import com.pti.sheldons_schedule.data.Options.Repeat
import com.pti.sheldons_schedule.db.OptionsTypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "event_table")
@TypeConverters(OptionsTypeConverter::class)
data class Event(
    @PrimaryKey(autoGenerate = false)
    val creationDate: Long,
    val title: String = "",
    val description: String = "",
    val startDate: String,
    val endDate: String,
    val duration: Long? = null,
    @SerialName("repeat")
    val repeat: Repeat? = null,
    @SerialName("priority")
    val priority: Priority = Priority.Low
)

fun ScreenState.toEvent(creationDate: Long, duration: Long) = FullEvent(
    event = Event(
        creationDate = creationDate,
        title = this.title,
        description = this.description,
        startDate = this.startDateISO,
        endDate = this.endDateISO,
        duration = duration,
        repeat = this.repeat,
        priority = this.priority
    ),
    toDoList = this.todoList.joinToString(",") { it.title }
)