package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pti.sheldons_schedule.CreateEventViewModel
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.LightSky

private const val PADDING_WIDTH_SUM = 60
private const val FIELD_COUNT = 2

@Composable
fun CreateEventScreen(viewModel: CreateEventViewModel = hiltViewModel()) {

    val createEventScreenState by viewModel.createEventScreenState.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(LightSky)
    ) {
        val halfFieldWidth = (this.maxWidth.value.toInt() - PADDING_WIDTH_SUM) / FIELD_COUNT

        Column(modifier = Modifier.fillMaxSize()) {
            SaveOrCloseCreatingEvent(
                onCloseIconClicked = {},
                onSaveIconClicked = {},
                modifier = Modifier
                    .height(58.dp)
                    .fillMaxWidth()
            )

            HeightSpacer()

            DefaultTextField(
                value = "",
                onValueChanged = {},
                label = stringResource(id = R.string.title),
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            HeightSpacer()
            DefaultTextField(
                value = "",
                onValueChanged = {},
                label = stringResource(id = R.string.description),
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            HeightSpacer(height = 15.dp)
            Row(modifier = Modifier.fillMaxWidth()) {
                DatePickerField(
                    pickedDate = createEventScreenState.formattedStartDate,
                    onPickedDate = { calendar ->
                        calendar?.let { it -> viewModel.onStartDatePicked(it) }
                    },
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(halfFieldWidth.dp)
                        .height(50.dp)
                )
                Spacer(modifier = Modifier.width(30.dp))
                DatePickerField(
                    pickedDate = createEventScreenState.formattedEndDate,
                    onPickedDate = { calendar ->
                        calendar?.let { it -> viewModel.onEndDatePicked(it) }
                    },
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .width(halfFieldWidth.dp)
                        .height(50.dp)
                )
            }
        }
    }
}