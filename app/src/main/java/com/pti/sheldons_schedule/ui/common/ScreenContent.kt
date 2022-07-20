package com.pti.sheldons_schedule.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.util.toTextFieldValue
import java.util.*

@Composable
fun ScreenContent(
    state: ScreenState?,
    textFieldFocusRequester: FocusRequester,
    fieldWidth: Dp,
    isAddToDoListClicked: Boolean,
    onTitleEdited: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onDescriptionEdited: (String) -> Unit,
    onStartDatePicked: (Calendar) -> Unit,
    onEndDatePicked: (Calendar) -> Unit,
    onTimeStartPicked: (Int, Int) -> Unit,
    onTimeEndPicked: (Int, Int) -> Unit,
    onRepeatFieldClicked: () -> Unit,
    onPriorityFieldClicked: () -> Unit,
    onRemindFieldClicked: () -> Unit,
    onIconedTextClicked: () -> Unit
) {
    val todoFieldFocusRequester = remember { FocusRequester() }

    val titleBorderColor = when (state?.titleFieldState) {
        TitleFieldState.Error -> MaterialTheme.colors.error
        else -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    }

    val titleTextFieldValue = remember {
        mutableStateOf(state?.title.toTextFieldValue())
    }

    val descriptionTextFieldValue = remember {
        mutableStateOf(state?.description.toTextFieldValue())
    }

    LaunchedEffect(key1 = state?.title) {
        titleTextFieldValue.value = state?.title.toTextFieldValue()
    }

    LaunchedEffect(key1 = state?.description) {
        descriptionTextFieldValue.value = state?.description.toTextFieldValue()
    }

    LaunchedEffect(key1 = isAddToDoListClicked) {
        if (isAddToDoListClicked) todoFieldFocusRequester.requestFocus()
    }

    DefaultTextField(
        value = titleTextFieldValue.value,
        onValueChanged = { onTitleEdited(it.text) },
        label = stringResource(id = R.string.title),
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .focusRequester(textFieldFocusRequester)
            .onFocusChanged { onFocusChanged(it.hasFocus) },
        errorText = state?.titleErrorText.orEmpty(),
        borderColor = titleBorderColor
    )
    HeightSpacer()
    DefaultTextField(
        value = descriptionTextFieldValue.value,
        onValueChanged = { onDescriptionEdited(it.text) },
        label = stringResource(id = R.string.description),
        modifier = Modifier.padding(horizontal = 15.dp),
    )
    if (!isAddToDoListClicked) {
        IconedText(
            text = stringResource(id = R.string.add_to_do_list),
            textSize = 15.sp,
            onClick = { onIconedTextClicked() },
            modifier = Modifier
                .padding(start = 15.dp)
                .fillMaxWidth()
                .height(50.dp)
        )
    }
//    todo: set necessary params
    if (isAddToDoListClicked) {
        DefaultCheckboxColumn(
            focusRequester = todoFieldFocusRequester,
            text = "Value",
            checked = false,
            onValueChanged = { },
            onCheckedChange = { },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 15.dp)
                .fillMaxWidth()
                .height(100.dp)
        )
    }
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
        text = state?.repeat?.name?.let { stringResource(id = it) },
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
        text = state?.priority?.name?.let { stringResource(id = it) },
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
        text = state?.remind?.name?.let { stringResource(id = it) },
        label = stringResource(id = R.string.remind),
        onClick = { onRemindFieldClicked() },
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChanged = { }
    )
}