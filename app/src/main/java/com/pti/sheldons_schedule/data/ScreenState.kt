package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class ScreenState(
    val currentDate: Calendar = Calendar.getInstance(),
    val startDate: Calendar,
    val endDate: Calendar,
    val title: String = "",
    val description: String = "",
    val options: List<Options>? = null,
    val remind: Options.Reminder = Options.Reminder.DontRemind,
    val repeat: Options.Repeat = Options.Repeat.DontRepeat,
    val priority: Options.Priority = Options.Priority.Low,
    val datePickerStartDate: Long = Calendar.getInstance().timeInMillis,
    val titleErrorText: String? = null,
    val titleFieldState: TitleFieldState = TitleFieldState.Normal,
    val isPickedTimeValid: Boolean = true,
    val pickedStartTime: Calendar,
    val pickedEndTime: Calendar
) {
    val formattedStartTime = startDate.formatDate(Constants.TIME_FORMAT)
    val formattedEndTime = endDate.formatDate(Constants.TIME_FORMAT)
    val formattedStartDate = startDate.formatDate(Constants.DATE_FORMAT)
    val formattedEndDate = endDate.formatDate(Constants.DATE_FORMAT)
    val startDateISO = startDate.formatDate(Constants.DATE_FORMAT_ISO_8601)
    val endDateISO = endDate.formatDate(Constants.DATE_FORMAT_ISO_8601)
}
