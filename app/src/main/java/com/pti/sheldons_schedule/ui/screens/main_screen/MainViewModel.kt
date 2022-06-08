package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val MINUTES_IN_HOUR_FLOAT = 60f
        private const val MINUTE_LONG = 60000L
    }

    val weeks = Pager(PagingConfig(1)) {
        WeekdaysPagingSource()
    }.flow.cachedIn(viewModelScope)

    val timelinePadding = MutableStateFlow(0f)

    private val timerFlow = MutableStateFlow<Calendar>(Calendar.getInstance())


    init {
        startTickerFlow()
        observeTimeFlow()
    }

    private fun startTickerFlow() {
        viewModelScope.launch {
            tickerFlow()
        }
    }

    private fun observeTimeFlow() {
        viewModelScope.launch {
            timerFlow.collect {
                calculateCurrentTimeLinePadding(it.get(Calendar.MINUTE))
            }
        }
    }

    private suspend fun tickerFlow() {
        while (true) {
            delay(MINUTE_LONG)
            timerFlow.emit(Calendar.getInstance())
        }
    }

    private fun calculateCurrentTimeLinePadding(currentMinutes: Int) {
        val currentMinutesInPercent = (currentMinutes / MINUTES_IN_HOUR_FLOAT)
        timelinePadding.value = CONTENT_BOX_HEIGHT * currentMinutesInPercent
    }
}