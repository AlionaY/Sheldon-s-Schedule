package com.pti.sheldons_schedule.ui.screens.edit_event_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun EditEventScreen(
    eventId: Long,
    navController: NavController,
    viewModel: EditScreenViewModel = hiltViewModel()
) {

    val event by viewModel.event.collectAsState(initial = null)

    viewModel.getEvent(eventId)
}