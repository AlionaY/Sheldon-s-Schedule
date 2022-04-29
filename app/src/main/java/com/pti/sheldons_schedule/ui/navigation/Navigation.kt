package com.pti.sheldons_schedule.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pti.sheldons_schedule.ui.screens.CreateEventScreen
import com.pti.sheldons_schedule.ui.screens.EntryScreen
import com.pti.sheldons_schedule.util.addScreen

@Composable
fun Navigation(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screen.EntryScreen.route) {
        addScreen(Screen.EntryScreen) {
            EntryScreen(navHostController)
        }

        addScreen(Screen.CreateEventScreen) {
            CreateEventScreen()
        }
    }
}