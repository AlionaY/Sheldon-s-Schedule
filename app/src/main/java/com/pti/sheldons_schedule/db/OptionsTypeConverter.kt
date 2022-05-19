package com.pti.sheldons_schedule.db

import androidx.room.TypeConverter
import com.pti.sheldons_schedule.data.Options
import org.json.JSONObject

class OptionsTypeConverter {

    @TypeConverter
    fun fromPriority(priority: Options.Priority) = JSONObject().apply {
//        put("name", priority.name)
        put("priority", priority.alias)
    }

    @TypeConverter
    fun toPriority(string: String?): Options.Priority? {
        return Options.Priority.values().firstOrNull { it.alias == string }
    }

    @TypeConverter
    fun fromReminder(remind: Options.Reminder) = JSONObject().apply {
//        put("name", remind.name)
        put("remind", remind.alias)
    }

    @TypeConverter
    fun toReminder(remind: String?): Options.Reminder? {
        return Options.Reminder.values().firstOrNull { it.alias == remind }
    }

    @TypeConverter
    fun fromRepeat(repeat: Options.Repeat) = JSONObject().apply {
//        put("name", repeat.name)
        put("repeat", repeat.alias)
    }

    @TypeConverter
    fun toRepeat(repeat: String?): Options.Repeat? =
        Options.Repeat.values().firstOrNull {
            it.alias == repeat
        }
}