package com.pti.sheldons_schedule.db

import androidx.room.Dao
import androidx.room.Query
import com.pti.sheldons_schedule.data.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getAllEvents(): List<Event>
}