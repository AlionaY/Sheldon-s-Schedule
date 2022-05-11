package com.pti.sheldons_schedule

import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.CreateEventScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor() : ViewModel() {

    val createEventScreenState = MutableStateFlow(
        CreateEventScreenState(
            startDate = Calendar.getInstance()
        )
    )

    fun onDatePicked(calendar: Calendar) {
        createEventScreenState.update { it.copy(startDate = calendar) }
    }
}