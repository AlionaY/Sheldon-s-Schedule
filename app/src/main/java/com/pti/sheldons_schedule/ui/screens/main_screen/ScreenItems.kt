package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
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
import com.pti.sheldons_schedule.data.EventsOfDay
import com.pti.sheldons_schedule.data.FullEvent
import com.pti.sheldons_schedule.ui.theme.Teal200
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.formatDate
import com.pti.sheldons_schedule.util.toCalendar
import kotlinx.coroutines.launch

@Composable
fun EventsColumn(
    eventsOfDay: EventsOfDay,
    currentHour: Int,
    onClick: (FullEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        eventsOfDay.events.filter {
            val eventStartHour = it.event.startDate.toCalendar()
                .formatDate(Constants.HOUR_FORMAT).toInt()

            eventStartHour == currentHour
        }.forEach { event ->
            Text(
                text = event.event.title,
                fontSize = 11.sp,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.clickable { onClick(event) }
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