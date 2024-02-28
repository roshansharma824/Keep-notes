package com.example.keepnotes.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keepnotes.navigation.screen.BottomNavItemScreen
import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.screen.editnote.EditNoteScreen
import com.example.keepnotes.presentation.screen.checklistnote.CheckListNote
import com.example.keepnotes.presentation.screen.drawnote.DrawNote
import com.example.keepnotes.presentation.screen.home.HomeScreen
import com.example.keepnotes.presentation.screen.picturenote.PictureNote
import com.example.keepnotes.presentation.screen.voicenote.VoiceNote
import com.example.keepnotes.utils.Constants.NOTE_ARGUMENT_KEY

@Composable
fun MainNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.MAIN,
        startDestination = BottomNavItemScreen.Home.route
    ) {
        composable(route = BottomNavItemScreen.Home.route) {
            HomeScreen(navController = navHostController)
        }
//        composable(route = BottomNavItemScreen.EditNote.route) {
//            EditNoteScreen(navController = navHostController)
//        }
        composable(route = BottomNavItemScreen.CheckListNote.route) {
            CheckListNote()
        }
        composable(route = BottomNavItemScreen.DrawNote.route) {
            DrawNote()
        }
        composable(route = BottomNavItemScreen.VoiceNote.route) {
            VoiceNote()
        }
        composable(route = BottomNavItemScreen.PictureNote.route) {
            PictureNote()
        }

        detailsNavGraph(navHostController = navHostController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navHostController: NavHostController) {
    navigation(
        route = Graph.EDITNOTE,
        startDestination = Screen.EditNote.route
    ) {
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(navArgument(NOTE_ARGUMENT_KEY) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt(NOTE_ARGUMENT_KEY)
            if (noteId != null) {
                EditNoteScreen(navController = navHostController,noteId = noteId)
            }
        }
    }
}