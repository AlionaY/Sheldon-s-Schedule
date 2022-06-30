package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.Event

interface EventRepository {

    suspend fun getAllEvents(): List<Event>

    suspend fun saveEvent(event: Event)

    fun getEvent(id: String): Event

    suspend fun editEvent(event: Event)

    suspend fun deleteEvent(event: Event)
}