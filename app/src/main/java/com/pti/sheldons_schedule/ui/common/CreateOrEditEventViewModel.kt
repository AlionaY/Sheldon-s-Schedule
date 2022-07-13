package com.pti.sheldons_schedule.ui.common

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.*
import com.pti.sheldons_schedule.data.Options.*
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.service.AlarmBroadcastReceiver
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.toCalendar
import com.pti.sheldons_schedule.util.updateDate
import com.pti.sheldons_schedule.util.updateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CreateOrEditEventViewModel @Inject constructor(
    private val context: Application,
    private val repository: EventRepository
) : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        ScreenState(
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

    val pickedEvent = MutableStateFlow<Event?>(null)
    val startDate = pickedEvent.value?.startDate?.toCalendar() ?: Calendar.getInstance()
    val endDate = pickedEvent.value?.endDate?.toCalendar() ?: Calendar.getInstance()
    val editEventScreenState = MutableStateFlow<ScreenState>(
        ScreenState(
            startDate = startDate,
            endDate = endDate,
            pickedStartTime = startDate,
            pickedEndTime = endDate
        )
    )

    val isPickedTimeValid = MutableSharedFlow<Boolean>()

    private val newEvent = MutableStateFlow<Event?>(null)
    private val scope = CoroutineScope(viewModelScope.coroutineContext) + Dispatchers.IO


    init {
        observeCreateEventScreenState()
        updateEditEventScreenState()
    }

    private fun updateEditEventScreenState() {
        viewModelScope.launch {
            pickedEvent.filterNotNull().collect { event ->
                editEventScreenState.update {
                    it.copy(
                        startDate = event.startDate.toCalendar(),
                        endDate = event.endDate.toCalendar(),
                        title = event.title,
                        description = event.description,
                        priority = event.priority,
                        remind = event.reminder ?: Reminder.DontRemind,
                        repeat = event.repeat ?: Repeat.DontRepeat
                    )
                }
            }
        }
    }

    fun onStartDatePicked(pickedDate: Calendar, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        setStartDate(state, pickedDate)
    }

    private fun setStartDate(
        state: MutableStateFlow<ScreenState>,
        pickedDate: Calendar
    ) {
        val startDate = state.value.startDate.updateDate(
            year = pickedDate.get(Calendar.YEAR),
            month = pickedDate.get(Calendar.MONTH),
            dayOfMonth = pickedDate.get(Calendar.DAY_OF_MONTH)
        )
        state.update { it.copy(startDate = startDate) }

        val endDate = if (startDate > state.value.endDate) {
            (state.value.endDate.clone() as Calendar).apply {
                time = state.value.startDate.time
                add(Calendar.MINUTE, 30)
            }
        } else {
            state.value.endDate
        }
        state.update { it.copy(endDate = endDate) }

        onTimeStartPicked(
            hour = startDate.get(Calendar.HOUR_OF_DAY),
            minutes = startDate.get(Calendar.MINUTE)
        )
    }

    fun onEndDatePicked(calendar: Calendar, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        setEndDate(state, calendar)
    }

    private fun setEndDate(
        state: MutableStateFlow<ScreenState>,
        calendar: Calendar
    ) {
        val endDate = state.value.endDate.updateDate(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        )

        state.update { it.copy(endDate = endDate) }
        onTimeEndPicked(
            hour = endDate.get(Calendar.HOUR_OF_DAY),
            minutes = endDate.get(Calendar.MINUTE)
        )
    }

    fun onTimeStartPicked(hour: Int, minutes: Int, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        setStartTime(state, hour, minutes)
    }

    private fun setStartTime(
        state: MutableStateFlow<ScreenState>,
        hour: Int,
        minutes: Int
    ) {
        val pickedTime = state.value.startDate.updateTime(
            hour = hour,
            minutes = minutes
        )

        state.update { it.copy(pickedStartTime = pickedTime) }
        validatePickedStartTime(state.value)
    }

    fun onTimeEndPicked(hour: Int, minutes: Int, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        setEndTime(state, hour, minutes)
    }

    private fun setEndTime(
        state: MutableStateFlow<ScreenState>,
        hour: Int,
        minutes: Int
    ) {
        val pickedTime = state.value.endDate.updateTime(
            hour = hour,
            minutes = minutes
        )

        state.update { it.copy(pickedEndTime = pickedTime) }
        validatePickedEndTime(state.value)
    }

    fun onRepeatFieldClicked(isEditEventScreen: Boolean = false) = viewModelScope.launch {
        if (isEditEventScreen) {
            editEventScreenState.update { it.copy(options = Repeat.values()) }
        } else {
            createEventScreenState.update { it.copy(options = Repeat.values()) }
        }
    }

    fun onPriorityFieldClicked(isEditEventScreen: Boolean = false) = viewModelScope.launch {
        if (isEditEventScreen) {
            editEventScreenState.update { it.copy(options = Priority.values()) }
        } else {
            createEventScreenState.update { it.copy(options = Priority.values()) }
        }
    }

    fun onRemindFieldClicked(isEditEventScreen: Boolean = false) = viewModelScope.launch {
        if (isEditEventScreen) {
            editEventScreenState.update { it.copy(options = Reminder.values()) }
        } else {
            createEventScreenState.update { it.copy(options = Reminder.values()) }
        }
    }

    fun onTitleEdited(title: String, isEditEventScreen: Boolean = false) {
        if (isEditEventScreen) {
            editEventScreenState.update { it.copy(title = title) }
        } else {
            createEventScreenState.update { it.copy(title = title) }
        }
    }

    fun onDescriptionEdited(description: String, isEditEventScreen: Boolean = false) {
        if (isEditEventScreen) {
            editEventScreenState.update { it.copy(description = description) }
        } else {
            createEventScreenState.update { it.copy(description = description) }
        }
    }

    fun onSaveEventClicked(isEditEventScreen: Boolean = false) {
        if (isEditEventScreen && editEventScreenState.value.title.isNotEmpty()) {
            saveEditedEvent()
        } else {
            if (createEventScreenState.value.title.isNotEmpty()) saveNewEvent()
        }
    }

    private fun saveEditedEvent() = viewModelScope.launch {
        editEventScreenState.value.let { state ->
            val currentTimeInMillis = Calendar.getInstance().timeInMillis
            val creationDate = pickedEvent.value?.creationDate ?: currentTimeInMillis
            val duration = getEventDuration(state)
            val remindAt = getRemindAtTime(state)

            pickedEvent.value = editEventScreenState.value.toEvent(creationDate, duration)

            withContext(scope.coroutineContext) {
                pickedEvent.value?.let { event ->
                    repository.editEvent(event)
                }
            }

            setReminderAlarm(remindAt.timeInMillis)
        }
    }

    private fun getRemindAtTime(state: ScreenState): Calendar {
        val currentDate = Calendar.getInstance()
        val remindTimeInMillis = TimeUnit.MINUTES.toMillis(state.remind.value)
        val remindAt = currentDate.apply {
            timeInMillis = state.startDate.timeInMillis - remindTimeInMillis
        }
        return remindAt
    }

    private fun saveNewEvent() = viewModelScope.launch {
        createEventScreenState.value.let { state ->
            val currentTimeInMillis = Calendar.getInstance().timeInMillis
            val duration = getEventDuration(state)
            val remindAt = getRemindAtTime(state)

            newEvent.value = createEventScreenState.value.toEvent(
                currentTimeInMillis,
                duration
            )
            newEvent.value?.let {
                repository.saveEvent(it)
            }

            setReminderAlarm(remindAt.timeInMillis)
        }
    }

    private fun getEventDuration(state: ScreenState) =
        state.endDate.timeInMillis - state.startDate.timeInMillis

    fun onSelected(options: Options, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        updateOptions(state, options)
    }

    private fun updateOptions(state: MutableStateFlow<ScreenState>, options: Options) {
        when (options) {
            is Repeat -> state.update {
                it.copy(repeat = options, options = null)
            }
            is Priority -> state.update {
                it.copy(priority = options, options = null)
            }
            is Reminder -> state.update {
                it.copy(remind = options, options = null)
            }
        }
    }

    fun onFocusChanged(hasFocus: Boolean, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        checkTitleForError(state, hasFocus)
    }

    private fun checkTitleForError(
        state: MutableStateFlow<ScreenState>,
        hasFocus: Boolean
    ) {
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
                titleFieldState = titleFieldState,
                title = it.title.trim(),
                description = it.description.trim()
            )
        }
    }

    private fun observeCreateEventScreenState() = viewModelScope.launch {
        val calendar = Calendar.getInstance()

        createEventScreenState.collect { state ->
            updateDatePickerStartDate(calendar, state)
        }
    }

    private fun updateDatePickerStartDate(
        calendar: Calendar,
        state: ScreenState
    ) {
        calendar.timeInMillis = state.startDate.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        createEventScreenState.update { it.copy(datePickerStartDate = calendar.timeInMillis) }
    }

    private fun validatePickedStartTime(state: ScreenState) {
        val currentTime = state.calendar
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

        createEventScreenState.update {
            it.copy(startDate = startDate, endDate = endDate)
        }

        viewModelScope.launch {
            isPickedTimeValid.emit(isStartTimeValid)
        }
    }

    private fun validatePickedEndTime(state: ScreenState) {
        val isPickedEndTimeValid = state.pickedEndTime > state.calendar &&
                state.pickedEndTime > state.startDate &&
                state.pickedStartTime < state.pickedEndTime
        val endDate = if (isPickedEndTimeValid) {
            state.pickedEndTime
        } else {
            (state.startDate.clone() as Calendar).apply {
                add(Calendar.MINUTE, 30)
            }
        }

        createEventScreenState.update { it.copy(endDate = endDate) }

        viewModelScope.launch {
            isPickedTimeValid.emit(isPickedEndTimeValid)
        }
    }

    fun resetTimeValidationValue() = viewModelScope.launch {
        isPickedTimeValid.emit(true)
    }

    private fun setReminderAlarm(remindTime: Long) {
        val id = newEvent.value?.creationDate
        val alarmManager = context.getSystemService<AlarmManager>()
        val reminderIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra(Constants.REMINDER_ID, id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id?.toInt() ?: 0,
            reminderIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, remindTime, pendingIntent
        )
    }

    fun getEvent(eventId: Long) = viewModelScope.launch {
        pickedEvent.value = repository.getEvent(eventId)
    }

    fun onDeleteEventClicked() = viewModelScope.launch {
        pickedEvent.value?.let {
            withContext(scope.coroutineContext) {
                repository.deleteEvent(it)
            }
        }
    }
}