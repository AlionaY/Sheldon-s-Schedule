package com.pti.sheldons_schedule.ui.screens.create_event_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.data.Options.*
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
    @ApplicationContext context: Context,
    private val repository: EventRepository
) : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        CreateEventScreenState(
            startDate = Calendar.getInstance(),
            endDate = Calendar.getInstance(),
            selectedPriority = context.getString(R.string.priority_low)
        )
    )

    val allEvents = MutableStateFlow<List<Event>>(emptyList())
    val newEvent = MutableStateFlow(
        Event(
            creationDate = Calendar.getInstance().formatDate(Constants.DATE_FORMAT_ISO_8601),
            startDate = Calendar.getInstance().formatDate(Constants.DATE_FORMAT),
            endDate = Calendar.getInstance().formatDate(Constants.DATE_FORMAT)
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
        newEvent.update { it.copy(startDate = createEventScreenState.value.formattedStartDate) }
    }

    fun onEndDatePicked(calendar: Calendar) {
        createEventScreenState.update {
            it.copy(endDate = (it.endDate.clone() as Calendar).apply {
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            })
        }
        newEvent.update { it.copy(endDate = createEventScreenState.value.formattedEndDate) }
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
            state.copy(options = RepeatOptions)
        }
    }

    fun onPriorityFieldClicked() {
        createEventScreenState.update { state ->
            state.copy(options = PriorityOptions)
        }
    }

    fun onRemindFieldClicked() {
        createEventScreenState.update { state ->
            state.copy(options = RemindOptions)
        }
    }

    fun onSelected(options: Options?, string: String) {
        when (options) {
            RepeatOptions -> createEventScreenState.update { it.copy(selectedRepeat = string) }
            RemindOptions -> createEventScreenState.update { it.copy(selectedRemind = string) }
            PriorityOptions -> createEventScreenState.update { it.copy(selectedPriority = string) }
            else -> { }
        }
    }

    fun onTitleEdited(string: String) {
        createEventScreenState.update { it.copy(title = string) }
        newEvent.update { it.copy(title = string) }
    }

    fun onDescriptionEdited(string: String) {
        createEventScreenState.update { it.copy(description = string) }
        newEvent.update { it.copy(description = string) }
    }

    fun getAllEvents() = viewModelScope.launch {
        allEvents.value = repository.getAllEvents()
    }

    fun saveEvent(event: Event) = viewModelScope.launch {
        repository.saveEvent(event)
    }
}