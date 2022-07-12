package com.pti.sheldons_schedule.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.util.toTextFieldValue
import java.util.*

@Composable
fun ScreenContent(
    screenState: ScreenState?,
    focusRequester: FocusRequester,
    fieldWidth: Dp,
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
) {
    val titleBorderColor = when (screenState?.titleFieldState) {
        TitleFieldState.Error -> MaterialTheme.colors.error
        else -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    }

    val titleTextFieldValue = remember {
        mutableStateOf(screenState?.title.toTextFieldValue())
    }

    val descriptionTextFieldValue = remember {
        mutableStateOf(screenState?.description.toTextFieldValue())
    }
    
    LaunchedEffect(key1 = screenState?.description) {
        descriptionTextFieldValue.value = screenState?.description.toTextFieldValue()
    }

    LaunchedEffect(key1 = screenState?.title) {
        titleTextFieldValue.value = screenState?.title.toTextFieldValue()
    }

    DefaultTextField(
        value = titleTextFieldValue.value,
        onValueChanged = {
            titleTextFieldValue.value = TextFieldValue(
                text = it.text,
                selection = TextRange(it.text.length)
            )
            onTitleEdited(titleTextFieldValue.value.text)
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
        value = descriptionTextFieldValue.value,
        onValueChanged = { onDescriptionEdited(it.text) },
        label = stringResource(id = R.string.description),
        modifier = Modifier.padding(horizontal = 15.dp),
    )
    HeightSpacer()
    DatePickedRow(
        screenState = screenState,
        fieldWidth = fieldWidth,
        onStartDatePicked = { onStartDatePicked(it) },
        onEndDatePicked = { onEndDatePicked(it) }
    )
    HeightSpacer()
    TimePickerRow(
        screenState = screenState,
        fieldWidth = fieldWidth,
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