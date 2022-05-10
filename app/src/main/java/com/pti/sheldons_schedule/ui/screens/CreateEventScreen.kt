package com.pti.sheldons_schedule.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pti.sheldons_schedule.MainViewModel
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.screens.create_event_screen.DatePicker
import com.pti.sheldons_schedule.ui.screens.create_event_screen.SaveOrCloseCreatingEvent
import com.pti.sheldons_schedule.ui.screens.create_event_screen.SpacerItem
import com.pti.sheldons_schedule.ui.screens.create_event_screen.TextFieldItem
import com.pti.sheldons_schedule.ui.theme.LightSky

@Composable
fun CreateEventScreen(viewModel: MainViewModel = hiltViewModel()) {

    val date by viewModel.date.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightSky)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SaveOrCloseCreatingEvent()
            SpacerItem()
            TextFieldItem(value = "", onValueChanged = {}, labelRes = R.string.title)
            SpacerItem()
            TextFieldItem(value = "", onValueChanged = {}, labelRes = R.string.description)
            SpacerItem()
            DatePicker(
                date = date,
                onPickedDate = { year, month, day ->
                    viewModel.onPickedDate(year, month, day)
                }
            )
        }
    }
}