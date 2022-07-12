package com.pti.sheldons_schedule.ui.screens.edit_or_delete_event_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
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
import com.pti.sheldons_schedule.ui.common.ModalBottomSheet
import com.pti.sheldons_schedule.ui.common.ScreenContent
import com.pti.sheldons_schedule.ui.common.TimePicker
import com.pti.sheldons_schedule.ui.screens.create_event_screen.CreateOrEditEventViewModel
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
    val screenState by viewModel.editEventScreenState.collectAsState(initial = null)
    val isPickedTimeValid by viewModel.isPickedTimeValid.collectAsState(initial = true)

    val focusManager = LocalFocusManager.current

    val snackbarMessage = stringResource(R.string.time_picker_error_message)
    val snackbarAction = stringResource(id = R.string.snackbar_action)

    var isSnackbarActionClicked by remember { mutableStateOf(false) }

    viewModel.getEvent(eventId)

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
                TopToolbar(onClick = { navController.popBackStack() })

                ScreenContent(
                    screenState = screenState,
                    fieldWidth = halfFieldWidth.dp,
                    focusRequester = focusRequester,
                    onTitleEdited = { viewModel.onTitleEdited(it) },
                    onFocusChanged = { viewModel.onFocusChanged(it) },
                    onDescriptionEdited = { viewModel.onDescriptionEdited(it) },
                    onStartDatePicked = { viewModel.onStartDatePicked(it) },
                    onEndDatePicked = { viewModel.onEndDatePicked(it) },
                    onStartTimePicked = { hour, minutes ->
                        viewModel.onTimeStartPicked(hour, minutes)
                    },
                    onEndTimePicked = { hour, minutes ->
                        viewModel.onTimeEndPicked(hour, minutes)
                    },
                    onRemindFieldClicked = { viewModel.onRemindFieldClicked() },
                    onRepeatFieldClicked = { viewModel.onRepeatFieldClicked() },
                    onPriorityFieldClicked = { viewModel.onPriorityFieldClicked() }
                )
            }

            BottomToolbar(
                onSaveEventClicked = { viewModel.onSaveEventClicked() },
                onDeleteEventClicked = {
                    viewModel.onDeleteEventClicked()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .height(58.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
