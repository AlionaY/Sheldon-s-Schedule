package com.pti.sheldons_schedule.db

import androidx.room.*
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.FullEvent
import com.pti.sheldons_schedule.data.ToDoList

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): List<FullEvent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoList(toDoList: ToDoList)

    @Query("SELECT * FROM event_table WHERE creationDate=:id ")
    suspend fun getEvent(id: Long): FullEvent

    @Update
    fun editEvent(event: Event)

    @Update
    fun editToDoList(toDoList: ToDoList)

    @Delete
    fun deleteEvent(event: Event)

    @Delete
    fun deleteToDoList(toDoList: ToDoList)
}