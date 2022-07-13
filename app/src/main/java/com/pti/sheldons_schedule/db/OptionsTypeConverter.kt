package com.pti.sheldons_schedule.db

import androidx.room.TypeConverter
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.data.ToDo

class OptionsTypeConverter {

    @TypeConverter
    fun fromPriority(priority: Options.Priority?): String? = priority?.alias

    @TypeConverter
    fun toPriority(alias: String?): Options.Priority? =
        Options.Priority.values().firstOrNull { it.alias == alias }

    @TypeConverter
    fun fromRepeat(repeat: Options.Repeat?): String? = repeat?.alias

    @TypeConverter
    fun toRepeat(alias: String?): Options.Repeat? =
        Options.Repeat.values().firstOrNull { it.alias == alias }

    @TypeConverter
    fun fromReminder(reminder: Options.Reminder?): String? = reminder?.alias

    @TypeConverter
    fun toReminder(alias: String?): Options.Reminder? =
        Options.Reminder.values().firstOrNull { it.alias == alias }

    @TypeConverter
    fun fromToDoList(toDoList: List<ToDo>): String {
        val stringList = mutableListOf<String>()
        toDoList.forEach {
            stringList.add(it.title)
            stringList.add(it.completed.toString())
        }
        return stringList.joinToString(",")
    }

    @TypeConverter
    fun toToDoList(list: String): List<ToDo> {
        val toDoList = mutableListOf<ToDo>()
        val stringList = list.split(",")
        for (string in stringList.indices step 2) {
            toDoList.add(ToDo(stringList[string], stringList[string + 2].toBoolean()))
        }
        return toDoList
    }
}