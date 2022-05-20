package com.pti.sheldons_schedule.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.ui.theme.LightSky
import com.pti.sheldons_schedule.ui.theme.Teal200
import com.pti.sheldons_schedule.ui.theme.White
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.formatDate
import java.time.DayOfWeek
import java.util.*

private const val SPACER_WIDTH = 60
private const val WEEK_DAYS_COUNT = 7

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val calendar = Calendar.getInstance(Locale.UK)
    val currentDayName = calendar.formatDate(Constants.WEEK_NAME_FORMAT).uppercase()

    val dayOfWeek = DayOfWeek.values()
    val pagerState = rememberPagerState()

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

            HorizontalPager(
                count = dayOfWeek.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(LightSky)
                ) {
                    val config = LocalConfiguration.current
                    val width = (config.screenWidthDp - SPACER_WIDTH) / WEEK_DAYS_COUNT

                    dayOfWeek.forEach {

                        val circleColor = if (it.name ==  currentDayName && page == 0) {
                            Teal200
                        } else {
                            White
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = it.name[0].toString(),
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .border(width = 1.dp, color = circleColor, shape = CircleShape)
                                    .background(circleColor),
                                color = Black,
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