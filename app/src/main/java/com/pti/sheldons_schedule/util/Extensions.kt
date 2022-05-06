package com.pti.sheldons_schedule.util

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.ComposeNavigator
import com.pti.sheldons_schedule.ui.navigation.NavDestination

fun Modifier.horizontalPadding(
    horizontal: Dp = 0.dp,
    top: Dp = 0.dp,
    bottom: Dp = 0.dp
) = this
    .padding(horizontal = horizontal)
    .padding(top = top, bottom = bottom)

fun Modifier.verticalPadding(
    vertical: Dp = 0.dp,
    start: Dp = 0.dp,
    end: Dp = 0.dp
) = this
    .padding(vertical = vertical)
    .padding(start = start, end = end)

fun NavController.navigate(destination: NavDestination) {
    navigate(destination.route)
}