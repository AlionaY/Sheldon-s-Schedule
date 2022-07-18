package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.EventWithReminder
import com.pti.sheldons_schedule.data.Reminder

interface EventRepository {

    suspend fun getAllEvents(): List<EventWithReminder>

    suspend fun saveEvent(event: Event)

    suspend fun saveReminder(remind: Reminder)

    suspend fun getEvent(id: Long): EventWithReminder

    suspend fun editEvent(event: Event)

    suspend fun editReminder(reminder: Reminder)

    suspend fun deleteEvent(event: Event)

    suspend fun deleteReminder(reminder: Reminder)
}