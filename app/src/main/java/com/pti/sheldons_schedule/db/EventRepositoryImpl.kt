package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.EventWithToDoList
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents() = eventDao.getAllEvents()

    override suspend fun saveEvent(event: EventWithToDoList) {
        eventDao.insertEvent(event.event)
        eventDao.insertToDoList(event.toDoList)
    }

    override suspend fun getEvent(id: Long) = eventDao.getEvent(id)

    override suspend fun editEvent(event: EventWithToDoList) {
        eventDao.editEvent(event.event)
        eventDao.editToDoList(event.toDoList)

    }

    override suspend fun deleteEvent(event: EventWithToDoList) {
        eventDao.deleteEvent(event.event)
        eventDao.deleteToDoList(event.toDoList)
    }
}