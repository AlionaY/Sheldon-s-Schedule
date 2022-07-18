package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.*
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents() = eventDao.getAllEvents()

    override suspend fun saveEvent(event: EventWithReminder) {
        eventDao.insertEvent(event.event)
        eventDao.insertReminder(event.remind)
    }

    override suspend fun getEvent(id: Long): EventWithReminder = eventDao.getEvent(id)

    override suspend fun editEvent(event: EventWithReminder) {
        eventDao.editEvent(event.event)
        eventDao.editReminder(event.remind)

    }

    override suspend fun deleteEvent(event: EventWithReminder) {
        eventDao.deleteEvent(event.event)
        eventDao.deleteReminder(event.remind)
    }
}