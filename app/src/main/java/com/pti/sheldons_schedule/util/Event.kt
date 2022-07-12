package com.pti.sheldons_schedule.util

import androidx.navigation.NavController
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.ui.navigation.NavDestination

fun Event.navigateToEditScreen(navController: NavController) {
    val route = "${NavDestination.EditEventScreen.route}/${this.creationDate}"
    navController.navigate(route)
}