package com.pti.sheldons_schedule.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.TitleFieldState
import java.util.*

@Composable
fun ScreenContent(
    state: ScreenState?,
    focusRequester: FocusRequester,
    fieldWidth: Dp,
    onTitleEdited: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onDescriptionEdited: (String) -> Unit,
    onStartDatePicked: (Calendar) -> Unit,
    onEndDatePicked: (Calendar) -> Unit,
    onTimeStartPicked: (Int, Int) -> Unit,
    onTimeEndPicked: (Int, Int) -> Unit,
    onRepeatFieldClicked: () -> Unit,
    onPriorityFieldClicked: () -> Unit,
    onRemindFieldClicked: () -> Unit
) {
    val titleBorderColor = when (state?.titleFieldState) {
        TitleFieldState.Error -> MaterialTheme.colors.error
        else -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    }

    DefaultTextField(
        value = state?.title.orEmpty(),
        onValueChanged = { onTitleEdited(it) },
        label = stringResource(id = R.string.title),
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.hasFocus) },
        errorText = state?.titleErrorText.orEmpty(),
        borderColor = titleBorderColor
    )
    HeightSpacer()
    DefaultTextField(
        value = state?.description.orEmpty(),
        onValueChanged = { onDescriptionEdited(it) },
        label = stringResource(id = R.string.description),
        modifier = Modifier.padding(horizontal = 15.dp),
    )
    HeightSpacer()
    DatePickerRow(
        state = state,
        fieldWidth = fieldWidth,
        onStartDatePicked = { onStartDatePicked(it) },
        onEndDatePicked = { onEndDatePicked(it) },
        modifier = Modifier.fillMaxWidth()
    )
    HeightSpacer()
    TimePickerRow(
        state = state,
        fieldWidth = fieldWidth,
        onTimeStartPicked = { hour, minutes ->
            onTimeStartPicked(hour, minutes)
        },
        onTimeEndPicked = { hour, minutes ->
            onTimeEndPicked(hour, minutes)
        },
        modifier = Modifier.fillMaxWidth()
    )
    HeightSpacer()
    DefaultBottomSheetField(
        string = state?.repeat?.name?.let { stringResource(id = it) },
        label = stringResource(id = R.string.repeat),
        onClick = { onRepeatFieldClicked() },
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChanged = { }
    )
    HeightSpacer()
    DefaultBottomSheetField(
        string = state?.priority?.name?.let { stringResource(id = it) },
        label = stringResource(id = R.string.priority),
        onClick = { onPriorityFieldClicked() },
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChanged = {}
    )
    HeightSpacer()
    DefaultBottomSheetField(
        string = state?.remind?.name?.let { stringResource(id = it) },
        label = stringResource(id = R.string.remind),
        onClick = { onRemindFieldClicked() },
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChanged = { }
    )
}