package com.pti.sheldons_schedule.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pti.sheldons_schedule.data.ScreenState
import java.util.*

@Composable
fun TimePickerRow(
    screenState: ScreenState?,
    fieldWidth: Dp,
    onStartTimePicked: (Int, Int) -> Unit,
    onEndTimePicked: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TimePickerField(
            pickedTime = screenState?.formattedStartTime,
            calendar = screenState?.startDate,
            onTimePicked = { hour, minutes ->
                onStartTimePicked(hour, minutes)
            },
            modifier = Modifier
                .padding(start = 15.dp)
                .width(fieldWidth)
                .wrapContentHeight(),
            label = { Text(text = stringResource(id = com.pti.sheldons_schedule.R.string.start_time)) },
            onValueChanged = {}
        )
        Spacer(modifier = Modifier.width(30.dp))
        TimePickerField(
            pickedTime = screenState?.formattedEndTime,
            calendar = screenState?.endDate,
            onTimePicked = { hour, minutes ->
                onEndTimePicked(hour, minutes)
            },
            modifier = Modifier
                .padding(end = 15.dp)
                .width(fieldWidth)
                .wrapContentHeight(),
            label = { Text(text = stringResource(id = com.pti.sheldons_schedule.R.string.end_time)) },
            onValueChanged = {}
        )
    }
}


@Composable
private fun TimePickerField(
    pickedTime: String?,
    calendar: Calendar?,
    onValueChanged: (String) -> Unit,
    onTimePicked: (hour: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    var isClicked by remember { mutableStateOf(false) }

    if (isClicked) {
        TimePicker(calendar = calendar, onTimePicked = onTimePicked)
        isClicked = false
    }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = pickedTime.orEmpty(),
            onValueChange = { onValueChanged(it) },
            modifier = Modifier
                .onFocusChanged { isClicked = it.hasFocus }
                .clickable { isClicked = true },
            readOnly = true,
            label = label,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    modifier = Modifier.clickable { isClicked = true }
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onBackground,
                cursorColor = MaterialTheme.colors.onBackground,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
fun TimePicker(calendar: Calendar?, onTimePicked: (hour: Int, minute: Int) -> Unit) {
    val activity = LocalContext.current as? AppCompatActivity
    val focusManager = LocalFocusManager.current

    MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setHour(calendar?.get(Calendar.HOUR_OF_DAY) ?: 0)
        .setMinute(calendar?.get(Calendar.MINUTE) ?: 0)
        .build()
        .apply {
            activity?.supportFragmentManager?.let { show(it, this.toString()) }
            addOnPositiveButtonClickListener {
                onTimePicked(hour, minute)
                focusManager.clearFocus()
            }
        }
}