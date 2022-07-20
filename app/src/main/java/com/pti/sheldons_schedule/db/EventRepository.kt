package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.EventWithReminder

interface EventRepository {

    suspend fun getAllEvents(): List<EventWithReminder>

    suspend fun saveEvent(event: EventWithReminder)

    suspend fun getEvent(id: Long): EventWithReminder

    suspend fun editEvent(event: EventWithReminder)

    suspend fun deleteEvent(event: EventWithReminder)
}