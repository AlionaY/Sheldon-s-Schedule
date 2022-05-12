package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import com.pti.sheldons_schedule.util.formatTime
import java.util.*

data class CreateEventScreenState(
    val startDate: Calendar,
    val startTime: TimeState,
    val endTime: TimeState
) {
    val formattedStartDate: String = startDate.formatDate(DATE_FORMAT)
    val formattedStartTime: String = startTime.formatTime(TIME_FORMAT)
    val formattedEndTime: String = endTime.formatTime(TIME_FORMAT)
}