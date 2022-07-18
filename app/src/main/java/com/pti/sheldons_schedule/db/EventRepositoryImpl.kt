package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.*
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents() = eventDao.getAllEvents()

    override suspend fun saveEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    override suspend fun saveReminder(remind: Reminder) {
        eventDao.insertReminder(remind)
    }

    override suspend fun getEvent(id: Long): EventWithReminder = eventDao.getEvent(id)

    override suspend fun editEvent(event: Event) {
        eventDao.editEvent(event)
    }

    override suspend fun editReminder(reminder: Reminder) {
        eventDao.editReminder(reminder)
    }

    override suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        eventDao.deleteReminder(reminder)
    }
}