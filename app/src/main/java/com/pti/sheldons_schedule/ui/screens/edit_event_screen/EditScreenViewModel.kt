package com.pti.sheldons_schedule.ui.screens.edit_event_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.db.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val event = MutableStateFlow<Event?>(null)


    fun getEvent(id: Long) {
        viewModelScope.launch {
            event.value = repository.getEvent(id)
        }
    }
}