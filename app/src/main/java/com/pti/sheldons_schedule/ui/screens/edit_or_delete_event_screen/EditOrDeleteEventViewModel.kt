package com.pti.sheldons_schedule.ui.screens.edit_or_delete_event_screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.EditOrDeleteEventScreenState
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.data.TitleFieldState
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.updateDate
import com.pti.sheldons_schedule.util.updateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditOrDeleteEventViewModel @Inject constructor(
    private val repository: EventRepository,
    private val context: Application
) : ViewModel() {

    val pickedEvent = MutableSharedFlow<Event>()
    val screenState = MutableStateFlow<EditOrDeleteEventScreenState?>(null)
    val isPickedTimeValid = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            pickedEvent.collect { event ->

                Log.d("###", "edit screen start ${event.startDate}, end ${event.endDate}")
//                screenState.update {
//                    it?.copy(
//                        title = event.title,
//                        description = event.description,
//                        startDate = ,
//                        endDate = ,
//                        remind = event.reminder ?: Options.Reminder.DontRemind,
//                        repeat = event.repeat ?: Options.Repeat.DontRepeat,
//                        priority = event.priority
//                    )
//                }
            }
        }
    }

    fun getPickedEvent(id: String) = viewModelScope.launch {
        pickedEvent.emit(repository.getEvent(id))
    }

    fun onTitleEdited(string: String) {
        screenState.update { it?.copy(title = string) }
    }

    fun onDescriptionEdited(string: String) {
        screenState.update { it?.copy(description = string) }
    }

    fun onFocusChanged(hasFocus: Boolean) {
        screenState.let { state ->
            val titleErrorText = if (state.value?.title.isNullOrEmpty() && !hasFocus) {
                context.getText(R.string.title_error_message).toString()
            } else {
                null
            }

            val titleFieldState = if (state.value?.title.isNullOrEmpty() && !hasFocus) {
                TitleFieldState.Error
            } else {
                TitleFieldState.Normal
            }

            state.update {
                it?.copy(
                    titleErrorText = titleErrorText,
                    titleFieldState = titleFieldState,
                    title = it.title.trim(),
                    description = it.description.trim()
                )
            }
        }
    }

    fun onStartDatePicked(pickedDate: Calendar) {
        screenState.value?.let { state ->
            val startDate = state.startDate.updateDate(
                year = pickedDate.get(Calendar.YEAR),
                month = pickedDate.get(Calendar.MONTH),
                dayOfMonth = pickedDate.get(Calendar.DAY_OF_MONTH)
            )
            screenState.update { it?.copy(startDate = startDate) }

            val endDate = if (startDate > state.endDate) {
                (state.endDate.clone() as Calendar).apply {
                    time = state.startDate.time
                    add(Calendar.MINUTE, 30)
                }
            } else {
                state.endDate
            }
            screenState.update { it?.copy(endDate = endDate) }

            onTimeStartPicked(
                hour = startDate.get(Calendar.HOUR_OF_DAY),
                minutes = startDate.get(Calendar.MINUTE)
            )
        }
    }

    fun onTimeStartPicked(hour: Int, minutes: Int) {
        screenState.value?.let { state ->
            val pickedTime = state.startDate.updateTime(
                hour = hour,
                minutes = minutes
            )

            screenState.update { it?.copy(pickedStartTime = pickedTime) }
            validatePickedStartTime(state)
        }
    }

    private fun validatePickedStartTime(state: EditOrDeleteEventScreenState) {
        val currentTime = state.currentDate
        val isStartTimeValid = state.pickedStartTime > currentTime
        val startDate = if (isStartTimeValid) state.pickedStartTime else state.startDate
        val endDate = if (isStartTimeValid) {
            state.endDate.updateTime(
                hour = state.pickedStartTime.get(Calendar.HOUR_OF_DAY),
                minutes = state.pickedStartTime.get(Calendar.MINUTE),
                minutesToAdd = 30
            )
        } else {
            state.endDate
        }

        screenState.update {
            it?.copy(startDate = startDate, endDate = endDate)
        }

        viewModelScope.launch {
            isPickedTimeValid.emit(isStartTimeValid)
        }
    }

    fun onEndDatePicked(calendar: Calendar) {
        screenState.value?.let { state ->
            val endDate = state.endDate.updateDate(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            )

            screenState.update { it?.copy(endDate = endDate) }

            onTimeEndPicked(
                hour = endDate.get(Calendar.HOUR_OF_DAY),
                minutes = endDate.get(Calendar.MINUTE)
            )
        }
    }

    fun onTimeEndPicked(hour: Int, minutes: Int) {
        screenState.value?.let { state ->
            val pickedTime = state.endDate.updateTime(
                hour = hour,
                minutes = minutes
            )

            screenState.update { it?.copy(pickedEndTime = pickedTime) }
            validatePickedEndTime(state)
        }
    }

    private fun validatePickedEndTime(state: EditOrDeleteEventScreenState) {
        val isPickedEndTimeValid = state.pickedEndTime > state.currentDate &&
                state.pickedEndTime > state.startDate &&
                state.pickedStartTime < state.pickedEndTime
        val endDate = if (isPickedEndTimeValid) {
            state.pickedEndTime
        } else {
            (state.startDate.clone() as Calendar).apply {
                add(Calendar.MINUTE, 30)
            }
        }

        screenState.update { it?.copy(endDate = endDate) }

        viewModelScope.launch {
            isPickedTimeValid.emit(isPickedEndTimeValid)
        }
    }

    fun resetTimeValidationValue() = viewModelScope.launch {
        isPickedTimeValid.emit(true)
    }

    fun onRepeatFieldClicked() = viewModelScope.launch {
        screenState.update { it?.copy(options = Options.Repeat.values()) }
    }

    fun onPriorityFieldClicked() = viewModelScope.launch {
        screenState.update { it?.copy(options = Options.Priority.values()) }
    }

    fun onRemindFieldClicked() = viewModelScope.launch {
        screenState.update { it?.copy(options = Options.Reminder.values()) }
    }

    fun onSelected(options: Options) {
        when (options) {
            is Options.Repeat -> screenState.update {
                it?.copy(repeat = options, options = null)
            }
            is Options.Priority -> screenState.update {
                it?.copy(priority = options, options = null)
            }
            is Options.Reminder -> screenState.update {
                it?.copy(remind = options, options = null)
            }
        }
    }
}