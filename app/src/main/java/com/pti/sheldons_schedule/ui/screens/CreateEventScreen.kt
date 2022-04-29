package com.pti.sheldons_schedule.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.theme.Sky

@Composable
fun CreateEventScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(Sky)) {
        Text(
            text = stringResource(id = R.string.create_event),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}