package com.example.keepnotes.presentation.screen.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.keepnotes.presentation.component.DrawerAppComponent


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RootScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {


    DrawerAppComponent(navController = navController, sharedTransitionScope, animatedContentScope)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RootScreenPreview() {
//    RootScreen(navController = rememberNavController())
}


