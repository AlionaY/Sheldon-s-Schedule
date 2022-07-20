package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pti.sheldons_schedule.data.*
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.WEEK_LENGTH
import com.pti.sheldons_schedule.util.formatDate
import com.pti.sheldons_schedule.util.toCalendar
import java.util.*

class WeekdaysPagingSource(private val events: List<FullEvent>) : PagingSource<Int, Week>() {

    private val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    override fun getRefreshKey(state: PagingState<Int, Week>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Week> {
        val page = params.key ?: 0
        val currentCalendar = (calendar.clone() as Calendar).apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }
        val eventOfDays = mutableListOf<EventsOfDay>()
        val dayName = currentCalendar.formatDate(Constants.DAY_NAME_FORMAT)
            .substring(0, 3)

        currentCalendar.add(Calendar.WEEK_OF_YEAR, page)

        for (i in 0 until WEEK_LENGTH) {
            val isCurrentDay = currentCalendar.get(Calendar.DAY_OF_YEAR) ==
                    calendar.get(Calendar.DAY_OF_YEAR)

            eventOfDays += EventsOfDay(
                day = DayOfWeek(
                    dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH),
                    dayName = dayName,
                    isCurrent = isCurrentDay,
                    day = currentCalendar.formatDate(DATE_FORMAT_ISO_8601)
                ),
                events = events.filter {
                    it.event.startDate.toCalendar().formatDate(DATE_FORMAT) ==
                            currentCalendar.formatDate(DATE_FORMAT)
                }
            )
            currentCalendar.add(Calendar.DAY_OF_WEEK, 1)
        }

        return LoadResult.Page(
            data = listOf(Week(eventOfDays)),
            prevKey = if (page == 0) null else page.minus(1),
            nextKey = page.plus(1)
        )
    }
}