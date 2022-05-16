package com.pti.sheldons_schedule.db

import com.pti.sheldons_schedule.data.Event
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val eventDao: EventDao) : EventRepository {

    override suspend fun getAllEvents() = eventDao.getAllEvents()

    override suspend fun setEvent(event: Event) {
        eventDao.insertEvent(event)
    }
}