package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pti.sheldons_schedule.data.DayOfWeek
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.data.Week
import com.pti.sheldons_schedule.data.WeekEvents
import com.pti.sheldons_schedule.db.EventRepository
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT
import com.pti.sheldons_schedule.util.Constants.DATE_FORMAT_ISO_8601
import com.pti.sheldons_schedule.util.Constants.WEEK_LENGTH
import com.pti.sheldons_schedule.util.convertToCalendar
import com.pti.sheldons_schedule.util.formatDate
import java.util.*
import javax.inject.Inject

class WeekdaysPagingSource(
    val events : List<Event>
) : PagingSource<Int, WeekEvents>() {


    private val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    override fun getRefreshKey(state: PagingState<Int, WeekEvents>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WeekEvents> {
        val page = params.key ?: 0
        val currentCalendar = (calendar.clone() as Calendar).apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }
        val weekList = mutableListOf<DayOfWeek>()
        val weekEvents = mutableListOf<Event>()

        currentCalendar.add(Calendar.WEEK_OF_YEAR, page)

        for (i in 0 until WEEK_LENGTH) {
            weekList += DayOfWeek(
                dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH),
                weekDayName = currentCalendar.formatDate(Constants.DAY_NAME_FORMAT).substring(0, 3),
                isCurrent = currentCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR),
                day = currentCalendar.formatDate(DATE_FORMAT_ISO_8601)
            )
            weekEvents += events.filter {
                it.startDate.convertToCalendar().formatDate(DATE_FORMAT) ==
                        currentCalendar.formatDate(DATE_FORMAT)
            }
            currentCalendar.add(Calendar.DAY_OF_WEEK, 1)
        }

        return LoadResult.Page(
            data = listOf(WeekEvents(week = weekList, events = weekEvents)),
            prevKey = if (page == 0) null else page.minus(1),
            nextKey = page.plus(1)
        )
    }
}