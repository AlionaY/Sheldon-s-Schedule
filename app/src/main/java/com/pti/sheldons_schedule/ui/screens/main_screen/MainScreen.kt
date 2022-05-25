package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pti.sheldons_schedule.ui.theme.LightSky

private const val SPACER_WIDTH = 60
private const val WEEK_DAYS_COUNT = 7

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState()
    val config = LocalConfiguration.current
    val width = (config.screenWidthDp - SPACER_WIDTH) / WEEK_DAYS_COUNT

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(SPACER_WIDTH.dp)
            )

//            todo: set endless pager
            HorizontalPager(
                count = state?.week?.size ?: 1,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightSky)
            ) { page ->
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(23.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state?.week?.forEach { dayOfWeek ->
                            Text(
                                text = dayOfWeek.weekDayName.substring(0, 3),
                                modifier = Modifier
                                    .width(width.dp)
                                    .fillMaxHeight(),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state?.week?.forEach { dayOfWeek ->
                            Text(
                                text = dayOfWeek.dayOfMonth.toString(),
                                modifier = Modifier
                                    .width(width.dp)
                                    .fillMaxHeight(),
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