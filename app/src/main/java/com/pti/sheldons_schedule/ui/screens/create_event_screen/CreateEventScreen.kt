package com.pti.sheldons_schedule.ui.screens.create_event_screen

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
import com.pti.sheldons_schedule.ui.navigation.NavDestination.EntryScreen
import com.pti.sheldons_schedule.ui.navigation.navigate
import com.pti.sheldons_schedule.util.Constants.FIELD_COUNT
import com.pti.sheldons_schedule.util.Constants.PADDING_WIDTH_SUM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    viewModel: CreateOrEditEventViewModel = hiltViewModel()
) {
    val state by viewModel.createEventScreenState.collectAsState()
    val isPickedTimeValid by viewModel.isPickedTimeValid.collectAsState(initial = true)
    val focusManager = LocalFocusManager.current

    val snackbarMessage = stringResource(R.string.time_picker_error_message)
    val snackbarAction = stringResource(id = R.string.snackbar_action)
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
        val scrollState = rememberScrollState()
        val focusRequester = remember { FocusRequester() }
        val snackbarHostState = remember { SnackbarHostState() }

        var isAddToDoListClicked by remember { mutableStateOf(false) }

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

        LaunchedEffect(key1 = isAddToDoListClicked) {
            if (isAddToDoListClicked) focusManager.clearFocus()
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val halfFieldWidth = (this.maxWidth.value.toInt() - PADDING_WIDTH_SUM) / FIELD_COUNT

            Column(modifier = Modifier.fillMaxSize()) {
                TopToolbar(
                    onCloseIconClicked = { navController.popBackStack() },
                    onSaveIconClicked = {
                        viewModel.onSaveEventClicked()
                        navController.navigate(EntryScreen)
                    },
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    ScreenContent(
                        state = state,
                        textFieldFocusRequester = focusRequester,
                        fieldWidth = halfFieldWidth.dp,
                        isCreateEventScreen = true,
                        isAddToDoListClicked = isAddToDoListClicked,
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
                        onRemindFieldClicked = { viewModel.onRemindFieldClicked() },
                        onIconedTextClicked = { isAddToDoListClicked = true }
                    )
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}