package com.pti.sheldons_schedule.data

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
data class FullEvent(
    @Embedded
    val event: Event,
    @Relation(
        parentColumn = "creationDate",
        entityColumn = "eventId"
    )
    val toDoList: String
)
