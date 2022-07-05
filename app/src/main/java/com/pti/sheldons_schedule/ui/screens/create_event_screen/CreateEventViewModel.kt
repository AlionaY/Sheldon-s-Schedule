package com.pti.sheldons_schedule.ui.screens.create_event_screen

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Parcelable
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.*
import com.pti.sheldons_schedule.data.Options.*
import com.pti.sheldons_schedule.data.Options.Reminder.*
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.service.AlarmBroadcastReceiver
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.updateDate
import com.pti.sheldons_schedule.util.updateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val context: Application,
    private val repository: EventRepository
) : ViewModel() {

    val screenState = MutableStateFlow(
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
    val isPickedTimeValid = MutableSharedFlow<Boolean>()
    val pickedEvent = MutableSharedFlow<Event>()

    private val newEvent = MutableStateFlow<Event?>(null)


    init {
        observeScreenState()
    }

    fun onStartDatePicked(pickedDate: Calendar) {
        screenState.let { state ->
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
    }

    fun onEndDatePicked(calendar: Calendar) {
        screenState.let { state ->
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
    }

    fun onTimeStartPicked(hour: Int, minutes: Int) {
        screenState.let { state ->
            val pickedTime = state.value.startDate.updateTime(
                hour = hour,
                minutes = minutes
            )

            state.update { it.copy(pickedStartTime = pickedTime) }
            validatePickedStartTime(state.value)
        }
    }

    fun onTimeEndPicked(hour: Int, minutes: Int) {
        screenState.let { state ->
            val pickedTime = state.value.endDate.updateTime(
                hour = hour,
                minutes = minutes
            )

            state.update { it.copy(pickedEndTime = pickedTime) }
            validatePickedEndTime(state.value)
        }
    }

    fun onRepeatFieldClicked() = viewModelScope.launch {
        screenState.update { it.copy(options = Repeat.values()) }
    }

    fun onPriorityFieldClicked() = viewModelScope.launch {
        screenState.update { it.copy(options = Priority.values()) }
    }

    fun onRemindFieldClicked() = viewModelScope.launch {
        screenState.update { it.copy(options = Reminder.values()) }
    }

    fun onTitleEdited(title: String) {
        screenState.update { it.copy(title = title) }
    }

    fun onDescriptionEdited(description: String) {
        screenState.update { it.copy(description = description) }
    }

    fun onSaveEventClicked() {
        screenState.value.let { state ->
            val currentDate = Calendar.getInstance().timeInMillis
            val duration = state.endDate.timeInMillis - state.startDate.timeInMillis

            if (state.title.isNotEmpty()) {
                val remindTime = getRemindTimeInMillis(state)
                val remindAt = state.startDate.timeInMillis - remindTime

                saveEvent(
                    currentDate,
                    duration,
                    remindAt
                )
            }
        }
    }

    private fun getRemindTimeInMillis(state: ScreenState) =
        when (state.remind) {
            DontRemind -> DontRemind.value
            Min10 -> TimeUnit.MINUTES.toMillis(Min10.value)
            Min15 -> TimeUnit.MINUTES.toMillis(Min15.value)
            Min30 -> TimeUnit.MINUTES.toMillis(Min30.value)
            Min60 -> TimeUnit.MINUTES.toMillis(Min60.value)
        }

    private fun saveEvent(
        currentDate: Long,
        duration: Long,
        remind: Long
    ) = viewModelScope.launch {
        newEvent.value = screenState.value.toEvent(currentDate, duration)
        newEvent.value?.let {
            repository.saveEvent(it)
        }

        setReminderAlarm(remind)
    }

    fun onSelected(options: Options) {
        when (options) {
            is Repeat -> screenState.update {
                it.copy(repeat = options, options = null)
            }
            is Priority -> screenState.update {
                it.copy(priority = options, options = null)
            }
            is Reminder -> screenState.update {
                it.copy(remind = options, options = null)
            }
        }
    }

    fun onFocusChanged(hasFocus: Boolean) {
        screenState.let { state ->
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
    }

    private fun observeScreenState() = viewModelScope.launch {
        val calendar = Calendar.getInstance()

        screenState.collect { state ->
            updateDatePickerStartDate(calendar, state)
        }
    }

    private fun updateDatePickerStartDate(
        calendar: Calendar,
        state: ScreenState
    ) {
        calendar.timeInMillis = state.startDate.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        screenState.update { it.copy(datePickerStartDate = calendar.timeInMillis) }
    }

    private fun validatePickedStartTime(state: ScreenState) {
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
            it.copy(startDate = startDate, endDate = endDate)
        }

        viewModelScope.launch {
            isPickedTimeValid.emit(isStartTimeValid)
        }
    }

    private fun validatePickedEndTime(state: ScreenState) {
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

        screenState.update { it.copy(endDate = endDate) }

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
            putExtra(Constants.EVENT, newEvent.value as Parcelable)
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
}