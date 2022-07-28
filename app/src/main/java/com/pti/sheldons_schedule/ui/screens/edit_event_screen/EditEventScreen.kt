package com.pti.sheldons_schedule.ui.screens.edit_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.common.CreateOrEditEventViewModel
import com.pti.sheldons_schedule.ui.common.ScreenContent
import com.pti.sheldons_schedule.ui.common.TimePicker
import com.pti.sheldons_schedule.ui.common.TopToolbar
import com.pti.sheldons_schedule.ui.navigation.NavDestination
import com.pti.sheldons_schedule.ui.navigation.navigate
import com.pti.sheldons_schedule.ui.screens.create_event_screen.ModalBottomSheet
import com.pti.sheldons_schedule.util.Constants.FIELD_COUNT
import com.pti.sheldons_schedule.util.Constants.PADDING_WIDTH_SUM
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditEventScreen(
    eventId: Long,
    navController: NavController,
    viewModel: CreateOrEditEventViewModel = hiltViewModel()
) {

    val screenState by viewModel.editEventScreenState.collectAsState()
    val isPickedTimeValid by viewModel.isPickedTimeValid.collectAsState(initial = true)
    val focusManager = LocalFocusManager.current

    val snackbarMessage = stringResource(R.string.time_picker_error_message)
    val snackbarAction = stringResource(id = R.string.snackbar_action)
    var isSnackbarActionClicked by remember { mutableStateOf(false) }

    viewModel.getEvent(eventId)

    if (screenState.pickedStartTime != screenState.startDate && isSnackbarActionClicked) {
        TimePicker(
            calendar = screenState.startDate,
            onTimePicked = { hour, minutes ->
                viewModel.onTimeStartPicked(
                    hour = hour,
                    minutes = minutes,
                    isEditEventScreen = true
                )
            }
        )
        isSnackbarActionClicked = false
    }

    if (screenState.pickedEndTime != screenState.endDate &&
        isSnackbarActionClicked &&
        screenState.startDate == screenState.pickedStartTime
    ) {
        TimePicker(
            calendar = screenState.endDate,
            onTimePicked = { hour, minutes ->
                viewModel.onTimeEndPicked(
                    hour = hour,
                    minutes = minutes,
                    isEditEventScreen = true
                )
            }
        )
        isSnackbarActionClicked = false
    }

    ModalBottomSheet(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        data = screenState.options.orEmpty(),
        header = screenState.options?.first()?.title?.let { stringResource(it) }.orEmpty(),
        nameGetter = { stringResource(id = it.nameId) },
        onClick = {
            viewModel.onSelected(it, true)
            focusManager.clearFocus()
        }
    ) { sheetState ->

        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val snackbarHostState = remember { SnackbarHostState() }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(key1 = screenState.options) {
            if (!screenState.options.isNullOrEmpty()) {
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
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    ScreenContent(
                        state = screenState,
                        fieldWidth = halfFieldWidth.dp,
                        textFieldFocusRequester = focusRequester,
                        isCreateEventScreen = false,
                        onTitleEdited = { viewModel.onTitleEdited(it, true) },
                        onFocusChanged = { viewModel.onFocusChanged(it, true) },
                        onDescriptionEdited = { viewModel.onDescriptionEdited(it, true) },
                        onStartDatePicked = { viewModel.onStartDatePicked(it, true) },
                        onEndDatePicked = { viewModel.onEndDatePicked(it, true) },
                        onTimeStartPicked = { hour, minutes ->
                            viewModel.onTimeStartPicked(hour, minutes, true)
                        },
                        onTimeEndPicked = { hour, minutes ->
                            viewModel.onTimeEndPicked(hour, minutes, true)
                        },
                        onRepeatFieldClicked = { viewModel.onRepeatFieldClicked(true) },
                        onPriorityFieldClicked = { viewModel.onPriorityFieldClicked(true) },
                        onRemindFieldClicked = { viewModel.onRemindFieldClicked(true) },
                        onAddTodoListClicked = {},
                        onAddTodoItemClicked = {},
                        onValueChanged = { title, index -> },
                        onCheckedChange = { isChecked, index -> },
                        onTodoItemChanged = {}
                    )
                }
            }

            BottomToolbar(
                onSaveEventClicked = {
                    viewModel.onSaveEventClicked(true)
                    navController.navigate(NavDestination.EntryScreen)
                },
                onDeleteEventClicked = {
                    viewModel.onDeleteEventClicked()
                    navController.navigate(NavDestination.EntryScreen)
                },
                modifier = Modifier
                    .height(58.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colors.background)
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}