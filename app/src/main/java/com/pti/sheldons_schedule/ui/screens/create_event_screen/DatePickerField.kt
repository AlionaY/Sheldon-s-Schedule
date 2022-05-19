package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.datepicker.MaterialDatePicker
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.ui.theme.Steel
import com.pti.sheldons_schedule.util.Constants
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun DatePickerField(
    pickedDate: String?,
    onPickedDate: (Calendar?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isClicked by remember { mutableStateOf(false) }
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
    val icon: ImageVector = Icons.Filled.DateRange

    if (isClicked) {
        DatePicker(onPickedDate)
        isClicked = false
    }

    OutlinedTextField(
        value = LocalDate.now().format(formatter),
        onValueChange = {  },
        modifier = modifier
//            .onFocusChanged { isClicked = it.isFocused }
            .clickable { isClicked = true },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.clickable { isClicked = true }
            )
        }
    )
}

@Composable
private fun DatePicker(onPickedDate: (Calendar?) -> Unit) {
    val activity = LocalContext.current as? AppCompatActivity

    MaterialDatePicker.Builder.datePicker()
        .setTitleText(stringResource(id = R.string.calendar_title))
        .build()
        .apply {
            activity?.supportFragmentManager?.let { show(it, this.toString()) }
            addOnPositiveButtonClickListener {
                Calendar.getInstance().apply {
                    this.timeInMillis = it
                    onPickedDate(this)
                }
            }
        }
}