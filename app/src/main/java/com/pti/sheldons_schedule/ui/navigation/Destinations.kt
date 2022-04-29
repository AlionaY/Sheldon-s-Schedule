package com.pti.sheldons_schedule.ui.navigation

sealed class Screen(val route: String) {
    object EntryScreen: Screen("entry_screen")
    object CreateEventScreen: Screen("create_event_screen")
}