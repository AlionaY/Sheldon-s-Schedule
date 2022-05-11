package com.pti.sheldons_schedule.ui.screens

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
import com.pti.sheldons_schedule.ui.screens.create_event_screen.DatePickerField
import com.pti.sheldons_schedule.ui.screens.create_event_screen.DefaultTextField
import com.pti.sheldons_schedule.ui.screens.create_event_screen.HeightSpacer
import com.pti.sheldons_schedule.ui.screens.create_event_screen.SaveOrCloseCreatingEvent
import com.pti.sheldons_schedule.ui.theme.LightSky

@Composable
fun CreateEventScreen(viewModel: CreateEventViewModel = hiltViewModel()) {

    val createEventScreenState by viewModel.createEventScreenState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightSky)
    ) {

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
        }
    }
}