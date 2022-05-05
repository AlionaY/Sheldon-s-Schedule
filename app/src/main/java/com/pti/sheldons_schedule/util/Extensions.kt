package com.pti.sheldons_schedule.util

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.padding(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
    start: Dp = 0.dp,
    end: Dp = 0.dp,
    top: Dp = 0.dp,
    bottom: Dp = 0.dp
) = this
    .padding(horizontal = horizontal, vertical = vertical)
    .padding(top = top, bottom = bottom, start = start, end = end)