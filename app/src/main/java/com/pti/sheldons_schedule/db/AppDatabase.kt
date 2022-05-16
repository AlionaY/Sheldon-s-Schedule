package com.pti.sheldons_schedule.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pti.sheldons_schedule.data.Event

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}