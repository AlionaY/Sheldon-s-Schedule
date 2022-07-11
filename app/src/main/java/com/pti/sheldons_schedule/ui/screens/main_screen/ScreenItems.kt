package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.data.DayOfWeek
import com.pti.sheldons_schedule.data.WeekEvents
import com.pti.sheldons_schedule.ui.theme.Teal200
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.convertToCalendar
import com.pti.sheldons_schedule.util.formatDate
import kotlinx.coroutines.launch

@Composable
fun EventsColumn(
    currentWeek: WeekEvents?,
    dayOfWeek: DayOfWeek,
    currentHour: Int
) {
    Column(modifier = Modifier.fillMaxSize()) {
        currentWeek?.events?.filter {
            val eventStartDay = it.startDate.convertToCalendar()
                .formatDate(Constants.DATE_FORMAT)
            val currentDay = dayOfWeek.day.convertToCalendar()
                .formatDate(Constants.DATE_FORMAT)
            val eventStartHour = it.startDate.convertToCalendar()
                .formatDate(Constants.HOUR_FORMAT).toInt()

             eventStartDay == currentDay && eventStartHour == currentHour
        }?.forEach { event ->
            Text(
                text = event.title,
                fontSize = 11.sp,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun HourDivider(
    isCurrentDay: Boolean,
    isCurrentHour: Boolean,
    padding: Dp,
    centerOfCalendar: Float,
    scrollState: ScrollState,
) {
    val scope = rememberCoroutineScope()

    if (isCurrentDay && isCurrentHour) {
        Divider(
            modifier = Modifier
                .padding(top = padding)
                .fillMaxWidth()
                .height(2.dp)
                .onGloballyPositioned { coordinates ->
                    if (
                        isCurrentHour &&
                        coordinates.positionInRoot().y != centerOfCalendar &&
                        !scrollState.isScrollInProgress
                    ) {
                        scope.launch {
                            scrollState.scrollTo(
                                centerOfCalendar.toInt()
                            )
                        }
                    }
                },
            color = Teal200
        )
    }
}