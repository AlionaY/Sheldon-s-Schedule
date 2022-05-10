package com.pti.sheldons_schedule.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.pti.sheldons_schedule.MainViewModel
import com.pti.sheldons_schedule.ui.screens.CreateEventScreen
import com.pti.sheldons_schedule.ui.screens.EntryScreen

@ExperimentalAnimationApi
@Composable
fun Navigation(navHostController: NavHostController) {

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    AnimatedNavHost(
        navController = navHostController,
        startDestination = NavDestination.EntryScreen.route
    ) {
        addScreen(route = NavDestination.EntryScreen) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val model = viewModel<MainViewModel>()
                EntryScreen(navHostController, model)
            }
        }

        addScreen(route = NavDestination.CreateEventScreen, withAnimation = true) {
            CreateEventScreen()
        }
    }
}
