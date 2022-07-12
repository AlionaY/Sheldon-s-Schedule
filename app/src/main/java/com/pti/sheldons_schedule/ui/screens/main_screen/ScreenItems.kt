package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.pti.sheldons_schedule.data.EventsOfDay
import com.pti.sheldons_schedule.ui.theme.Teal200
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.convertToCalendar
import com.pti.sheldons_schedule.util.formatDate
import kotlinx.coroutines.launch

@Composable
fun EventsColumn(eventsOfDay: EventsOfDay, currentHour: Int) {
    Column(modifier = Modifier.fillMaxSize()) {
        eventsOfDay.events.filter {
            val eventStartHour = it.startDate.convertToCalendar()
                .formatDate(Constants.HOUR_FORMAT).toInt()

            eventStartHour == currentHour
        }.forEach { event ->
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

@Composable
fun OnLifecycleEvent(
    onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit
) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(key1 = lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}