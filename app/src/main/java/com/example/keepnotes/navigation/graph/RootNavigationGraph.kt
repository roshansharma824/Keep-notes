package com.example.keepnotes.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.MainScreen
import com.example.keepnotes.presentation.screen.splash.SplashScreen

@Composable
fun RootNavigationGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route){
            SplashScreen(navController = navHostController)
        }

        composable(route = Graph.MAIN){
            MainScreen()
        }
    }
}