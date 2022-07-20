package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.FullEvent

interface EventRepository {

    suspend fun getAllEvents(): List<FullEvent>

    suspend fun saveEvent(event: FullEvent)

    suspend fun getEvent(id: Long): FullEvent

    suspend fun editEvent(event: FullEvent)

    suspend fun deleteEvent(event: FullEvent)
}