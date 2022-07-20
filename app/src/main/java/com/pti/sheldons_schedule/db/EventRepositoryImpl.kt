package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.*
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents() = eventDao.getAllEvents()

    override suspend fun saveEvent(event: FullEvent) {
        eventDao.apply {
            insertEvent(event.event)
            insertReminder(event.reminder)
            insertToDoList(event.toDoList)
        }
    }

    override suspend fun getEvent(id: Long) = eventDao.getEvent(id)

    override suspend fun editEvent(event: FullEvent) {
        eventDao.apply {
            editEvent(event.event)
            editReminder(event.reminder)
            editToDoList(event.toDoList)
        }
    }

    override suspend fun deleteEvent(event: FullEvent) {
        eventDao.apply {
            deleteEvent(event.event)
            deleteReminder(event.event.creationDate)
            deleteToDoList(event.event.creationDate)
        }
    }

    override suspend fun deleteToDoItem(item: ToDo) {
        eventDao.deleteToDoItem(item)
    }
}