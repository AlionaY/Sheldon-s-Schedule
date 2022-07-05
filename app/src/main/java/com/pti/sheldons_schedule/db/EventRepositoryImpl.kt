package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.Event
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents() = eventDao.getAllEvents()

    override suspend fun saveEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    override suspend fun getEvent(id: Long): Event = eventDao.getEvent(id)

    override suspend fun editEvent(event: Event) {
        eventDao.editEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }
}