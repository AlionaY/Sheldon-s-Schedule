package com.pti.sheldons_schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.*
import com.pti.sheldons_schedule.data.BottomSheetType.*
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
            selectedPriority = context.getString(PriorityOptions.Low.stringRes)
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

    fun onRepeatFieldClicked(context: Context) {
        createEventScreenState.update { state ->
            state.copy(
                bottomSheetType = Repeat,
                optionsList = RepeatOptions.values().map { context.getString(it.stringRes) }
            )
        }
    }

    fun onPriorityFieldClicked(context: Context) {
        createEventScreenState.update { state ->
            state.copy(
                bottomSheetType = Priority,
                optionsList = PriorityOptions.values().map { context.getString(it.stringRes) }
            )
        }
    }

    fun onRemindFieldClicked(context: Context) {
        createEventScreenState.update { state ->
            state.copy(
                bottomSheetType = Reminder,
                optionsList = RemindOptions.values().map { context.getString(it.stringRes) }
            )
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