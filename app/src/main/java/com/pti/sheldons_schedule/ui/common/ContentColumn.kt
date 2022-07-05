package com.pti.sheldons_schedule.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.EditEventScreenState
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.ui.screens.edit_or_delete_event_screen.TopToolbar
import java.util.*

@Composable
fun ContentColumn(
    screenState: EditEventScreenState?,
    focusRequester: FocusRequester,
    fieldWidth: Int,
    onToolbarClick: () -> Unit,
    onTitleEdited: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onDescriptionEdited: (String) -> Unit,
    onStartDatePicked: (Calendar) -> Unit,
    onEndDatePicked: (Calendar) -> Unit,
    onStartTimePicked: (Int, Int) -> Unit,
    onEndTimePicked: (Int, Int) -> Unit,
    onRepeatFieldClicked: () -> Unit,
    onPriorityFieldClicked: () -> Unit,
    onRemindFieldClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val titleBorderColor = when (screenState?.titleFieldState) {
        TitleFieldState.Error -> MaterialTheme.colors.error
        else -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopToolbar(onClick = onToolbarClick)
        DefaultTextField(
            value = screenState?.title.orEmpty(),
            onValueChanged = {
                onTitleEdited(it)
            },
            label = stringResource(id = R.string.title),
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged(it.hasFocus) },
            errorText = screenState?.titleErrorText.orEmpty(),
            borderColor = titleBorderColor
        )
        HeightSpacer()
        DefaultTextField(
            value = screenState?.description.orEmpty(),
            onValueChanged = { onDescriptionEdited(it) },
            label = stringResource(id = R.string.description),
            modifier = Modifier.padding(horizontal = 15.dp),
        )
        HeightSpacer()
        DatePickedRow(
            screenState = screenState,
            fieldWidth = fieldWidth.dp,
            onStartDatePicked = { calendar -> calendar?.let { onStartDatePicked(it) } },
            onEndDatePicked = { calendar -> calendar?.let { onEndDatePicked(it) } }
        )
        HeightSpacer()
        TimePickerRow(
            screenState = screenState,
            fieldWidth = fieldWidth.dp,
            onStartTimePicked = { hour, minutes ->
                onStartTimePicked(hour, minutes)
            },
            onEndTimePicked = { hour, minutes ->
                onEndTimePicked(hour, minutes)
            }
        )
        HeightSpacer()
        DefaultBottomSheetField(
            text = screenState?.repeat?.name?.let { stringResource(id = it) },
            label = stringResource(id = R.string.repeat),
            onClick = onRepeatFieldClicked,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            onValueChanged = { }
        )
        HeightSpacer()
        DefaultBottomSheetField(
            text = screenState?.priority?.name?.let { stringResource(id = it) },
            label = stringResource(id = R.string.priority),
            onClick = onPriorityFieldClicked,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            onValueChanged = {}
        )
        HeightSpacer()
        DefaultBottomSheetField(
            text = screenState?.remind?.name?.let { stringResource(id = it) },
            label = stringResource(id = R.string.remind),
            onClick = onRemindFieldClicked,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            onValueChanged = { }
        )
    }
}