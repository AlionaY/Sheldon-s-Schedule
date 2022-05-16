package com.pti.sheldons_schedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String
//    todo: add params
)
