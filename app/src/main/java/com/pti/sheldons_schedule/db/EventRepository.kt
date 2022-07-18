package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.FullEvent
import com.pti.sheldons_schedule.data.ToDoList

interface EventRepository {

    suspend fun getAllEvents(): List<FullEvent>

    suspend fun saveEvent(event: Event)

//    suspend fun saveToDoList(toDoList: ToDoList)

    suspend fun getEvent(id: Long): FullEvent

    suspend fun editEvent(event: Event)

//    suspend fun editToDoList(toDoList: ToDoList)

    suspend fun deleteEvent(event: Event)

//    suspend fun deleteToDoList(toDoList: ToDoList)
}