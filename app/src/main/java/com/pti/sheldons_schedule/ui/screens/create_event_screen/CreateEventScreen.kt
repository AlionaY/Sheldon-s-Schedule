package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.util.Constants.FIELD_COUNT
import com.pti.sheldons_schedule.util.Constants.PADDING_WIDTH_SUM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    viewModel: CreateEventViewModel = hiltViewModel()
) {
    val state by viewModel.createEventScreenState.collectAsState()
    val isPickedTimeValid by viewModel.isPickedTimeValid.collectAsState(initial = true)
    val focusManager = LocalFocusManager.current

    val snackbarMessage = stringResource(R.string.time_picker_error_message)
    val snackbarAction = stringResource(id = R.string.snackbar_action)
    val titleBorderColor = when (state.titleFieldState) {
        TitleFieldState.Normal -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        TitleFieldState.Error -> MaterialTheme.colors.error
    }
    var isSnackbarActionClicked by remember { mutableStateOf(false) }

    if (state.pickedStartTime != state.startDate && isSnackbarActionClicked) {
        TimePicker(
            calendar = state.startDate,
            onTimePicked = { hour, minutes ->
                viewModel.onTimeStartPicked(hour = hour, minutes = minutes)
            }
        )
        isSnackbarActionClicked = false
    }

    if (state.pickedEndTime != state.endDate &&
        isSnackbarActionClicked &&
        state.startDate == state.pickedStartTime
    ) {
        TimePicker(
            calendar = state.endDate,
            onTimePicked = { hour, minutes ->
                viewModel.onTimeEndPicked(hour = hour, minutes = minutes)
            }
        )
        isSnackbarActionClicked = false
    }

    ModalBottomSheet(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        data = state.options.orEmpty(),
        header = state.options?.first()?.title?.let { stringResource(it) }.orEmpty(),
        nameGetter = { stringResource(id = it.nameId) },
        onClick = {
            viewModel.onSelected(it)
            focusManager.clearFocus()
        }
    ) { sheetState ->

        val scope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = state.options) {
            if (!state.options.isNullOrEmpty()) {
                scope.launch {
                    sheetState.show()
                }
            }
        }

        LaunchedEffect(key1 = Unit) {
            focusRequester.requestFocus()
        }

        LaunchedEffect(key1 = isPickedTimeValid) {
            if (!isPickedTimeValid) {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = snackbarMessage,
                        actionLabel = snackbarAction
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            isSnackbarActionClicked = true
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
                viewModel.resetTimeValidationValue()
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val halfFieldWidth = (this.maxWidth.value.toInt() - PADDING_WIDTH_SUM) / FIELD_COUNT

            Column(modifier = Modifier.fillMaxSize()) {
                SaveOrCloseCreatingEvent(
                    onCloseIconClicked = { navController.popBackStack() },
                    onSaveIconClicked = {
                        viewModel.onSaveEventClicked()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth()
                )
                HeightSpacer()
                DefaultTextField(
                    value = state.title,
                    onValueChanged = { viewModel.onTitleEdited(it) },
                    label = stringResource(id = R.string.title),
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { viewModel.onFocusChanged(it.hasFocus) },
                    errorText = state.titleErrorText.orEmpty(),
                    borderColor = titleBorderColor
                )
                HeightSpacer()
                DefaultTextField(
                    value = state.description,
                    onValueChanged = { viewModel.onDescriptionEdited(it) },
                    label = stringResource(id = R.string.description),
                    modifier = Modifier.padding(horizontal = 15.dp),
                )
                HeightSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DatePickerField(
                        pickedDate = state.formattedStartDate,
                        onPickedDate = { calendar ->
                            calendar?.let { it -> viewModel.onStartDatePicked(it) }
                        },
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .width(halfFieldWidth.dp)
                            .wrapContentHeight(),
                        label = { Text(stringResource(id = R.string.start_date)) },
                        onValueChanged = { }
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    DatePickerField(
                        pickedDate = state.formattedEndDate,
                        onPickedDate = { calendar ->
                            calendar?.let { it -> viewModel.onEndDatePicked(it) }
                        },
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .width(halfFieldWidth.dp)
                            .wrapContentHeight(),
                        label = { Text(text = stringResource(id = R.string.end_date)) },
                        onValueChanged = { },
                        startDate = state.datePickerStartDate
                    )
                }
                HeightSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TimePickerField(
                        pickedTime = state.formattedStartTime,
                        calendar = state.startDate,
                        onTimePicked = { hour, minutes ->
                            viewModel.onTimeStartPicked(hour, minutes)
                        },
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .width(halfFieldWidth.dp)
                            .wrapContentHeight(),
                        label = { Text(text = stringResource(id = R.string.start_time)) },
                        onValueChanged = {}
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    TimePickerField(
                        pickedTime = state.formattedEndTime,
                        calendar = state.endDate,
                        onTimePicked = { hour, minutes ->
                            viewModel.onTimeEndPicked(hour, minutes)
                        },
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .width(halfFieldWidth.dp)
                            .wrapContentHeight(),
                        label = { Text(text = stringResource(id = R.string.end_time)) },
                        onValueChanged = {}
                    )
                }
                HeightSpacer()
                DefaultBottomSheetField(
                    string = stringResource(id = state.repeat.name),
                    label = stringResource(id = R.string.repeat),
                    onClick = { viewModel.onRepeatFieldClicked() },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onValueChanged = { }
                )
                HeightSpacer()
                DefaultBottomSheetField(
                    string = stringResource(id = state.priority.name),
                    label = stringResource(id = R.string.priority),
                    onClick = { viewModel.onPriorityFieldClicked() },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onValueChanged = {}
                )
                HeightSpacer()
                DefaultBottomSheetField(
                    string = stringResource(id = state.remind.name),
                    label = stringResource(id = R.string.remind),
                    onClick = { viewModel.onRemindFieldClicked() },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onValueChanged = { }
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}