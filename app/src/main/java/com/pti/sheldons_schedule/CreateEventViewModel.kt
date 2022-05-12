package com.pti.sheldons_schedule

import android.util.Log
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
            startTime = Calendar.getInstance(),
            endTime = Calendar.getInstance()
        )
    )


    fun onDatePicked(calendar: Calendar) {
        createEventScreenState.update {
            Log.d("###", "onDatePicked day ${calendar.get(Calendar.DAY_OF_MONTH)}")
            it.copy(startDate = it.startDate.apply {
                this.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                this.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                this.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            })
        }
    }

    fun onTimeStartPicked(calendar: Calendar) {
        createEventScreenState.update {
            it.copy(startTime = it.startTime.apply {
                this.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
            })
        }
    }

    fun onTimeEndPicked(calendar: Calendar) {
//        createEventScreenState.update { it.copy(endTime = ) }
    }
}