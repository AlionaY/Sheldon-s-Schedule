package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "todo_list_table",
    foreignKeys = [ForeignKey(
        entity = Event::class,
        parentColumns = ["creationDate"],
        childColumns = ["eventId"]
    )]
)
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0,
    val eventId: Long,
    val title: String,
    val completed: Boolean = false
)