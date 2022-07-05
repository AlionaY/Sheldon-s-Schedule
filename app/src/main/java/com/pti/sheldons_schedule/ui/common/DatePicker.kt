package com.pti.sheldons_schedule.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.util.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun DatePickedRow(
    screenState: ScreenState?,
    fieldWidth: Dp,
    onStartDatePicked: (Calendar) -> Unit,
    onEndDatePicked: (Calendar) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DatePickerField(
            pickedDate = screenState?.formattedStartDate,
            onPickedDate = { calendar -> calendar?.let { onStartDatePicked(it) } },
            modifier = Modifier
                .padding(start = 15.dp)
                .width(fieldWidth)
                .wrapContentHeight(),
            label = { Text(stringResource(id = R.string.start_date)) },
            onValueChanged = { }
        )
        Spacer(modifier = Modifier.width(30.dp))
        DatePickerField(
            pickedDate = screenState?.formattedEndDate,
            onPickedDate = { calendar -> calendar?.let { onEndDatePicked(it) } },
            modifier = Modifier
                .padding(end = 15.dp)
                .width(fieldWidth)
                .wrapContentHeight(),
            label = { Text(text = stringResource(id = R.string.end_date)) },
            onValueChanged = { },
            startDate = screenState?.datePickerStartDate
        )
    }
}

@Composable
private fun DatePickerField(
    pickedDate: String?,
    onPickedDate: (Calendar?) -> Unit,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    startDate: Long? = null,
    label: @Composable (() -> Unit)? = null
) {
    var isClicked by remember { mutableStateOf(false) }
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)

    if (isClicked) {
        DatePicker(onPickedDate = onPickedDate, startDate = startDate)
        isClicked = false
    }

    OutlinedTextField(
        value = pickedDate ?: LocalDate.now().format(formatter),
        onValueChange = { onValueChanged(it) },
        modifier = modifier
            .onFocusChanged { isClicked = it.hasFocus }
            .clickable { isClicked = true },
        readOnly = true,
        label = label,
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
                modifier = Modifier.clickable { isClicked = true }
            )
        }
    )
}

@Composable
private fun DatePicker(
    onPickedDate: (Calendar?) -> Unit,
    startDate: Long? = null,
    calendarHeader: String = stringResource(id = R.string.calendar_title)
) {
    val activity = LocalContext.current as? AppCompatActivity
    val focusManager = LocalFocusManager.current
    val validator = if (startDate == null) {
        DateValidatorPointForward.now()
    } else {
        DateValidatorPointForward.from(startDate)
    }

    val constraintsBuilder = CalendarConstraints.Builder()
        .setValidator(validator)
        .build()

    MaterialDatePicker.Builder.datePicker()
        .setCalendarConstraints(constraintsBuilder)
        .setTitleText(calendarHeader)
        .build()
        .apply {
            activity?.supportFragmentManager?.let { show(it, this.toString()) }
            addOnPositiveButtonClickListener {
                Calendar.getInstance().apply {
                    this.timeInMillis = it
                    onPickedDate(this)
                    focusManager.clearFocus()
                }
            }
        }
}