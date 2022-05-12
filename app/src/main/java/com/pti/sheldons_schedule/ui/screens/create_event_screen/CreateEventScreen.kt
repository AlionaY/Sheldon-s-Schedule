package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.android.InternalPlatformTextApi
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pti.sheldons_schedule.CreateEventViewModel
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.LightSky
import com.pti.sheldons_schedule.ui.theme.Steel
import java.util.*

private const val PADDING_WIDTH_SUM = 60
private const val TIME_PICKER_FIELD_COUNT = 2

@Composable
fun CreateEventScreen(viewModel: CreateEventViewModel = hiltViewModel()) {

    val createEventScreenState by viewModel.createEventScreenState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightSky)
    ) {

        val config = LocalConfiguration.current
        val halfFieldWidth =
            (config.screenWidthDp - PADDING_WIDTH_SUM) / TIME_PICKER_FIELD_COUNT

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
            HeightSpacer()
            DatePickerField(
                pickedDate = createEventScreenState.formattedStartDate,
                onPickedDate = { calendar ->
                    calendar?.let { it -> viewModel.onDatePicked(it) }
                },
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            )
            HeightSpacer(height = 15.dp)
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
                    currentTime = createEventScreenState.formattedStartTime,
                    onTimePicked = { hour, minutes ->
                        viewModel.onTimeStartPicked(hour, minutes)
                    },
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .width(halfFieldWidth.dp)
                        .height(50.dp)
                )

                Spacer(modifier = Modifier.width(30.dp))

                TimePickerField(
                    currentTime = createEventScreenState.formattedEndTime,
                    onTimePicked = { hour, minutes ->
                        viewModel.onTimeEndPicked(hour, minutes)
                    },
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