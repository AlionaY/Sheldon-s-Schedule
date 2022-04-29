package com.pti.sheldons_schedule.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.navigation.Screen
import com.pti.sheldons_schedule.ui.theme.Black
import com.pti.sheldons_schedule.ui.theme.Sky
import kotlin.math.hypot

@Composable
fun EntryScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                animateShape.animateTo(maxRadiusPx, animationSpec = tween()) {
                    radius = value
                }
                navController.navigate(Screen.CreateEventScreen.route)
            }
            isClicked = false
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = Sky,
                    radius = radius,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            })

        Button(
            onClick = { isClicked = !isClicked },
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = stringResource(id = R.string.create_event),
                fontSize = 15.sp,
                color = Black
            )
        }
    }
}