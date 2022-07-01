package com.pti.sheldons_schedule.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pti.sheldons_schedule.R
import java.util.*

@Composable
fun TimePickerField(
    pickedTime: String,
    calendar: Calendar,
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
            value = pickedTime,
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
fun TimePicker(calendar: Calendar, onTimePicked: (hour: Int, minute: Int) -> Unit) {
    val activity = LocalContext.current as? AppCompatActivity
    val focusManager = LocalFocusManager.current

    MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setTitleText(stringResource(id = R.string.time_picker_title))
        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
        .setMinute(calendar.get(Calendar.MINUTE))
        .build()
        .apply {
            activity?.supportFragmentManager?.let { show(it, this.toString()) }
            addOnPositiveButtonClickListener {
                onTimePicked(hour, minute)
                focusManager.clearFocus()
            }
        }
}