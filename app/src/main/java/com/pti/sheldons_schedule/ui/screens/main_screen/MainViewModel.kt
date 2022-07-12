package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.db.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
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

    private val allEvents = MutableStateFlow<List<Event>>(emptyList())

    var weeks = Pager(PagingConfig(1)) {
            WeekdaysPagingSource(allEvents.value)
        }.flow.cachedIn(viewModelScope)

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
        getEventsList()
    }

    private fun getEventsList() {
        viewModelScope.launch {
            allEvents.value = repository.getAllEvents().sortedBy { it.startDate }
        }
    }

    fun reload() {
        weeks = Pager(PagingConfig(1)) {
            WeekdaysPagingSource(allEvents.value)
        }.flow.cachedIn(viewModelScope)
    }
}