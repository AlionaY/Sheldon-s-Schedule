package com.pti.sheldons_schedule.db

import androidx.room.*
import com.pti.sheldons_schedule.data.*

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): List<FullEvent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(remind: Reminder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoList(list: List<ToDo>)

    @Query("SELECT * FROM event_table WHERE creationDate = :id")
    suspend fun getEvent(id: Long): FullEvent

    @Update
    fun editEvent(event: Event)

    @Update
    fun editReminder(remind: Reminder)

    @Update
    fun editToDoList(list: List<ToDo>)

    @Delete
    fun deleteEvent(event: Event)

    @Query("DELETE FROM reminder_table WHERE eventId = :eventId")
    fun deleteReminder(eventId: Long)

    @Query("DELETE FROM todo_list_table WHERE eventId = :eventId")
    fun deleteToDoList(eventId: Long)

    @Delete
    fun deleteToDoItem(item: ToDo)
}