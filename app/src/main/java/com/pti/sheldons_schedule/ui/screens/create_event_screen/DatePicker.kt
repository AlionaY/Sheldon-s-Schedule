package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.datepicker.MaterialDatePicker
import com.pti.sheldons_schedule.R

@Composable
fun DatePicker(
    activity: AppCompatActivity,
    pickedDate: String?,
    onPickedDate: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var isClicked by remember { mutableStateOf(false) }

    if (isClicked) {
        DatePickerDialog(activity, onPickedDate)
        isClicked = false
    }

    Box(
        modifier = modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10)
            )
            .clickable { isClicked = true }
    ) {
        Text(
            text = pickedDate.orEmpty(),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize()
                .align(Alignment.CenterStart),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun DatePickerDialog(
    activity: AppCompatActivity,
    onPickedDate: (Long) -> Unit
) {
    MaterialDatePicker.Builder.datePicker()
        .setTitleText(stringResource(id = R.string.calendar_title))
        .build().apply {
            show(activity.supportFragmentManager, this.toString())
            addOnPositiveButtonClickListener {
                onPickedDate(it)
            }
        }
}