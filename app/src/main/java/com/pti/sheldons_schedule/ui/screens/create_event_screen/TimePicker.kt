package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.ui.theme.Steel

@Composable
fun TimePickerField(
    currentTime: String,
    onTimePicked: (hour: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
    textPadding: Int = 10
) {

    var isClicked by remember { mutableStateOf(false) }

    if (isClicked) {
        TimePicker(onTimePicked)
        isClicked = false
    }

    Text(
        text = currentTime,
        fontSize = 16.sp,
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = Steel,
                shape = RoundedCornerShape(10)
            )
            .clickable { isClicked = true }
            .padding(horizontal = textPadding.dp)
            .wrapContentSize(),
        color = Black,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TimePicker(onTimePicked: (hour: Int, minute: Int) -> Unit) {
    val activity = LocalContext.current as? AppCompatActivity

    MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setTitleText(stringResource(id = R.string.time_picker_title))
        .build()
        .apply {
            activity?.supportFragmentManager?.let { show(it, this.toString()) }
            addOnPositiveButtonClickListener {
                onTimePicked(hour, minute)
            }
        }
}