package com.example.keepnotes.navigation.screen

import com.example.keepnotes.utils.Constants.NOTE_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object OnBoarding: Screen("on_boarding_screen")
    object Search: Screen("search_screen")

    object EditNote:Screen("edit_note_screen/{${NOTE_ARGUMENT_KEY}}"){
        fun passNoteId(noteId: Int): String = "edit_note_screen/${noteId}"
    }
}