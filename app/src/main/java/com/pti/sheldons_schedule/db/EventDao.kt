package com.pti.sheldons_schedule.db

import androidx.room.*
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.EventWithToDoList
import com.pti.sheldons_schedule.data.ToDo

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): List<EventWithToDoList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoList(toDoList: List<ToDo>)

    @Query("SELECT * FROM event_table WHERE creationDate=:id ")
    suspend fun getEvent(id: Long): EventWithToDoList

    @Update
    fun editEvent(event: Event)

    @Update
    fun editToDoList(toDoList: List<ToDo>)

    @Delete
    fun deleteEvent(event: Event)

    @Delete
    fun deleteToDoList(toDoList: List<ToDo>)
}