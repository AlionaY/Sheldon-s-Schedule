package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.WeekState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val pagingSource: EventPagingSource
) : ViewModel() {

    val weekState = MutableStateFlow(
        WeekState(calendar = Calendar.getInstance(), currentDay = Calendar.getInstance())
    )


    init {
        getFirstDayOfTheWeek()
    }

    private fun getFirstDayOfTheWeek() {
        val currDay = weekState.value.calendar.get(Calendar.DAY_OF_WEEK) - 2
        val days = if (currDay == -1) -6 else currDay * (-1)
        weekState.value.updatedDate.add(Calendar.DAY_OF_MONTH, days)
        weekState.update { it.copy(monday = (it.updatedDate.clone() as Calendar)) }

        weekState.value.updatedDate.add(Calendar.DAY_OF_MONTH, 1)
        weekState.update { it.copy(tuesday = (it.updatedDate.clone() as Calendar)) }

        weekState.value.updatedDate.add(Calendar.DAY_OF_MONTH, 1)
        weekState.update { it.copy(wednesday = (it.updatedDate.clone() as Calendar)) }

        weekState.value.updatedDate.add(Calendar.DAY_OF_MONTH, 1)
        weekState.update { it.copy(thursday = (it.updatedDate.clone() as Calendar)) }

        weekState.value.updatedDate.add(Calendar.DAY_OF_MONTH, 1)
        weekState.update { it.copy(friday = (it.updatedDate.clone() as Calendar)) }

        weekState.value.updatedDate.add(Calendar.DAY_OF_MONTH, 1)
        weekState.update { it.copy(saturday = (it.updatedDate.clone() as Calendar)) }

        weekState.value.updatedDate.add(Calendar.DAY_OF_MONTH, 1)
        weekState.update { it.copy(sunday = (it.updatedDate.clone() as Calendar)) }
    }
}