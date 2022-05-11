package com.pti.sheldons_schedule.ui.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.pti.sheldons_schedule.MainViewModel
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.screens.create_event_screen.DatePicker
import com.pti.sheldons_schedule.ui.screens.create_event_screen.DefaultSpacer
import com.pti.sheldons_schedule.ui.screens.create_event_screen.DefaultTextField
import com.pti.sheldons_schedule.ui.screens.create_event_screen.SaveOrCloseCreatingEvent
import com.pti.sheldons_schedule.ui.theme.LightSky

@Composable
fun CreateEventScreen(viewModel: MainViewModel = hiltViewModel()) {

    val date by viewModel.date.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightSky)
    ) {
        val activity = LocalContext.current as? AppCompatActivity

        Column(modifier = Modifier.fillMaxSize()) {
            SaveOrCloseCreatingEvent()
            DefaultSpacer()
            DefaultTextField(value = "", onValueChanged = {}, labelRes = R.string.title)
            DefaultSpacer()
            DefaultTextField(value = "", onValueChanged = {}, labelRes = R.string.description)
            DefaultSpacer()
            activity?.let {
                DatePicker(
                    activity = it,
                    pickedDate = date,
                    onPickedDate = { millis -> viewModel.onDatePicked(millis) }
                )
            }
        }
    }
}