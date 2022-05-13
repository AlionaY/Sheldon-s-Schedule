package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class CreateEventScreenState(
    val startDate: Calendar,
    val endDate: Calendar,
    val bottomSheetType: BottomSheetType = BottomSheetType.None,
    val repeatOptionsList: List<String>,
    val priorityOptionsList: List<String>,
    val remindOptionsList: List<String>
) {
    val formattedStartDate: String = startDate.formatDate(DATE_FORMAT)
    val formattedEndDate: String = endDate.formatDate(DATE_FORMAT)
    val formattedStartTime: String = startDate.formatDate(TIME_FORMAT)
    val formattedEndTime: String = endDate.formatDate(TIME_FORMAT)
}