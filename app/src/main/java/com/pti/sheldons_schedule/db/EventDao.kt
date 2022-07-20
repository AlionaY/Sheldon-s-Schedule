package com.pti.sheldons_schedule.db

import androidx.room.*
import com.pti.sheldons_schedule.data.*

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): List<EventWithReminder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(remind: Reminder)

    @Query("SELECT * FROM event_table WHERE creationDate = :id")
    suspend fun getEvent(id: Long): EventWithReminder

    @Update
    fun editEvent(event: Event)

    @Update
    fun editReminder(remind: Reminder)

    @Delete
    fun deleteEvent(event: Event)

    @Delete
    fun deleteReminder(reminder: Reminder)
}