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
            },
            pickedStartTime = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 10)
            },
            pickedEndTime = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 40)
            }
        )
    )

    val isPickedTimeValid = MutableSharedFlow<Boolean>()
    val isSnackbarActionClicked = MutableSharedFlow<Boolean>()


    init {
        observeScreenState()
    }

    fun onStartDatePicked(pickedDate: Calendar) {
        createEventScreenState.let { state ->
            val endDate = if (pickedDate > state.value.endDate) {
                getDateWithUpdatedTime(
                    date = pickedDate,
                    hour = state.value.startDate.get(Calendar.HOUR_OF_DAY),
                    minutes = state.value.startDate.get(Calendar.MINUTE),
                    minutesToAdd = 40
                )
            } else {
                state.value.endDate
            }

            val startDate = getCalendarWithUpdatedDate(
                date = state.value.startDate,
                year = pickedDate.get(Calendar.YEAR),
                month = pickedDate.get(Calendar.MONTH),
                dayOfMonth = pickedDate.get(Calendar.DAY_OF_MONTH)
            )

            updateCalendar(startDate, endDate)
            onTimeStartPicked(
                hour = state.value.startDate.get(Calendar.HOUR_OF_DAY),
                minutes = state.value.startDate.get(Calendar.MINUTE)
            )
        }
    }

    private fun getDateWithUpdatedTime(
        date: Calendar,
        hour: Int,
        minutes: Int,
        minutesToAdd: Int? = null
    ) = (date.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minutes)
        if (minutesToAdd != null) add(Calendar.MINUTE, minutesToAdd)
    }

    private fun getCalendarWithUpdatedDate(
        date: Calendar,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) = (date.clone() as Calendar).apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    private fun updateCalendar(startDate: Calendar, endDate: Calendar) {
        createEventScreenState.update {
            it.copy(startDate = startDate, endDate = endDate)
        }
    }

    fun onEndDatePicked(calendar: Calendar) {
        createEventScreenState.value.let { state ->
            val isEndDateValid = calendar > state.startDate
            val pickedDate = getCalendarWithUpdatedDate(
                date = state.endDate,
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            )
            val endDate = if (isEndDateValid) {
                pickedDate
            } else {
                getDateWithUpdatedTime(
                    date = pickedDate,
                    hour = state.startDate.get(Calendar.HOUR_OF_DAY),
                    minutes = state.startDate.get(Calendar.MINUTE),
                    minutesToAdd = 30
                )
            }

            updateCalendar(startDate = state.startDate, endDate = endDate)
            onTimeEndPicked(
                hour = endDate.get(Calendar.HOUR_OF_DAY),
                minutes = endDate.get(Calendar.MINUTE)
            )
        }
    }

    fun onTimeStartPicked(hour: Int, minutes: Int) {
        createEventScreenState.let { state ->
            val pickedTime = getDateWithUpdatedTime(
                date = state.value.startDate,
                hour = hour,
                minutes = minutes
            )

            state.update { it.copy(pickedStartTime = pickedTime) }
            validatePickedStartTime(state.value)
        }
    }

    fun onTimeEndPicked(hour: Int, minutes: Int) {
        createEventScreenState.let { state ->
            val pickedTime = getDateWithUpdatedTime(
                date = state.value.endDate,
                hour = hour,
                minutes = minutes
            )

            state.update { it.copy(pickedEndTime = pickedTime) }
            validatePickedEndTime(state.value)
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

    private fun validatePickedStartTime(state: CreateEventScreenState) {
        val currentTime = state.calendar
        val isStartTimeValid = state.pickedStartTime > currentTime &&
                state.pickedStartTime >= state.startDate
        val startDate = if (isStartTimeValid) state.pickedStartTime else state.startDate
        val endDate = if (isStartTimeValid) {
            getDateWithUpdatedTime(
                date = state.endDate,
                hour = state.pickedStartTime.get(Calendar.HOUR_OF_DAY),
                minutes = state.pickedStartTime.get(Calendar.MINUTE),
                minutesToAdd = 30
            )
        } else {
            state.endDate
        }

        createEventScreenState.update {
            it.copy(
                startDate = startDate,
                endDate = endDate
            )
        }

        viewModelScope.launch {
            isPickedTimeValid.emit(isStartTimeValid)
        }
    }

    private fun validatePickedEndTime(state: CreateEventScreenState) {
        val isEndTimeValid = state.pickedEndTime > state.calendar &&
                state.pickedEndTime > state.startDate &&
                state.pickedStartTime < state.pickedEndTime
        val endDate = if (isEndTimeValid) state.pickedEndTime else state.endDate

        createEventScreenState.update {
            it.copy(endDate = endDate)
        }

        viewModelScope.launch {
            isPickedTimeValid.emit(isEndTimeValid)
        }
    }

    fun resetTimeValidationValue() = viewModelScope.launch {
        isPickedTimeValid.emit(true)
    }
}