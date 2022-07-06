package com.pti.sheldons_schedule.ui.screens.edit_screen

import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject


//todo: deal with view model
@HiltViewModel
class EditEventViewModel @Inject constructor() : ViewModel() {
    val state = MutableStateFlow(
        ScreenState(
            endDate = Calendar.getInstance(),
            startDate = Calendar.getInstance(),
            pickedStartTime = Calendar.getInstance(),
            pickedEndTime = Calendar.getInstance()
        )
    )
}