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
    val repeat: Repeat = Repeat.DontRepeat,
    @SerialName("priority")
    val priority: Priority = Priority.Low
)