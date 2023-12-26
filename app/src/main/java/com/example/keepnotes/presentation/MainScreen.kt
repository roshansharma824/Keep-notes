package com.example.keepnotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.navigation.graph.MainNavGraph
import com.example.keepnotes.presentation.component.BottomBar
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.BottomBarBackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_32dp
import com.example.keepnotes.ui.theme.DIMENS_40dp
import com.example.keepnotes.ui.theme.DIMENS_64dp
import com.example.keepnotes.ui.theme.DIMENS_8dp

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            Surface(
                elevation = DIMENS_32dp,
                shape = RectangleShape
            ) {
                BottomBar(navController = navController)
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.background(
                    color = BottomBarBackgroundColor, shape = RoundedCornerShape(
                        DIMENS_16dp
                    )
                ).size(DIMENS_64dp).border(shape = RoundedCornerShape(
                    DIMENS_16dp
                ), width = DIMENS_8dp, color = BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New Note", modifier = Modifier.size(
                    DIMENS_40dp))
            }
        },
        isFloatingActionButtonDocked = true
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            MainNavGraph(navHostController = navController)
        }
    }
}