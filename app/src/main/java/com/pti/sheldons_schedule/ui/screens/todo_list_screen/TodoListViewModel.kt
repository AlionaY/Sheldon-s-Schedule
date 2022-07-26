package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.data.FullEvent
import com.pti.sheldons_schedule.data.ToDo
import com.pti.sheldons_schedule.db.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val event = MutableStateFlow<FullEvent?>(null)


    fun getEvent(eventId: Long) = viewModelScope.launch {
        event.value = repository.getEvent(eventId)
    }

    fun onCheckedChange(isChecked: Boolean, index: Int) {
        updateTodoList(index, isChecked)
        saveUpdatedEvent()
    }

    private fun updateTodoList(index: Int, isChecked: Boolean) {
        val list = event.value?.toDoList?.toMutableList()
        list?.set(
            index,
            ToDo(
                title = list[index].title,
                completed = isChecked,
                itemId = list[index].itemId,
                eventId = list[index].eventId
            )
        )

        event.update { it?.copy(toDoList = list?.toList() ?: emptyList()) }
    }

    private fun saveUpdatedEvent() {
        viewModelScope.launch {
            event.value?.let { repository.saveEvent(it) }
        }
    }
}