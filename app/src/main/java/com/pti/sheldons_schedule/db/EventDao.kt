package com.pti.sheldons_schedule.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pti.sheldons_schedule.data.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM EventTable")
    suspend fun getAllEvents(): List<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)
}