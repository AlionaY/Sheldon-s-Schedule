package com.pti.sheldons_schedule.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Week(
    val week: List<DayOfWeek>
) : Parcelable

@Parcelize
data class DayOfWeek(
    val dayOfMonth: Int,
    val weekDayName: String,
    val isCurrent: Boolean = false,
    val day: String
) : Parcelable

@Parcelize
data class WeekEvents(
    val week: List<DayOfWeek>,
    val events: List<Event>
): Parcelable