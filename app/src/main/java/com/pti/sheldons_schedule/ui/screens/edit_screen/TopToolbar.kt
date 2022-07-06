package com.pti.sheldons_schedule.ui.screens.edit_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pti.sheldons_schedule.ui.theme.Black

@Composable
fun TopToolbar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
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