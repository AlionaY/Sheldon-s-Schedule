package com.pti.sheldons_schedule.ui.screens.main_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.pti.sheldons_schedule.data.WeekState
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.ui.theme.LightSky
import com.pti.sheldons_schedule.ui.theme.Teal200
import com.pti.sheldons_schedule.ui.theme.White
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.formatDate
import java.util.*

private const val SPACER_WIDTH = 60
private const val WEEK_DAYS_COUNT = 7

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.weekState.collectAsState()
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
                count = 10,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(LightSky)
                ) {
                    // todo: fix text color and circle color conditions
                    val textColor = if (state.currentDay.formatDate(Constants.DAY_NUMBER_FORMAT) ==
                        state.calendar.formatDate(Constants.DAY_NAME_FORMAT)
                    ) Teal200 else Black

                    val circleColor = Teal200

                    DayNumberAndNameItem(
                        width = width,
                        state = state,
                        dayNumber = getDayNumber(state.monday),
                        dayName = getDayName(state.monday),
                        textColor = textColor,
                        circleColor = circleColor
                    )

                    DayNumberAndNameItem(
                        width = width,
                        state = state,
                        dayNumber = getDayNumber(state.tuesday),
                        dayName = getDayName(state.tuesday),
                        textColor = textColor,
                        circleColor = circleColor
                    )

                    DayNumberAndNameItem(
                        width = width,
                        state = state,
                        dayNumber = getDayNumber(state.wednesday),
                        dayName = getDayName(state.wednesday),
                        textColor = textColor,
                        circleColor = circleColor
                    )

                    DayNumberAndNameItem(
                        width = width,
                        state = state,
                        dayNumber = getDayNumber(state.thursday),
                        dayName = getDayName(state.thursday),
                        textColor = textColor,
                        circleColor = circleColor
                    )

                    DayNumberAndNameItem(
                        width = width,
                        state = state,
                        dayNumber = getDayNumber(state.friday),
                        dayName = getDayName(state.friday),
                        textColor = textColor,
                        circleColor = circleColor
                    )

                    DayNumberAndNameItem(
                        width = width,
                        state = state,
                        dayNumber = getDayNumber(state.saturday),
                        dayName = getDayName(state.saturday),
                        textColor = textColor,
                        circleColor = circleColor
                    )

                    DayNumberAndNameItem(
                        width = width,
                        state = state,
                        dayNumber = getDayNumber(state.sunday),
                        dayName = getDayName(state.sunday),
                        textColor = textColor,
                        circleColor = circleColor
                    )
                }
            }
        }
    }
}

@Composable
private fun DayNumberAndNameItem(
    width: Int,
    state: WeekState,
    dayName: String,
    dayNumber: String,
    textColor: Color,
    circleColor: Color,
    modifier: Modifier = Modifier
) {
    Log.d("$$$", "day name $dayName")
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(width.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayName,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = textColor,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = dayNumber,
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = circleColor,
                    shape = CircleShape
                )
                .background(circleColor),
            color = Black,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

fun getDayName(calendar: Calendar) = calendar.formatDate(Constants.DAY_NAME_FORMAT)[0].toString()

fun getDayNumber(calendar: Calendar) = calendar.formatDate(Constants.DAY_NUMBER_FORMAT)