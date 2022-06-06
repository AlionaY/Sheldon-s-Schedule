package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pti.sheldons_schedule.ui.navigation.NavDestination
import com.pti.sheldons_schedule.ui.navigation.navigate
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.ui.theme.LightSky
import com.pti.sheldons_schedule.ui.theme.Sky
import com.pti.sheldons_schedule.ui.theme.White
import com.pti.sheldons_schedule.util.horizontalPadding
import java.util.*


private const val HOURS_COUNT = 24
private const val CONTENT_BOX_HEIGHT = 60
private const val MINUTES_IN_HOUR_FLOAT = 60f

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()
    val weeks = viewModel.weeks.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Sky),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            count = weeks.itemCount,
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            var currentWeek = weeks.peek(page)

            LaunchedEffect(key1 = pagerState) {
                snapshotFlow { pagerState.currentPage }.collect {
                    currentWeek = weeks[page]
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                CalendarHeader(currentWeek)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 0.5.dp,
                            shape = RectangleShape,
                            color = LightSky
                        )
                ) {
                    items(HOURS_COUNT) { hourItem ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(CONTENT_BOX_HEIGHT.dp)
                        ) {
                            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                                val (horizontalLine, hour, content) = createRefs()

                                Divider(
                                    color = LightSky,
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
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .weight(1f)
                                                .drawBehind {
                                                    val strokeWidth = 2f
                                                    val y = size.height - strokeWidth

                                                    drawLine(
                                                        color = LightSky,
                                                        start = Offset(0f, 0f),
                                                        end = Offset(0f, y),
                                                        strokeWidth = strokeWidth
                                                    )
                                                }
                                        ) {
                                            val calendar = Calendar.getInstance()
                                            val isCurrentHour =
                                                calendar.get(Calendar.HOUR_OF_DAY) == hourItem

                                            if (dayOfWeek.isCurrent && isCurrentHour) {
                                                val currentMinutesInPercent = (calendar.get(Calendar.MINUTE)
                                                    .toFloat() / MINUTES_IN_HOUR_FLOAT)
                                                val padding = CONTENT_BOX_HEIGHT * currentMinutesInPercent

                                                Divider(
                                                    modifier = Modifier
                                                        .padding(top = padding.dp)
                                                        .fillMaxWidth()
                                                        .height(2.dp),
                                                    color = White
                                                )
                                            }
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
                                    color = Black
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