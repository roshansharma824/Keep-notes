package com.example.keepnotes.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.navigation.screen.BottomNavItemScreen
import com.example.keepnotes.ui.theme.BottomBarBackgroundColor
import com.example.keepnotes.ui.theme.BottomNavigationHeight
import com.example.keepnotes.ui.theme.DIMENS_24dp

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val navigationItems = listOf(
        BottomNavItemScreen.Home,
        BottomNavItemScreen.CheckListNote,
        BottomNavItemScreen.DrawNote,
        BottomNavItemScreen.VoiceNote,
        BottomNavItemScreen.PictureNote
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarDestination = navigationItems.any { it.route == currentRoute }


    if (bottomBarDestination) {

        Row(
            modifier = Modifier
                .background(color = BottomBarBackgroundColor, shape = RectangleShape)
                .height(
                    BottomNavigationHeight
                )
                .fillMaxWidth()
        ) {
            navigationItems.forEach { item ->
                if (item.route != "home_screen") {
                    IconButton(onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(
                                DIMENS_24dp
                            )
                        )
                    }
                }


            }
        }

    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navController = rememberNavController())
}