package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pti.sheldons_schedule.data.WeekState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar.*
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val weekState = MutableStateFlow<WeekState?>(null)

    val calendar = getInstance().apply {
        set(HOUR_OF_DAY, 0)
        set(MINUTE, 0)
        set(SECOND, 0)
        set(MILLISECOND, 0)
        set(DAY_OF_WEEK, MONDAY)
        firstDayOfWeek = MONDAY
    }

    val source = Pager(PagingConfig(7)) {
        WeekdaysPagingSource(calendar)
    }.flow.cachedIn(viewModelScope)


    fun onPageOffsetChanged(offset: Float) {
        calendar.add(DAY_OF_YEAR, offset.roundToInt() * 7)
    }
}