package com.pti.sheldons_schedule

import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.data.TimeState
import com.pti.sheldons_schedule.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor() : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        CreateEventScreenState(
            startDate = Calendar.getInstance()
        )
    )

    fun onDatePicked(calendar: Calendar) {
        createEventScreenState.update { it.copy(startDate = calendar) }
    }

    fun onTimeStartPicked(hour: Int, minutes: Int) {
        createEventScreenState.update { it.copy(startTime = TimeState(hour, minutes)) }
    }

    fun onTimeEndPicked(hour: Int, minutes: Int) {
        createEventScreenState.update { it.copy(endTime = TimeState(hour, minutes)) }
    }
}