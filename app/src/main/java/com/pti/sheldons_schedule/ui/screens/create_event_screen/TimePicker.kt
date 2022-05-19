package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pti.sheldons_schedule.R

@Composable
fun TimePickerField(
    pickedTime: String,
    onValueChanged: (String) -> Unit,
    onTimePicked: (hour: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null
) {
    var isClicked by remember { mutableStateOf(false) }

    if (isClicked) {
        TimePicker(onTimePicked)
        isClicked = false
    }

    OutlinedTextField(
        value = pickedTime,
        onValueChange = { onValueChanged(it) },
        modifier = modifier
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
        }
    )
}

@Composable
fun TimePicker(onTimePicked: (hour: Int, minute: Int) -> Unit) {
    val activity = LocalContext.current as? AppCompatActivity
    val focusManager = LocalFocusManager.current

    MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setTitleText(stringResource(id = R.string.time_picker_title))
        .build()
        .apply {
            activity?.supportFragmentManager?.let { show(it, this.toString()) }
            addOnPositiveButtonClickListener {
                onTimePicked(hour, minute)
                focusManager.clearFocus()
            }
        }
}