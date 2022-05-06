package com.pti.sheldons_schedule.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pti.sheldons_schedule.MainViewModel
import com.pti.sheldons_schedule.ui.navigation.NavDestination
import com.pti.sheldons_schedule.ui.theme.Sky
import com.pti.sheldons_schedule.util.horizontalPadding
import com.pti.sheldons_schedule.util.navigate

@Composable
fun EntryScreen(navController: NavController, viewModel: MainViewModel) {
    val animationState by viewModel.animationState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        val animateShape = remember { Animatable(animationState.animateShape) }
        viewModel.getMaxRadiusPx(LocalDensity.current, LocalConfiguration.current)

        LaunchedEffect(key1 = animationState.isClicked) {
            if (animationState.isClicked) {
                animateShape.animateTo(animationState.maxRadiusPx, animationSpec = tween()) {
                    viewModel.updateAnimationRadius(value)
                }
                animateShape.snapTo(0f)
                navController.navigate(NavDestination.CreateEventScreen)
            }
            viewModel.resetIsClicked()
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = Sky,
                    radius = animationState.radius,
                    center = Offset(size.width, size.height)
                )
            })

        FloatingActionButton(
            onClick = { viewModel.toggleClicked() },
            modifier = Modifier
                .horizontalPadding(horizontal = 10.dp, bottom = 15.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }
}