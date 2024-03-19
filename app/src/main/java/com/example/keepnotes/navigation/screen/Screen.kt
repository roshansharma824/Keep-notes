package com.example.keepnotes.navigation.screen

import com.example.keepnotes.utils.Constants.NOTE_ARGUMENT_KEY

sealed class Screen(val route: String) {
    data object Splash : Screen("splash_screen")
    data object Login : Screen("login_screen")
    data object OnBoarding: Screen("on_boarding_screen")
    data object Search: Screen("search_screen")

    data object EditNote:Screen("edit_note_screen/{${NOTE_ARGUMENT_KEY}}"){
        fun passNoteId(noteId: String): String = "edit_note_screen/${noteId}"
    }
}