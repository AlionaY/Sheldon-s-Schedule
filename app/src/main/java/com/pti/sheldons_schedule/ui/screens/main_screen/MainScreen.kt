package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pti.sheldons_schedule.ui.navigation.NavDestination
import com.pti.sheldons_schedule.ui.navigation.navigate
import com.pti.sheldons_schedule.ui.theme.Graphite
import com.pti.sheldons_schedule.ui.theme.Steel
import com.pti.sheldons_schedule.util.horizontalPadding
import java.util.*


private const val HOURS_COUNT = 24
const val CONTENT_BOX_HEIGHT = 60
const val CALENDAR_HEADER_HEIGHT = 58

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val weeks = viewModel.weeks.collectAsLazyPagingItems()
    val ticker by viewModel.ticker.collectAsState(initial = 0f)

    val pagerState = rememberPagerState()

    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                viewModel.reload()
            }
            else -> {}
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val heightPx = with(LocalDensity.current) {
            maxHeight.value.dp.toPx()
        }
        val centerOfCalendar = heightPx / 2

        HorizontalPager(
            count = weeks.itemCount,
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            var currentWeek = weeks.peek(page)
            val scrollState = rememberScrollState()

            LaunchedEffect(key1 = pagerState) {
                snapshotFlow { pagerState.currentPage }.collect {
                    currentWeek = weeks[page]
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                CalendarHeader(
                    currentWeek = currentWeek?.week,
                    height = CALENDAR_HEADER_HEIGHT.dp
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    (0 until HOURS_COUNT).forEach { hourItem ->
                        val isCurrentHour = currentHour == hourItem

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(CONTENT_BOX_HEIGHT.dp)
                        ) {
                            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                                val (horizontalLine, hour, content) = createRefs()
                                val dividerColor = if (isSystemInDarkTheme()) Steel else Graphite

                                Divider(
                                    color = dividerColor,
                                    modifier = Modifier
                                        .height(0.7.dp)
                                        .constrainAs(horizontalLine) {
                                            top.linkTo(content.top)
                                            start.linkTo(hour.end)
                                            end.linkTo(parent.end)
                                            width = Dimension.fillToConstraints
                                        }
                                )

                                Row(
                                    modifier = Modifier
                                        .constrainAs(content) {
                                            width = Dimension.fillToConstraints
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        }
                                        .padding(start = 60.dp)
                                ) {
                                    currentWeek?.week?.forEach { dayOfWeek ->

                                        BoxWithConstraints(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .weight(1f)
                                                .drawBehind {
                                                    val strokeWidth = 2f
                                                    val y = size.height - strokeWidth

                                                    drawLine(
                                                        color = dividerColor,
                                                        start = Offset(0f, 0f),
                                                        end = Offset(0f, y),
                                                        strokeWidth = strokeWidth
                                                    )
                                                }
                                        ) {
                                            val padding = ticker * this.maxHeight.value

                                            EventsColumn(dayOfWeek, hourItem)

                                            HourDivider(
                                                isCurrentDay = dayOfWeek.day.isCurrent,
                                                isCurrentHour = isCurrentHour,
                                                padding = padding.dp,
                                                centerOfCalendar = centerOfCalendar,
                                                scrollState = scrollState,
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = if (hourItem == 0) "" else "$hourItem:00",
                                    modifier = Modifier.constrainAs(hour) {
                                        top.linkTo(horizontalLine.top)
                                        bottom.linkTo(horizontalLine.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(horizontalLine.start)
                                        width = Dimension.value(50.dp)
                                    },
                                    textAlign = TextAlign.Center,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(NavDestination.CreateEventScreen) },
            modifier = Modifier
                .horizontalPadding(horizontal = 10.dp, bottom = 15.dp)
                .align(Alignment.BottomEnd),
            elevation = FloatingActionButtonDefaults.elevation(5.dp)
        ) {
            Icon(Icons.Filled.Add, null)
        }
    }
}