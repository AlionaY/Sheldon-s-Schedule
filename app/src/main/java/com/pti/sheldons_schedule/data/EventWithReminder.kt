package com.pti.sheldons_schedule.data

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
data class EventWithReminder(
    @Embedded
    val event: Event,
    @Relation(
        parentColumn = "creationDate",
        entityColumn = "eventId"
    )
    val remind: Reminder
)

fun EventWithReminder.toReminder() = Options.Remind.values().first { reminder ->
    reminder.alias == this.remind.remind
}
