package com.simplifymindfulness.selfbetter

sealed class Screen(val route: String) {
    object HabitTracker : Screen("habitTracker")
    object NewHabit : Screen("newHabit")
}
