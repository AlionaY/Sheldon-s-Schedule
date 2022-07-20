package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.EventWithToDoList

interface EventRepository {

    suspend fun getAllEvents(): List<EventWithToDoList>

    suspend fun saveEvent(event: EventWithToDoList)

    suspend fun getEvent(id: Long): EventWithToDoList

    suspend fun editEvent(event: EventWithToDoList)

    suspend fun deleteEvent(event: EventWithToDoList)
}