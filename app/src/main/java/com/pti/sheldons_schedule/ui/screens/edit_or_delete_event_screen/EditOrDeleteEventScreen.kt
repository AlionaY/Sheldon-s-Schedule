package com.pti.sheldons_schedule.ui.screens.edit_or_delete_event_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
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
import com.pti.sheldons_schedule.ui.common.*
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.util.Constants.FIELD_COUNT
import com.pti.sheldons_schedule.util.Constants.PADDING_WIDTH_SUM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditOrDeleteEventScreen(
    navController: NavController,
    viewModel: EditOrDeleteEventViewModel = hiltViewModel()
) {

    val event by viewModel.pickedEvent.collectAsState(initial = null)
    val screenState by viewModel.screenState.collectAsState(initial = null)
    val isPickedTimeValid by viewModel.isPickedTimeValid.collectAsState(initial = true)

    val focusManager = LocalFocusManager.current

    val snackbarMessage = stringResource(R.string.time_picker_error_message)
    val snackbarAction = stringResource(id = R.string.snackbar_action)
    val titleBorderColor = when (screenState?.titleFieldState) {
        TitleFieldState.Error -> MaterialTheme.colors.error
        else -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    }
    var isSnackbarActionClicked by remember { mutableStateOf(false) }

    if (screenState?.pickedStartTime != screenState?.startDate && isSnackbarActionClicked) {
        TimePicker(
            calendar = screenState?.startDate,
            onTimePicked = { hour, minutes ->
                viewModel.onTimeStartPicked(hour = hour, minutes = minutes)
            }
        )
        isSnackbarActionClicked = false
    }

    if (screenState?.pickedEndTime != screenState?.endDate &&
        isSnackbarActionClicked &&
        screenState?.startDate == screenState?.pickedStartTime
    ) {
        TimePicker(
            calendar = screenState?.endDate,
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
        data = screenState?.options.orEmpty(),
        header = screenState?.options?.first()?.title?.let { stringResource(it) }.orEmpty(),
        nameGetter = { stringResource(id = it.nameId) },
        onClick = {
            viewModel.onSelected(it)
            focusManager.clearFocus()
        }
    ) { sheetState ->

        val scope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = screenState?.options) {
            if (!screenState?.options.isNullOrEmpty()) {
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
                Row(
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        contentDescription = "back",
                        modifier = Modifier.clickable { navController.popBackStack() },
                        tint = Black
                    )
                }
                DefaultTextField(
                    value = screenState?.title.orEmpty(),
                    onValueChanged = { viewModel.onTitleEdited(it) },
                    label = stringResource(id = R.string.title),
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { viewModel.onFocusChanged(it.hasFocus) },
                    errorText = screenState?.titleErrorText.orEmpty(),
                    borderColor = titleBorderColor
                )
                HeightSpacer()
                DefaultTextField(
                    value = screenState?.description.orEmpty(),
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
                        pickedDate = screenState?.formattedStartDate,
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
                        pickedDate = screenState?.formattedEndDate,
                        onPickedDate = { calendar ->
                            calendar?.let { it -> viewModel.onEndDatePicked(it) }
                        },
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .width(halfFieldWidth.dp)
                            .wrapContentHeight(),
                        label = { Text(text = stringResource(id = R.string.end_date)) },
                        onValueChanged = { },
                        startDate = screenState?.datePickerStartDate
                    )
                }
                HeightSpacer()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TimePickerField(
                        pickedTime = screenState?.formattedStartTime,
                        calendar = screenState?.startDate,
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
                        pickedTime = screenState?.formattedEndTime,
                        calendar = screenState?.endDate,
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
                    string = screenState?.repeat?.name?.let { stringResource(id = it) },
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
                    string = screenState?.priority?.name?.let { stringResource(id = it) },
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
                    string = screenState?.remind?.name?.let { stringResource(id = it) },
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
