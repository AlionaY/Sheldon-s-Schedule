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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    val pickedEvent = MutableStateFlow<FullEvent?>(null)
    val startDate = pickedEvent.value?.event?.startDate?.toCalendar() ?: Calendar.getInstance()
    val endDate = pickedEvent.value?.event?.endDate?.toCalendar() ?: Calendar.getInstance()
    val editEventScreenState = MutableStateFlow(
        ScreenState(
            startDate = startDate,
            endDate = endDate,
            pickedStartTime = startDate,
            pickedEndTime = endDate
        )
    )

    val isPickedTimeValid = MutableSharedFlow<Boolean>()

    private val newEvent = MutableStateFlow<FullEvent?>(null)


    init {
        observeScreenState(createEventScreenState)
        observeScreenState(editEventScreenState)
        updateEditEventScreenState()
    }

    private fun updateEditEventScreenState() {
        viewModelScope.launch {
            pickedEvent.filterNotNull().collect { event ->
                editEventScreenState.update {
                    it.copy(
                        startDate = event.event.startDate.toCalendar(),
                        endDate = event.event.endDate.toCalendar(),
                        title = event.event.title,
                        description = event.event.description,
                        priority = event.event.priority,
                        repeat = event.event.repeat,
                        remind = event.reminder.toRemind()
                    )
                }
            }
        }
    }

    fun onStartDatePicked(pickedDate: Calendar, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        setStartDate(state, pickedDate, isEditEventScreen)
    }

    private fun setStartDate(
        state: MutableStateFlow<ScreenState>,
        pickedDate: Calendar,
        isEditEventScreen: Boolean
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
            minutes = startDate.get(Calendar.MINUTE),
            isEditEventScreen = isEditEventScreen
        )
    }

    fun onEndDatePicked(calendar: Calendar, isEditEventScreen: Boolean = false) {
        val state = if (isEditEventScreen) editEventScreenState else createEventScreenState
        setEndDate(state, calendar, isEditEventScreen)
    }

    private fun setEndDate(
        state: MutableStateFlow<ScreenState>,
        calendar: Calendar,
        isEditEventScreen: Boolean
    ) {
        val endDate = state.value.endDate.updateDate(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        )

        state.update { it.copy(endDate = endDate) }
        onTimeEndPicked(
            hour = endDate.get(Calendar.HOUR_OF_DAY),
            minutes = endDate.get(Calendar.MINUTE),
            isEditEventScreen = isEditEventScreen
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
        validatePickedStartTime(state)
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
        validatePickedEndTime(state)
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
            editEventScreenState.update { it.copy(options = Remind.values()) }
        } else {
            createEventScreenState.update { it.copy(options = Remind.values()) }
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

    private fun saveEditedEvent() = viewModelScope.launch(Dispatchers.IO) {
        editEventScreenState.value.let { state ->
            val currentTimeInMillis = Calendar.getInstance().timeInMillis
            val creationDate = pickedEvent.value?.event?.creationDate ?: currentTimeInMillis
            val duration = getEventDuration(state)
            val remindAt = getRemindAtTime(state)
            val reminder = Reminder(creationDate, state.remind.alias)

            pickedEvent.value = editEventScreenState.value.toEvent(
                creationDate,
                duration,
                reminder
            )

            pickedEvent.value?.let {
                repository.editEvent(it)
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
            val creationDate = Calendar.getInstance().timeInMillis
            val duration = getEventDuration(state)
            val remindAt = getRemindAtTime(state)
            val reminder = Reminder(creationDate, state.remind.alias)

            newEvent.value = createEventScreenState.value.toEvent(
                creationDate,
                duration,
                reminder
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
            is Remind -> state.update {
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

    private fun observeScreenState(state: MutableStateFlow<ScreenState>) =
        viewModelScope.launch {
            val calendar = Calendar.getInstance()

            calendar.timeInMillis = state.value.startDate.timeInMillis
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            state.update { it.copy(datePickerStartDate = calendar.timeInMillis) }
        }

    private fun validatePickedStartTime(state: MutableStateFlow<ScreenState>) {
        val currentTime = state.value.calendar
        val isStartTimeValid = state.value.pickedStartTime > currentTime
        val startDate = if (isStartTimeValid) state.value.pickedStartTime else state.value.startDate
        val endDate = if (isStartTimeValid) {
            state.value.endDate.updateTime(
                hour = state.value.pickedStartTime.get(Calendar.HOUR_OF_DAY),
                minutes = state.value.pickedStartTime.get(Calendar.MINUTE),
                minutesToAdd = 30
            )
        } else {
            state.value.endDate
        }

        state.update {
            it.copy(startDate = startDate, endDate = endDate)
        }

        viewModelScope.launch {
            isPickedTimeValid.emit(isStartTimeValid)
        }
    }

    private fun validatePickedEndTime(state: MutableStateFlow<ScreenState>) {
        val isPickedEndTimeValid = state.value.pickedEndTime > state.value.calendar &&
                state.value.pickedEndTime > state.value.startDate &&
                state.value.pickedStartTime < state.value.pickedEndTime
        val endDate = if (isPickedEndTimeValid) {
            state.value.pickedEndTime
        } else {
            (state.value.startDate.clone() as Calendar).apply {
                add(Calendar.MINUTE, 30)
            }
        }

        state.update { it.copy(endDate = endDate) }

        viewModelScope.launch {
            isPickedTimeValid.emit(isPickedEndTimeValid)
        }
    }

    fun resetTimeValidationValue() = viewModelScope.launch {
        isPickedTimeValid.emit(true)
    }

    private fun setReminderAlarm(remindTime: Long) {
        val id = newEvent.value?.event?.creationDate
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

    fun onDeleteEventClicked() = viewModelScope.launch(Dispatchers.IO) {
        pickedEvent.value?.let {
            repository.deleteEvent(it)
        }
    }
}