package com.pti.sheldons_schedule.ui.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
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
        addScreen(NavDestination.EntryScreen.route) {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides viewModelStoreOwner
            ) {
                val model = viewModel<MainViewModel>()
                EntryScreen(navHostController, model)
            }
        }

        addScreen(NavDestination.CreateEventScreen.route) {
            CreateEventScreen()
        }
    }
}


@ExperimentalAnimationApi
fun NavGraphBuilder.addScreen(
    route: String,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {

    val enterTransition = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(500)
    )

    val popExitTransition = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(500)
    )

    composable(
        route = route,
        enterTransition = { enterTransition },
        exitTransition = { null },
        popEnterTransition = { null },
        popExitTransition = { popExitTransition }
    ) {
        content(it)
    }
}