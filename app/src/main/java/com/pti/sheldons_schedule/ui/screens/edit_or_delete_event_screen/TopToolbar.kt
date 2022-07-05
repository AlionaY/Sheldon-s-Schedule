package com.pti.sheldons_schedule.ui.screens.edit_or_delete_event_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pti.sheldons_schedule.ui.theme.Black

@Composable
fun TopToolbar(onClick: () -> Unit, modifier : Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(58.dp)
            .fillMaxWidth()
            .padding(start = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIos,
            contentDescription = "back",
            modifier = Modifier.clickable { onClick() },
            tint = Black
        )
    }
}