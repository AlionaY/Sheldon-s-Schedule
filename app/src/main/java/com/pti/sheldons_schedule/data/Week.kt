package com.pti.sheldons_schedule.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Week(
    val week: List<DayOfWeekUI>
) : Parcelable

@Parcelize
data class DayOfWeekUI(
    val dayOfMonth: Int,
    val weekDayName: String,
    val isCurrent: Boolean = false
) : Parcelable