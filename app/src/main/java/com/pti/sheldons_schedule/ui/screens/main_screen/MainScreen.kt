package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .background(Sky)
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 0.5.dp,
                        shape = RectangleShape,
                        color = LightSky
                    )
            ) {
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
                    items(HOURS_COUNT) { item ->
                        val hoursText = if (item == 0) "" else "$item:00"
                        val config = LocalConfiguration.current
                        val height = (config.screenHeightDp - 58).toFloat() / 11

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height.dp)
                        ) {
                            Text(
                                text = hoursText,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(60.dp)
                                    .align(Alignment.Bottom),
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp,
                                color = Black
                            )

                            for (day in 0 until Constants.WEEK_LENGTH) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(0.14f)
                                        .border(
                                            width = 0.5.dp,
                                            shape = RectangleShape,
                                            color = LightSky
                                        )
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