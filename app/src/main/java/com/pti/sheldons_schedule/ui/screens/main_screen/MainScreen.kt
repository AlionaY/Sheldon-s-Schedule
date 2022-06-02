package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pti.sheldons_schedule.data.Week
import com.pti.sheldons_schedule.ui.theme.LightSky

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val pagerState = rememberPagerState()
    val weeks = viewModel.weeks.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
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
                state = pagerState,
            ) { page ->

                var currentWeek : Week? = null
                if (currentWeek == null) currentWeek = weeks.peek(page)

                LaunchedEffect(key1 = pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect {
                        currentWeek = weeks[page]
                    }
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    currentWeek?.week?.forEach { day ->
                        Column(
                            modifier = Modifier
                                .weight(0.14f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = day.weekDayName.substring(0, 3),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .height(23.dp),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = day.dayOfMonth.toString(),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .height(35.dp),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}