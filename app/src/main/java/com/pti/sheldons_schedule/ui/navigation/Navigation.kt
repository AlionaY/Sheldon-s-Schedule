package com.pti.sheldons_schedule.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.pti.sheldons_schedule.ui.screens.create_event_screen.CreateEventScreen
import com.pti.sheldons_schedule.ui.screens.EntryScreen

@ExperimentalAnimationApi
@Composable
fun Navigation(navHostController: NavHostController) {

    AnimatedNavHost(
        navController = navHostController,
        startDestination = NavDestination.EntryScreen.route
    ) {
        addScreen(route = NavDestination.EntryScreen) {
            EntryScreen(navHostController)
        }

        addScreen(route = NavDestination.CreateEventScreen, withAnimation = true) {
            CreateEventScreen()
        }
    }
}
