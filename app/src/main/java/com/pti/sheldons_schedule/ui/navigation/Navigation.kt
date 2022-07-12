package com.pti.sheldons_schedule.ui.navigation

import android.provider.CalendarContract.Instances.EVENT_ID
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.pti.sheldons_schedule.ui.screens.create_event_screen.CreateEventScreen
import com.pti.sheldons_schedule.ui.screens.edit_event_screen.EditEventScreen
import com.pti.sheldons_schedule.ui.screens.main_screen.MainScreen

@ExperimentalAnimationApi
@Composable
fun Navigation(navHostController: NavHostController) {

    AnimatedNavHost(
        navController = navHostController,
        startDestination = NavDestination.EntryScreen.route
    ) {
        addScreen(route = NavDestination.EntryScreen) {
            MainScreen(navHostController)
        }

        addScreen(route = NavDestination.CreateEventScreen, withAnimation = true) {
            CreateEventScreen(navHostController)
        }

        composable("${NavDestination.EditEventScreen.route}/{$EVENT_ID}") {
            val eventId = it.arguments?.getString(EVENT_ID)?.toLong() ?: 0
            EditEventScreen(eventId = eventId, navController = navHostController)
        }
    }
}
