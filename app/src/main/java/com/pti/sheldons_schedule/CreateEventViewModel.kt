package com.pti.sheldons_schedule

import android.app.Application
import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.*
import com.pti.sheldons_schedule.data.BottomSheetType.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        CreateEventScreenState(
            startDate = Calendar.getInstance(),
            endDate = Calendar.getInstance(),
            repeatOptionsList = RepeatOptions.values().map { application.getString(it.stringRes) },
            priorityOptionsList = PriorityOptions.values().map { application.getString(it.stringRes) },
            remindOptionsList = RemindOptions.values().map { application.getString(it.stringRes) }
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
        createEventScreenState.update { it.copy(bottomSheetType = Repeat) }
    }

    fun onPriorityFieldClicked() {
        createEventScreenState.update { it.copy(bottomSheetType = Priority) }
    }

    fun onRemindFieldClicked() {
        createEventScreenState.update { it.copy(bottomSheetType = Reminder) }
    }
}