package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.ui.common.*
import com.pti.sheldons_schedule.ui.navigation.NavDestination.EntryScreen
import com.pti.sheldons_schedule.util.Constants.FIELD_COUNT
import com.pti.sheldons_schedule.util.Constants.PADDING_WIDTH_SUM
import kotlinx.coroutines.launch
import java.util.*

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
                TopToolbar(
                    onCloseIconClicked = { navController.popBackStack() },
                    onSaveIconClicked = {
                        viewModel.onSaveEventClicked()
                        navController.navigate(EntryScreen.route)
                    },
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth()
                )
                ScreenContent(
                    state = state,
                    focusRequester = focusRequester,
                    titleBorderColor = titleBorderColor,
                    fieldWidth = halfFieldWidth.dp,
                    onTitleEdited = { viewModel.onTitleEdited(it) },
                    onDescriptionEdited = { viewModel.onDescriptionEdited(it) },
                    onFocusChanged = { viewModel.onFocusChanged(it) },
                    onStartDatePicked = { viewModel.onStartDatePicked(it) },
                    onEndDatePicked = { viewModel.onEndDatePicked(it) },
                    onTimeStartPicked = { hour, minutes ->
                        viewModel.onTimeStartPicked(hour, minutes)
                    },
                    onTimeEndPicked = { hour, minutes ->
                        viewModel.onTimeEndPicked(hour, minutes)
                    },
                    onRepeatFieldClicked = { viewModel.onRepeatFieldClicked() },
                    onPriorityFieldClicked = { viewModel.onPriorityFieldClicked() },
                    onRemindFieldClicked = { viewModel.onRemindFieldClicked() }
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun ScreenContent(
    state: ScreenState,
    focusRequester: FocusRequester,
    titleBorderColor: Color,
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
    DefaultTextField(
        value = state.title,
        onValueChanged = { onTitleEdited(it) },
        label = stringResource(id = R.string.title),
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.hasFocus) },
        errorText = state.titleErrorText.orEmpty(),
        borderColor = titleBorderColor
    )
    HeightSpacer()
    DefaultTextField(
        value = state.description,
        onValueChanged = { onDescriptionEdited(it) },
        label = stringResource(id = R.string.description),
        modifier = Modifier.padding(horizontal = 15.dp),
    )
    HeightSpacer()
    DatePickedRow(
        screenState = state,
        fieldWidth = fieldWidth,
        onStartDatePicked = { onStartDatePicked(it) },
        onEndDatePicked = { onEndDatePicked(it) },
        modifier = Modifier.fillMaxWidth()
    )
    HeightSpacer()
    TimePickerRow(
        screenState = state,
        fieldWidth = fieldWidth,
        onStartTimePicked = { hour, minutes ->
            onTimeStartPicked(hour, minutes)
        },
        onEndTimePicked = { hour, minutes ->
            onTimeEndPicked(hour, minutes)
        },
        modifier = Modifier.fillMaxWidth()
    )
    HeightSpacer()
    DefaultBottomSheetField(
        text = stringResource(id = state.repeat.name),
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
        text = stringResource(id = state.priority.name),
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
        text = stringResource(id = state.remind.name),
        label = stringResource(id = R.string.remind),
        onClick = { onRemindFieldClicked() },
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChanged = { }
    )
}