package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.google.android.material.datepicker.MaterialDatePicker
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.util.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun DatePickerField(
    pickedDate: String?,
    onPickedDate: (Calendar?) -> Unit,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null
) {
    var isClicked by remember { mutableStateOf(false) }
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)

    if (isClicked) {
        DatePicker(onPickedDate)
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
private fun DatePicker(onPickedDate: (Calendar?) -> Unit) {
    val activity = LocalContext.current as? AppCompatActivity
    val focusManager = LocalFocusManager.current

    MaterialDatePicker.Builder.datePicker()
        .setTitleText(stringResource(id = R.string.calendar_title))
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