package com.pti.sheldons_schedule.util

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import java.text.SimpleDateFormat
import java.util.*

fun String.convertToCalendar(): Calendar {
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