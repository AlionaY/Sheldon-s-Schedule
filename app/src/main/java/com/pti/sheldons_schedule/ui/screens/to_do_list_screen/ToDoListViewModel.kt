package com.pti.sheldons_schedule.ui.screens.to_do_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.data.EventWithToDoList
import com.pti.sheldons_schedule.db.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val event = MutableStateFlow<EventWithToDoList?>(null)
    val addTextField = MutableSharedFlow<Boolean>()


    fun getEvent(eventId: Long) = viewModelScope.launch {
        event.value = repository.getEvent(eventId)
    }

    fun onAddItemClicked() = viewModelScope.launch {
        addTextField.emit(true)
    }
}