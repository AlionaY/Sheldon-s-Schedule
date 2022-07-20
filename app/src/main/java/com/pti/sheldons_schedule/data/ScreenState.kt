package com.pti.sheldons_schedule.data

import com.pti.sheldons_schedule.data.Options.*
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.TIME_FORMAT
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

data class ScreenState(
    val calendar: Calendar = Calendar.getInstance(),
    val startDate: Calendar,
    val endDate: Calendar,
    val title: String = "",
    val description: String = "",
    val options: List<Options>? = null,
    val remind: Remind = Remind.DontRemind,
    val repeat: Repeat = Repeat.DontRepeat,
    val priority: Priority = Priority.Low,
    val datePickerStartDate: Long = Calendar.getInstance().timeInMillis,
    val titleErrorText: String? = null,
    val titleFieldState: TitleFieldState = TitleFieldState.Normal,
    val isPickedTimeValid: Boolean = true,
    val pickedStartTime: Calendar,
    val pickedEndTime: Calendar,
    val toDoList: List<ToDo> = emptyList()
) {
    val startDateISO = startDate.formatDate(DATE_FORMAT_ISO_8601)
    val endDateISO = endDate.formatDate(DATE_FORMAT_ISO_8601)
    val formattedStartDate: String = startDate.formatDate(DATE_FORMAT)
    val formattedEndDate: String = endDate.formatDate(DATE_FORMAT)
    val formattedStartTime: String = startDate.formatDate(TIME_FORMAT)
    val formattedEndTime: String = endDate.formatDate(TIME_FORMAT)
}

fun ScreenState.toEvent(creationDate: Long, duration: Long) = Event(
    creationDate = creationDate,
    title = this.title,
    description = this.description,
    startDate = this.startDateISO,
    endDate = this.endDateISO,
    duration = duration,
    repeat = this.repeat,
    priority = this.priority
)