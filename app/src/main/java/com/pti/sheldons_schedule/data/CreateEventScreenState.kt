package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.data.Options.*
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class CreateEventScreenState(
    val startDate: Calendar,
    val endDate: Calendar,
    val title: String = "",
    val description: String = "",
    val options: List<Options>? = null,
    val remind: Reminder? = null,
    val repeat: Repeat? = null,
    val priority: Priority = Priority.Low,
    val datePickerStartDate: Long = Calendar.getInstance().timeInMillis
) {
    val formattedStartDate: String = startDate.formatDate(DATE_FORMAT)
    val formattedEndDate: String = endDate.formatDate(DATE_FORMAT)
    val formattedStartTime: String = startDate.formatDate(TIME_FORMAT)
    val formattedEndTime: String = endDate.formatDate(TIME_FORMAT)
}