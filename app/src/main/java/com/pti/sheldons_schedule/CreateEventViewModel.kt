package com.pti.sheldons_schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.data.Options
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        CreateEventScreenState(
            startDate = Calendar.getInstance(),
            endDate = Calendar.getInstance(),
            selectedPriority = context.getString(R.string.priority_low)
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

    fun onRepeatFieldClicked() {
        createEventScreenState.update { state ->
            state.copy(options = Options.Repeat)
        }
    }

    fun onPriorityFieldClicked() {
        createEventScreenState.update { state ->
            state.copy(options = Options.Priority)
        }
    }

    fun onRemindFieldClicked() {
        createEventScreenState.update { state ->
            state.copy(options = Options.Reminder)
        }
    }

    fun onRepeatSelected(string: String) {
        createEventScreenState.update { it.copy(selectedRepeat = string) }
    }

    fun onRemindSelected(string: String) {
        createEventScreenState.update { it.copy(selectedRemind = string) }
    }

    fun onPrioritySelected(string: String) {
        createEventScreenState.update { it.copy(selectedPriority = string) }
    }
}