package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.formatTime
import java.util.*

data class CreateEventScreenState(
    val startDate: Calendar,
    val startTime: TimeState? = null,
    val endTime: TimeState? = null
) {
    val formattedStartDate: String = startDate.formatTime(DATE_FORMAT)
    val formattedStartTime: String = startTime?.formatTime().orEmpty()
    val formattedEndTime: String = endTime?.formatTime().orEmpty()
}