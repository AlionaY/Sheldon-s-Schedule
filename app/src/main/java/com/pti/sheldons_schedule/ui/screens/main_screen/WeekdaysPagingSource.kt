package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pti.sheldons_schedule.data.DayOfWeek
import com.pti.sheldons_schedule.data.Week
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.WEEK_LENGTH
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

class WeekdaysPagingSource : PagingSource<Int, Week>() {
    private val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    override fun getRefreshKey(state: PagingState<Int, Week>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Week> {
        val page = params.key ?: 0
        val currentCalendar = calendar.clone() as Calendar
        val weekList = mutableListOf<DayOfWeek>()

        currentCalendar.add(Calendar.WEEK_OF_YEAR, page)

        for (i in 0 until WEEK_LENGTH) {
             weekList += DayOfWeek(
                dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH),
                weekDayName = currentCalendar.formatDate(Constants.DAY_NAME_FORMAT)
            )
            currentCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return LoadResult.Page(
            data = listOf(Week(weekList)),
            prevKey = if (page == 0) null else page.minus(1),
            nextKey = page.plus(1)
        )
    }
}