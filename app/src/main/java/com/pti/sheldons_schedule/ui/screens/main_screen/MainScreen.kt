package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.horizontalPadding


private const val HOURS_COUNT = 24

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
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

            CalendarHeader(currentWeek)

            ConstraintLayout(
                modifier = Modifier
                    .padding(top = 58.dp)
                    .fillMaxSize()
            ) {
                val box = createRef()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 0.5.dp,
                            shape = RectangleShape,
                            color = LightSky
                        )
                ) {
                    items(HOURS_COUNT) { item ->
                        Box(
                            modifier = Modifier
                                .constrainAs(box) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                                val (horizontalLine, hour, content) = createRefs()

                                Row(
                                    modifier = Modifier
                                        .constrainAs(content) {
                                            width = Dimension.fillToConstraints
                                            start.linkTo(parent.start, 60.dp)
                                            end.linkTo(parent.end)
                                        }
                                        .padding(start = 60.dp)
                                ) {
                                    for (day in 0 until Constants.WEEK_LENGTH) {
                                        Box(
                                            modifier = Modifier
                                                .height(60.dp)
                                                .weight(0.14f)
                                                .drawBehind {
                                                    drawLine(
                                                        LightSky,
                                                        Offset(0f, size.height),
                                                        Offset(size.height, size.width),
                                                        Stroke.DefaultMiter
                                                    )
                                                }
                                        )
                                    }
                                }

                                Divider(
                                    color = LightSky,
                                    modifier = Modifier
                                        .height(0.5.dp)
                                        .padding(start = 50.dp)
                                        .constrainAs(horizontalLine) {
                                            end.linkTo(parent.end)
                                            top.linkTo(box.top)
                                            start.linkTo(hour.end)
                                            end.linkTo(parent.end)
                                            width = Dimension.fillToConstraints
                                        }
                                )

                                Text(
                                    text = "$item:00",
                                    modifier = Modifier.constrainAs(hour) {
                                        width = Dimension.fillToConstraints
                                        top.linkTo(horizontalLine.top)
                                        bottom.linkTo(horizontalLine.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(horizontalLine.start)
                                        width = Dimension.wrapContent
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