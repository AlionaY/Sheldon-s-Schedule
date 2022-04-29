package com.pti.sheldons_schedule.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.Sky

@Composable
fun EntryScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = { onClick() }, modifier = modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 15.dp)
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(stringResource(id = R.string.create_event))
        }
    }
}


@Composable
fun CircularRevealLayout(modifier: Modifier = Modifier) {
    var radius by remember { mutableStateOf(0f) }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .drawBehind {
                    drawCircle(
                        color = Sky,
                        radius = radius,
                        center = Offset(size.width, 0f)
                    )
                },
            contentAlignment = Alignment.BottomEnd
        ) {
            val animatedRadius = remember { androidx.compose.animation.core.Animatable(0f) }
        }
    }
}