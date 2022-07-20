package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "todo_list_table")
data class ToDo(
    @PrimaryKey
    val eventId: Long,
    val title: String,
    val completed: Boolean = false
)