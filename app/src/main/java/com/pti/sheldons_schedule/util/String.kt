package com.pti.sheldons_schedule.util

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.ToDo
import java.text.SimpleDateFormat
import java.util.*

fun String.toCalendar(): Calendar {
    val formatter = SimpleDateFormat(Constants.DATE_FORMAT_ISO_8601, Locale.getDefault())
    val parsedDate = formatter.parse(this)
    return Calendar.getInstance().apply {
        if (parsedDate != null) time = parsedDate
    }
}

fun String?.toTextFieldValue() = TextFieldValue(
    text = this.orEmpty(),
    selection = TextRange(this.orEmpty().length)
)

//todo: update creation date before save to db
fun String.toToDo(screenState: ScreenState, completed: Boolean = false) = ToDo(
    title = this,
    completed = completed,
    eventId = screenState.calendar.timeInMillis
)