package com.pti.sheldons_schedule.ui.screens.main_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import com.pti.sheldons_schedule.data.Week
import com.pti.sheldons_schedule.ui.navigation.NavDestination
import com.pti.sheldons_schedule.ui.navigation.navigate
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.ui.theme.LightSky
import com.pti.sheldons_schedule.ui.theme.Teal200
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.horizontalPadding
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()
    val weeks = viewModel.weeks.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(60.dp)
            )

            HorizontalPager(
                count = weeks.itemCount,
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightSky),
                state = pagerState
            ) { page ->
                var currentWeek = weeks.peek(page)

                LaunchedEffect(key1 = pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect {
                        currentWeek = weeks[page]
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    currentWeek?.week?.forEach { day ->
                        val textColor = if (day.isCurrent) Teal200 else Black
                        val backgroundColor = if (day.isCurrent) Teal200 else Color.Transparent

                        Column(
                            modifier = Modifier
                                .weight(0.14f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = day.weekDayName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.4f),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = textColor
                            )

                            Box(
                                modifier = Modifier
                                    .size(35.dp)
                                    .weight(0.6f)
                                    .background(color = backgroundColor, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.dayOfMonth.toString(),
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.Center),
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(24) { item ->
                val calendar = Calendar.getInstance()
                val config = LocalConfiguration.current
                val height = (config.screenHeightDp - 58).toFloat() / 11

                Log.d("###", "curr hours ${calendar.get(Calendar.HOUR_OF_DAY)}, min ${calendar.get(Calendar.MINUTE)}")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height.dp)
                ) {

                    Text(
                        text = "$item:00",
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(60.dp)
                            .align(Alignment.Bottom),
                        textAlign = TextAlign.End,
                        fontSize = 12.sp,
                        color = Black
                    )
                    for (day in 0 until Constants.WEEK_LENGTH) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.14f)
                                .border(
                                    width = 1.dp,
                                    shape = RectangleShape,
                                    color = Color.Gray
                                )
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(NavDestination.CreateEventScreen) },
            modifier = Modifier
                .horizontalPadding(horizontal = 10.dp, bottom = 15.dp)
//                    .align(Alignment.BottomEnd)
                .alpha(1f)
        ) {
            Icon(Icons.Filled.Add, null)
        }
    }
}