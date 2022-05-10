package com.pti.sheldons_schedule.ui.navigation

sealed class NavDestination(val route: String) {
    object EntryScreen: NavDestination("entry_screen")
    object CreateEventScreen: NavDestination("create_event_screen")
}