package com.pti.sheldons_schedule.db

import androidx.room.TypeConverter
import com.pti.sheldons_schedule.data.Options

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
}