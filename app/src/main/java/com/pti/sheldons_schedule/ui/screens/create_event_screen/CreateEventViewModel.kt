package com.pti.sheldons_schedule.ui.screens.create_event_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.data.Options.*
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.data.toEvent
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
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
            startDate = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 10)
            },
            endDate = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 40)
            }
        )
    )
    val showTimePicker = MutableSharedFlow<Boolean>()


    init {
        observeScreenState()
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
        validateStartPickedTime(hour, minutes)
    }

    fun onTimeEndPicked(hour: Int, minutes: Int) {
        validateEndPickedTime(hour, minutes)
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

    fun onSaveEventClicked() {
        createEventScreenState.value.let { state ->
            val currentDate = Calendar.getInstance().formatDate(Constants.DATE_FORMAT)
            val duration = state.endDate.timeInMillis - state.startDate.timeInMillis

            if (state.title.isNotEmpty()) saveEvent(currentDate, duration)
        }
    }

    private fun saveEvent(currentDate: String, duration: Long) = viewModelScope.launch {
        repository.saveEvent(
            createEventScreenState.value.toEvent(currentDate, duration)
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
        createEventScreenState.let { state ->
            val titleErrorText = if (state.value.title.isEmpty() && !hasFocus) {
                context.getText(R.string.title_error_message).toString()
            } else {
                null
            }

            val titleFieldState = if (state.value.title.isEmpty() && !hasFocus) {
                TitleFieldState.Error
            } else {
                TitleFieldState.Normal
            }

            state.update {
                it.copy(
                    titleErrorText = titleErrorText,
                    titleFieldState = titleFieldState
                )
            }
        }
    }

    private fun observeScreenState() = viewModelScope.launch {
        val calendar = Calendar.getInstance()

        createEventScreenState.collect { state ->
            updateDatePickerStartDate(calendar, state)
        }
    }

    private fun updateDatePickerStartDate(
        calendar: Calendar,
        state: CreateEventScreenState
    ) {
        calendar.timeInMillis = state.startDate.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        createEventScreenState.update { it.copy(datePickerStartDate = calendar.timeInMillis) }
    }

    private fun validateStartPickedTime(hour: Int, minutes: Int) {
        createEventScreenState.let { state ->
            val currentTime = state.value.calendar.formatDate(Constants.TIME_FORMAT)
            val isTheSameDay = state.value.formattedEndDate == state.value.formattedStartDate
            val pickedTime = "$hour:$minutes"
            val isStartTimeValid = currentTime < pickedTime && isTheSameDay

            val startDate = if (isStartTimeValid) {
                (state.value.startDate.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minutes)
                }
            } else {
                state.value.startDate
            }

            val endDate = if (isStartTimeValid) {
                (state.value.endDate.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minutes)
                    add(Calendar.MINUTE, 30)
                }
            } else {
                state.value.endDate
            }

            createEventScreenState.update {
                it.copy(
                    startDate = startDate,
                    endDate = endDate,
                    isPickedTimeValid = isStartTimeValid
                )
            }
        }
    }

    private fun validateEndPickedTime(hour: Int, minutes: Int) {
        createEventScreenState.let { state ->
            val currentTime = state.value.calendar.formatDate(Constants.TIME_FORMAT)
            val isTheSameDay = state.value.formattedEndDate == state.value.formattedStartDate
            val pickedTime = "$hour:$minutes"
            val isEndTimeValid = (currentTime > pickedTime ||
                    state.value.formattedStartTime > pickedTime) &&
                    isTheSameDay
            val endDate = if (isEndTimeValid) {
                (state.value.endDate.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minutes)
                }
            } else {
                state.value.endDate
            }

            createEventScreenState.update {
                it.copy(
                    isPickedTimeValid = isEndTimeValid,
                    endDate = endDate
                )
            }
        }
    }

    fun onSnakbarActionClicked() = viewModelScope.launch {
        showTimePicker.emit(true)
    }
}