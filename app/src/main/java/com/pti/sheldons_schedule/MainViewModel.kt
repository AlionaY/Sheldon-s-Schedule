package com.pti.sheldons_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pti.sheldons_schedule.data.Date
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val date = MutableStateFlow<Date?>(null)


    init {
        getCurrentDate()
        formatPickedDate()
    }

    private fun getCurrentDate() {
        if (date.value == null) {
            val calendar = getCalendar()
            date.value = Date(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    private fun getCalendar(): Calendar {
        return Calendar.getInstance()
    }

    fun onPickedDate(year: Int, month: Int, day: Int) {
        date.update {
            it?.copy(
                year = year,
                month = month,
                day = day,
            )
        }
    }

    private fun formatPickedDate() = viewModelScope.launch {
        date.filterNotNull().collect {
            date.update { it?.copy(formattedDate = it.toFormat()) }
        }
    }
}