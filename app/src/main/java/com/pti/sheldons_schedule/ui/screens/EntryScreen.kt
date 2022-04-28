package com.pti.sheldons_schedule.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.R

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