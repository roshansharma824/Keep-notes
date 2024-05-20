package com.example.keepnotes.navigation.graph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.keepnotes.navigation.screen.BottomNavItemScreen
import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.screen.checklistnote.CheckListNote
import com.example.keepnotes.presentation.screen.drawnote.DrawNote
import com.example.keepnotes.presentation.screen.editnote.EditNoteScreen
import com.example.keepnotes.presentation.screen.home.RootScreen
import com.example.keepnotes.presentation.screen.picturenote.PictureNote
import com.example.keepnotes.presentation.screen.search.SearchNotesScreen
import com.example.keepnotes.presentation.screen.voicenote.VoiceNote
import com.example.keepnotes.utils.Constants.NOTE_ARGUMENT_KEY

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainNavGraph(navHostController: NavHostController = rememberNavController()) {
    SharedTransitionLayout {

        NavHost(
            navController = navHostController,
            route = Graph.MAIN,
            startDestination = BottomNavItemScreen.Home.route
        ) {
            composable(route = BottomNavItemScreen.Home.route) {
                RootScreen(
                    navController = navHostController,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                )
            }
            composable(route = BottomNavItemScreen.Search.route) {
                SearchNotesScreen(
                    navController = navHostController,
                    animatedContentScope = this@composable
                )
            }
            composable(route = BottomNavItemScreen.CheckListNote.route) {
                CheckListNote(
                    navController = navHostController,
                    noteId = "-1",
                    animatedContentScope = this@composable,
                )
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

            detailsNavGraph(navHostController = navHostController, this@SharedTransitionLayout)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.detailsNavGraph(
    navHostController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
) {
    navigation(
        route = Graph.EDITNOTE,
        startDestination = Screen.EditNote.route
    ) {
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(navArgument(NOTE_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(NOTE_ARGUMENT_KEY, "-1")
            if (noteId != null) {
                sharedTransitionScope.EditNoteScreen(
                    navController = navHostController,
                    noteId = noteId,
                    animatedContentScope = this@composable,
                )
            }
        }
    }
}