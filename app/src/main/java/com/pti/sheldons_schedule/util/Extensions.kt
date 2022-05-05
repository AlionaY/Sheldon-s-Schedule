package com.pti.sheldons_schedule.util

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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