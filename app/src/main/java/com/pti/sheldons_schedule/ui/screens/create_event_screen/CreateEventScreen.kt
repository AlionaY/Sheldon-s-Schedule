package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pti.sheldons_schedule.CreateEventViewModel
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.ui.theme.LightSky
import com.pti.sheldons_schedule.ui.theme.Steel
import java.util.*

private const val PADDING_WIDTH_SUM = 60
private const val FIELD_COUNT = 2

@Composable
fun CreateEventScreen(viewModel: CreateEventViewModel = hiltViewModel()) {

    val state by viewModel.createEventScreenState.collectAsState(
        CreateEventScreenState(
            startDate = Calendar.getInstance(),
            endDate = Calendar.getInstance()
        )
    )

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
            HeightSpacer(height = 18.dp)
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.start_date).uppercase(),
                    modifier = Modifier.width(halfFieldWidth.dp),
                    fontSize = 10.sp,
                    color = Steel,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = stringResource(id = R.string.end_date).uppercase(),
                    modifier = Modifier.width(halfFieldWidth.dp),
                    fontSize = 10.sp,
                    color = Steel,
                    textAlign = TextAlign.Start
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                DatePickerField(
                    pickedDate = state.formattedStartDate,
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
                    pickedDate = state.formattedEndDate,
                    onPickedDate = { calendar ->
                        calendar?.let { it -> viewModel.onEndDatePicked(it) }
                    },
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .width(halfFieldWidth.dp)
                        .height(50.dp)
                )
            }
            HeightSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.start_time).uppercase(Locale.ROOT),
                    modifier = Modifier.width(halfFieldWidth.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 10.sp,
                    color = Steel,
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = stringResource(id = R.string.end_time).uppercase(Locale.ROOT),
                    modifier = Modifier.width(halfFieldWidth.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 10.sp,
                    color = Steel,
                )
            }
            HeightSpacer(height = 5.dp)
            Row(modifier = Modifier.fillMaxWidth()) {
                TimePickerField(
                    currentTime = state.formattedStartTime,
                    onTimePicked = { viewModel.onTimeStartPicked(it) },
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(halfFieldWidth.dp)
                        .height(50.dp)
                )
                Spacer(modifier = Modifier.width(30.dp))
                TimePickerField(
                    currentTime = state.formattedEndTime,
                    onTimePicked = { viewModel.onTimeEndPicked(it) },
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .width(halfFieldWidth.dp)
                        .height(50.dp)
                )
            }
            HeightSpacer()
        }
    }
}