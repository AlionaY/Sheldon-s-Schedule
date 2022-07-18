package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "todo_list_table")
data class ToDoList(
    @PrimaryKey
    val eventId: Long,
    val list: List<String> = emptyList()
)