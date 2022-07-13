package com.pti.sheldons_schedule.ui.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable


fun NavController.navigate(destination: NavDestination, param: String? = null) {
    val route = "${destination.route}/$param"
    if (param == null) navigate(destination.route) else navigate(route)
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addScreen(
    route: NavDestination,
    withAnimation: Boolean = false,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {

    val enterTransition = if (withAnimation) scaleIn(
        animationSpec = tween(500),
        initialScale = 0f,
        transformOrigin = TransformOrigin(1f, 1f)
    ) else null

    composable(
        route = route.route,
        enterTransition = { enterTransition },
        exitTransition = { null },
        popEnterTransition = { null },
    ) {
        content(it)
    }
}