package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.ToDoList
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents() = eventDao.getAllEvents()

    override suspend fun saveEvent(event: Event) {
        eventDao.insertEvent(event)
    }

//    override suspend fun saveToDoList(toDoList: ToDoList) {
//        eventDao.insertToDoList(toDoList)
//    }

    override suspend fun getEvent(id: Long) = eventDao.getEvent(id)

    override suspend fun editEvent(event: Event) {
        eventDao.editEvent(event)
    }

//    override suspend fun editToDoList(toDoList: ToDoList) {
//        eventDao.editToDoList(toDoList)
//    }

    override suspend fun deleteEvent(event: Event) {
        eventDao.deleteEvent(event)
    }

//    override suspend fun deleteToDoList(toDoList: ToDoList) {
//        eventDao.deleteToDoList(toDoList)
//    }
}