package com.pti.sheldons_schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.data.TimeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor() : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        CreateEventScreenState(
            startDate = Calendar.getInstance(),
            startTime = TimeState(
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE)
            ),
            endTime = TimeState(
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE)
            )
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