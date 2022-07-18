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
}