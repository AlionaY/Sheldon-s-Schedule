package com.pti.sheldons_schedule.ui.screens.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pti.sheldons_schedule.data.DayOfWeek
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Week
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.convertToCalendar
import com.pti.sheldons_schedule.util.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@OptIn(ObsoleteCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    companion object {
        private const val MINUTES_IN_HOUR_FLOAT = 60f
        private const val MINUTE_LONG = 60000L
    }

    private lateinit var allEvents : MutableStateFlow<List<Event>>

    val dayEvent = MutableStateFlow<Event?>(null)
    val weekEvents = MutableStateFlow<Set<Event>>(emptySet())
    val currentCalendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    val weeks by lazy {
        Pager(PagingConfig(1)) {
            WeekdaysPagingSource(allEvents.value)
        }.flow.cachedIn(viewModelScope)
    }
    val ticker = ticker(
        delayMillis = MINUTE_LONG,
        initialDelayMillis = 0,
        context = viewModelScope.coroutineContext
    ).receiveAsFlow().map {
        val calendar = Calendar.getInstance()
        val currentMinutes = calendar.get(Calendar.MINUTE)
        val currentMinutesInPercent = (currentMinutes / MINUTES_IN_HOUR_FLOAT)
        currentMinutesInPercent
    }


    init {
        viewModelScope.launch {
            allEvents = MutableStateFlow(repository.getAllEvents().sortedBy { it.startDate })
        }

        viewModelScope.launch {
            dayEvent.collect {
                Log.d("$$$", "week events $it")
            }
        }
    }

    fun onWeekGet(week: Week?) {
        Log.d("$$$", "current week $week")
    }

    fun onDayParamsGet(dayOfWeek: DayOfWeek, hourItem: Int) {
        viewModelScope.launch {
            allEvents.collect {
                it.forEach { event ->
                    if (event.startDate.convertToCalendar().formatDate(
                            DATE_FORMAT
                        ) == dayOfWeek.day.convertToCalendar().formatDate(DATE_FORMAT)
                    ) {
                        weekEvents.value += event
                        Log.d("$$$", "event filter ${event.startDate}")
                    }
                }
            }
        }

        viewModelScope.launch {
            weekEvents.collect {
                it.forEach { event ->
                    if (
                        (event.startDate.convertToCalendar().formatDate(
                            DATE_FORMAT
                        ) == dayOfWeek.day.convertToCalendar().formatDate(DATE_FORMAT)) &&
                        event.startDate.convertToCalendar().formatDate("HH").toInt() == hourItem) {
                        dayEvent.value = event
                    }
                }
            }
        }
    }
}