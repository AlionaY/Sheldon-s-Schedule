package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.DayOfWeekUI
import com.pti.sheldons_schedule.data.WeekState
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val weekdaysPagingSource: WeekdaysPagingSource
) : ViewModel() {

    companion object {
        const val WEEK_LENGTH = 7
    }

    val state = MutableStateFlow<WeekState?>(null)

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val currentDay = calendar.clone() as Calendar
    val weekList = MutableStateFlow<List<DayOfWeekUI>>(emptyList())

    init {
        getCurrentWeek()
    }

    private fun getCurrentWeek() {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.firstDayOfWeek = Calendar.MONDAY

        (1..WEEK_LENGTH).map {
            weekList.value += DayOfWeekUI(
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                weekDayName = calendar.formatDate(Constants.DAY_NAME_FORMAT)
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        state.value = WeekState(weekList.value)
    }
}