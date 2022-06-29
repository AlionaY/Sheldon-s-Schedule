package com.pti.sheldons_schedule.db

import androidx.room.*
import com.pti.sheldons_schedule.data.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM EventTable")
    suspend fun getAllEvents(): List<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Query("SELECT * FROM EventTable WHERE creationDate=:id ")
    fun getEvent(id: String): Event
}