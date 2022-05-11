package com.pti.sheldons_schedule.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pti.sheldons_schedule.ui.navigation.NavDestination
import com.pti.sheldons_schedule.ui.navigation.navigate
import com.pti.sheldons_schedule.util.horizontalPadding

@Composable
fun EntryScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { navController.navigate(NavDestination.CreateEventScreen) },
            modifier = Modifier
                .horizontalPadding(horizontal = 10.dp, bottom = 15.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }
}