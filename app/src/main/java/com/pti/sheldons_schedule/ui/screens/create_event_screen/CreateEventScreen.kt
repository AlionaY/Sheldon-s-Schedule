package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.LightSky
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

    ModalBottomSheet(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        data = state.options.orEmpty(),
        header = state.options?.first()?.title?.let { stringResource(it) }.orEmpty(),
        nameGetter = { stringResource(id = it.nameId) },
        onClick = { viewModel.onSelected(it) }
    ) { sheetState ->

        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = state.options) {
            if (!state.options.isNullOrEmpty()) {
                scope.launch {
                    sheetState.show()
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(LightSky)
        ) {
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
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                HeightSpacer()
                DefaultTextField(
                    value = state.description,
                    onValueChanged = { viewModel.onDescriptionEdited(it) },
                    label = stringResource(id = R.string.description),
                    modifier = Modifier.padding(horizontal = 15.dp)
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
                        label = { Text(text = stringResource(id = R.string.start_time))},
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
                        label = { Text(text = stringResource(id = R.string.end_time))},
                        onValueChanged = {}
                    )
                }
                HeightSpacer()
                DefaultBottomSheetField(
                    string = stringResource(
                        id = state.repeat?.name ?: R.string.repeat
                    )
                ) {
                    viewModel.onRepeatFieldClicked()
                }
                HeightSpacer()
                DefaultBottomSheetField(string = stringResource(id = state.priority.name)) {
                    viewModel.onPriorityFieldClicked()
                }
                HeightSpacer()
                DefaultBottomSheetField(
                    string = stringResource(
                        id = state.remind?.name ?: R.string.remind
                    )
                ) {
                    viewModel.onRemindFieldClicked()
                }
            }
        }
    }
}