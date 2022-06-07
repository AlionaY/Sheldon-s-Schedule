package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
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
import com.pti.sheldons_schedule.ui.theme.Teal200
import com.pti.sheldons_schedule.util.horizontalPadding
import java.util.*


private const val HOURS_COUNT = 24
const val CONTENT_BOX_HEIGHT = 60

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val weeks = viewModel.weeks.collectAsLazyPagingItems()
    val timePadding by viewModel.timePadding.collectAsState()
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    val pagerState = rememberPagerState()
    val lazyListState = rememberLazyListState()

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
                        ),
                    state = lazyListState
                ) {
                    itemsIndexed(items = (0 until HOURS_COUNT).map { listOf(it) }) { hourItem, index ->
                        LaunchedEffect(key1 = Unit) {
                            lazyListState.scrollToItem(currentHour)
                        }

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
                                        BoxWithConstraints(
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
                                            val isCurrentHour = currentHour == hourItem

                                            if (dayOfWeek.isCurrent && isCurrentHour) {
                                                Divider(
                                                    modifier = Modifier
                                                        .padding(top = timePadding.dp)
                                                        .fillMaxWidth()
                                                        .height(2.dp),
                                                    color = Teal200
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