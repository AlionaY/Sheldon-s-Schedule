package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class CreateEventScreenState(
    val startDate: Calendar,
    val endDate: Calendar
) {
    val formattedStartDate: String = startDate.formatDate(DATE_FORMAT)
    val formattedEndDate: String = endDate.formatDate(DATE_FORMAT)
}