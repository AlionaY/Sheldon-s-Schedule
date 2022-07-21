package com.pti.sheldons_schedule.ui.navigation

sealed class NavDestination(val route: String) {
    object EntryScreen: NavDestination("entry_screen")
    object CreateEventScreen: NavDestination("create_event_screen")
    object EditEventScreen: NavDestination("edit_event_screen")
    object ToDoListScreen: NavDestination("to_do_list_screen")
}