package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.data.FullEvent
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.ToDo
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.toCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val event = MutableStateFlow<FullEvent?>(null)
    val startDate = event.value?.event?.startDate?.toCalendar() ?: Calendar.getInstance()
    val endDate = event.value?.event?.endDate?.toCalendar() ?: Calendar.getInstance()

    val screenState = MutableStateFlow(
        ScreenState(
            startDate = startDate,
            endDate = endDate,
            pickedStartTime = startDate,
            pickedEndTime = endDate
        )
    )


    fun getEvent(eventId: Long) = viewModelScope.launch {
        event.value = repository.getEvent(eventId)
    }

    fun onCheckedChange(isChecked: Boolean, todo: ToDo) {

    }
}