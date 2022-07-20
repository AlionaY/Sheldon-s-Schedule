package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.FullEvent
import com.pti.sheldons_schedule.data.ToDo

interface EventRepository {

    suspend fun getAllEvents(): List<FullEvent>

    suspend fun saveEvent(event: FullEvent)

    suspend fun getEvent(id: Long): FullEvent

    suspend fun editEvent(event: FullEvent)

    suspend fun deleteEvent(event: FullEvent)

    suspend fun deleteToDoItem(item: ToDo)
}