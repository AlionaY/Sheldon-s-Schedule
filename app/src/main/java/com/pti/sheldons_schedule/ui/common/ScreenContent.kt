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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.ui.screens.edit_event_screen.CheckboxContent
import com.pti.sheldons_schedule.ui.screens.todo_list_screen.ToDoList
import com.pti.sheldons_schedule.util.horizontalPadding
import com.pti.sheldons_schedule.util.toTextFieldValue
import java.util.*

@Composable
fun ScreenContent(
    state: ScreenState?,
    textFieldFocusRequester: FocusRequester,
    fieldWidth: Dp,
    isCreateEventScreen: Boolean,
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
    onIconedTextClicked: () -> Unit,
    onTodoItemChanged: (String, Int) -> Unit,
    onCheckedChange: (Boolean, Int) -> Unit
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
    if (isCreateEventScreen) {
        ToDoList(
            itemsCount = state?.toDoList?.size ?: 1,
            checked = false,
            isAddToDoListClicked = isAddToDoListClicked,
            focusRequester = todoFieldFocusRequester,
            onValueChanged = { title, index -> },
            onClick = { onIconedTextClicked() }
        )
    } else if (!isCreateEventScreen && !state?.toDoList.isNullOrEmpty()) {
        CheckboxContent(
            state = state,
            onTodoItemChanged = { title, index ->
                onTodoItemChanged(title, index)
            },
            onCheckedChange = onCheckedChange
        )
    } else {
        HeightSpacer()
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
            .horizontalPadding(horizontal = 15.dp, bottom = 70.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChanged = { }
    )
}