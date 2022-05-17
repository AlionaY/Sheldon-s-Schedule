package com.pti.sheldons_schedule.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pti.sheldons_schedule.data.Event

@Database(entities = [Event::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}
// todo: add migrations
fun createDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "eventDatabase.db"
    )
        .fallbackToDestructiveMigration()
        .build()
}