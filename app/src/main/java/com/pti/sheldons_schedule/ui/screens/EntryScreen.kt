package com.pti.sheldons_schedule.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.ui.theme.Sky
import kotlin.math.hypot

@Composable
fun EntryScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        var radius by remember { mutableStateOf(0f) }
        var isClicked by remember { mutableStateOf(false) }
        val animateShape = remember { Animatable(0f) }
        val (width, height) = with(LocalConfiguration.current) {
            with(LocalDensity.current) {
                screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx()
            }
        }
        val maxRadiusPx = hypot(width, height)
        
        LaunchedEffect(key1 = isClicked) {
            if (isClicked) {
                animateShape.animateTo(
                    maxRadiusPx, animationSpec = tween()
                ) {
                    radius = value
                }
            }
            isClicked = false
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = Sky,
                    radius = radius
                )
            })

        FloatingActionButton(
            onClick = { isClicked = !isClicked },
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }
}