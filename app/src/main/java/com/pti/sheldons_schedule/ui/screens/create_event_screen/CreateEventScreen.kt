package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.R
import kotlinx.coroutines.launch

private const val PADDING_WIDTH_SUM = 60
private const val FIELD_COUNT = 2

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateEventScreen(
    navController: NavController,
    viewModel: CreateEventViewModel = hiltViewModel()
) {

    val state by viewModel.createEventScreenState.collectAsState()
    val focusManager = LocalFocusManager.current

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
                        .focusRequester(focusRequester),
                    showError = state.showError,
                    onFocusChanged = { viewModel.validateTitle(it) }
                        .focusRequester(focusRequester)
                        .onFocusChanged { viewModel.onFocusChanged(it.hasFocus) },
                    errorText = state.errorText.orEmpty(),
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
                        onValueChanged = { }
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
                    string = stringResource(id = state.repeat?.name ?: R.string.repeat),
                    onClick = { viewModel.onRepeatFieldClicked() },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    onValueChanged = { }
                )
                HeightSpacer()
                DefaultBottomSheetField(
                    string = stringResource(id = state.priority.name),
                    onClick = { viewModel.onPriorityFieldClicked() },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    onValueChanged = {})
                HeightSpacer()
                DefaultBottomSheetField(
                    string = stringResource(id = state.remind?.name ?: R.string.remind),
                    onClick = { viewModel.onRemindFieldClicked() },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    onValueChanged = { }
                )
            }
        }
    }
}