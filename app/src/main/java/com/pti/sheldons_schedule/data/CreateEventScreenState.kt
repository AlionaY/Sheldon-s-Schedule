package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class CreateEventScreenState(
    val startDate: Calendar,
    val startTime: Calendar,
    val endTime: Calendar
) {
    val formattedStartDate: String = startDate.formatDate(DATE_FORMAT)
    val formattedStartTime: String = startTime.formatDate(TIME_FORMAT)
    val formattedEndTime: String = endTime.formatDate(TIME_FORMAT)
}