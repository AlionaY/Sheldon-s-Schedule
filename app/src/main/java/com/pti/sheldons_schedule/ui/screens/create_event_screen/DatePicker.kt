package com.pti.sheldons_schedule.ui.screens.create_event_screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.data.Date

@Composable
fun DatePicker(
    date: Date?,
    onPickedDate: (year: Int, month: Int, day: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isClicked by remember { mutableStateOf(false)}

    if (isClicked) {
        DatePickerDialog(date, onPickedDate)
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
            text = date?.formattedDate.orEmpty(),
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
    datePicked: Date?,
    onPickedDate: (year: Int, month: Int, day: Int) -> Unit
) {
    val context = LocalContext.current

    datePicked?.let {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                onPickedDate(year, month, dayOfMonth)
            },
            it.year,
            it.month,
            it.day
        ).show()
    }
}