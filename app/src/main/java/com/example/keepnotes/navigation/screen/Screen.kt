package com.example.keepnotes.navigation.screen

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object OnBoarding: Screen("on_boarding_screen")
    object Search: Screen("search_screen")

    object Details:Screen("details_screen/{noteId}"){
        fun passNoteId(noteId: Int): String = "details_screen/${noteId}"
    }
}