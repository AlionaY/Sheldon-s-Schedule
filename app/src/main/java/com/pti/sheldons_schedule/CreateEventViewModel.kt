package com.pti.sheldons_schedule

import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.CreateEventScreenState
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
            endDate = Calendar.getInstance()
        )
    )



    fun onStartDatePicked(calendar: Calendar) {
        createEventScreenState.update {
            it.copy(startDate = (it.startDate.clone() as Calendar).apply {
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            })
        }
    }

    fun onEndDatePicked(calendar: Calendar) {
        createEventScreenState.update {
            it.copy(endDate = (it.endDate.clone() as Calendar).apply {
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            })
        }
    }

    fun onTimeStartPicked(hour: Int, minutes: Int) {
        createEventScreenState.update {
            it.copy(startDate = (it.startDate.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minutes)
            })
        }
    }

    fun onTimeEndPicked(hour: Int, minutes: Int) {
        createEventScreenState.update {
            it.copy(endDate = (it.endDate.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minutes)
            })
        }
    }
}