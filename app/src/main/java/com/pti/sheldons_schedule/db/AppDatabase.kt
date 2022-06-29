package com.pti.sheldons_schedule.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pti.sheldons_schedule.data.Event

@Database(entities = [Event::class], version = 6, exportSchema = false)
@TypeConverters(OptionsTypeConverter::class)
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