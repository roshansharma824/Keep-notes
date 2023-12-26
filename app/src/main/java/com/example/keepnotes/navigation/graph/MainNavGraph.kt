package com.example.keepnotes.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.keepnotes.navigation.screen.BottomNavItemScreen
import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.screen.checklistnote.CheckListNote
import com.example.keepnotes.presentation.screen.drawnote.DrawNote
import com.example.keepnotes.presentation.screen.home.HomeScreen
import com.example.keepnotes.presentation.screen.picturenote.PictureNote
import com.example.keepnotes.presentation.screen.voicenote.VoiceNote

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
    }
}

fun NavGraphBuilder.detailsNavGraph() {
    navigation(
        route = Graph.DETAILS,
        startDestination = Screen.Details.route
    ) {

    }
}