package com.pti.sheldons_schedule.ui.screens.create_event_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.data.Options.*
import com.pti.sheldons_schedule.data.toEvent
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: EventRepository
) : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        CreateEventScreenState(
            startDate = Calendar.getInstance(),
            endDate = Calendar.getInstance()
        )
    )


    init {
        onStartDateGet()
    }

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
            it.copy(
                startDate = (it.startDate.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minutes)
                }
            )
        }
    }

    fun onTimeEndPicked(hour: Int, minutes: Int) {
        createEventScreenState.let { state ->
            state.update {
                it.copy(
                    endDate = (it.endDate.clone() as Calendar).apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minutes)
                    }
                )
            }
        }
    }

    private fun gerTimePickerErrorText(hourValidated: Boolean): String? {
        createEventScreenState.let { state ->
            val isTheSameDay =
                state.value.formattedEndDate == state.value.formattedStartDate

            val errorText = if (hourValidated && isTheSameDay) {
                context.getString(R.string.time_picker_error_message)
            } else {
                null
            }
            return errorText
        }
    }

    fun onRepeatFieldClicked() = viewModelScope.launch {
        createEventScreenState.update { it.copy(options = Repeat.values()) }
    }

    fun onPriorityFieldClicked() = viewModelScope.launch {
        createEventScreenState.update { it.copy(options = Priority.values()) }
    }

    fun onRemindFieldClicked() = viewModelScope.launch {
        createEventScreenState.update { it.copy(options = Reminder.values()) }
    }

    fun onTitleEdited(string: String) {
        createEventScreenState.update { it.copy(title = string) }
    }

    fun onDescriptionEdited(string: String) {
        createEventScreenState.update { it.copy(description = string) }
    }

    fun onSaveEventClicked() = viewModelScope.launch {
        val currentDate = Calendar.getInstance().formatDate(Constants.DATE_FORMAT)
        repository.saveEvent(
            createEventScreenState.value.toEvent(currentDate)
        )
    }

    fun onSelected(options: Options) {
        when (options) {
            is Repeat -> createEventScreenState.update {
                it.copy(repeat = options, options = null)
            }
            is Priority -> createEventScreenState.update {
                it.copy(priority = options, options = null)
            }
            is Reminder -> createEventScreenState.update {
                it.copy(remind = options, options = null)
            }
        }
    }

    fun onFocusChanged(hasFocus: Boolean) {
        val errorText = if (createEventScreenState.value.title.isEmpty() && !hasFocus) {
            context.getText(R.string.title_error_message).toString()
        } else {
            null
        }

        createEventScreenState.update { it.copy(titleErrorText = errorText) }
    }

    private fun onStartDateGet() = viewModelScope.launch {
        val calendar = Calendar.getInstance()

        createEventScreenState.collect { state ->
            calendar.timeInMillis = state.startDate.timeInMillis
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            createEventScreenState.update { it.copy(datePickerStartDate = calendar.timeInMillis) }


            val currentHour = createEventScreenState.value.calendar.get(Calendar.HOUR_OF_DAY)
            val startHour = createEventScreenState.value.startDate.get(Calendar.HOUR_OF_DAY)
            val endHour = createEventScreenState.value.endDate.get(Calendar.HOUR_OF_DAY)
            val startHourValidated = currentHour > startHour
            val endTimeValidated = currentHour > endHour || startHour > endHour

            createEventScreenState.update {
                it.copy(
                    endTimeErrorText = gerTimePickerErrorText(endTimeValidated),
                    startTimeErrorText = gerTimePickerErrorText(startHourValidated)
                )
            }
        }
    }
}