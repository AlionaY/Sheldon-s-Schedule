package com.pti.sheldons_schedule.util

import androidx.navigation.NavController
import com.pti.sheldons_schedule.data.Event
import com.pti.sheldons_schedule.ui.navigation.NavDestination

fun Event.navigateToEditEventScreen(navController: NavController) {
    val eventId = this.creationDate.toString()
    val route = "${NavDestination.EditEventScreen.route}/${eventId}"
    navController.navigate(route)
}

