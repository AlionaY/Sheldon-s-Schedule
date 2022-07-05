package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class EditEventScreenState(
    val title: String,
    val currentDate: Calendar = Calendar.getInstance(),
    val startDate: Calendar,
    val endDate: Calendar,
    val description: String,
    val options: List<Options>? = null,
    val remind: Options.Reminder,
    val repeat: Options.Repeat,
    val priority: Options.Priority,
    val datePickerStartDate: Long = Calendar.getInstance().timeInMillis,
    val titleErrorText: String? = null,
    val titleFieldState: TitleFieldState = TitleFieldState.Normal,
    val isPickedTimeValid: Boolean = true,
    val pickedStartTime: Calendar,
    val pickedEndTime: Calendar
) {
    val formattedStartTime = startDate.formatDate(TIME_FORMAT)
    val formattedEndTime = endDate.formatDate(TIME_FORMAT)
    val formattedStartDate = startDate.formatDate(DATE_FORMAT)
    val formattedEndDate = endDate.formatDate(DATE_FORMAT)
    val startDateISO = startDate.formatDate(DATE_FORMAT_ISO_8601)
    val endDateISO = endDate.formatDate(DATE_FORMAT_ISO_8601)
}