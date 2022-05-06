package com.pti.sheldons_schedule.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pti.sheldons_schedule.MainViewModel
import com.pti.sheldons_schedule.ui.screens.CreateEventScreen
import com.pti.sheldons_schedule.ui.screens.EntryScreen

@Composable
fun Navigation(navHostController: NavHostController) {

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(navController = navHostController, startDestination = NavDestination.EntryScreen.route) {
        composable(NavDestination.EntryScreen.route) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val model = viewModel<MainViewModel>()
                EntryScreen(navHostController, model)
            }
        }

        composable(NavDestination.CreateEventScreen.route) {
            CreateEventScreen()
        }
    }
}