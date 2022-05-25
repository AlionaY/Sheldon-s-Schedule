package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pti.sheldons_schedule.data.DayOfWeekUI
import com.pti.sheldons_schedule.data.WeekState
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.WEEK_LENGTH
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

class WeekdaysPagingSource(
    private val calendar : Calendar,
) : PagingSource<Int, WeekState>() {
    private val weekOfDaysList = mutableListOf<DayOfWeekUI>()
    private val weeksList = mutableListOf<WeekState>()

    override fun getRefreshKey(state: PagingState<Int, WeekState>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WeekState> {
        val page = params.key ?: 1
        val nextPage = page + 1
        val prevPage = page - 1
        val currentCalendar = calendar.clone() as Calendar

        for (i in 0 until WEEK_LENGTH) {
            weekOfDaysList += DayOfWeekUI(
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                weekDayName = calendar.formatDate(Constants.DAY_NAME_FORMAT)
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        weeksList += WeekState(weekOfDaysList)
        calendar.add(Calendar.WEEK_OF_YEAR, 1)

        return LoadResult.Page(
            data = weeksList,
            prevKey = prevPage,
            nextKey = nextPage
        )
    }
}