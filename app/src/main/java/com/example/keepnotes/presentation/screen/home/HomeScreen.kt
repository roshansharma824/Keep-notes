package com.example.keepnotes.presentation.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.presentation.component.DrawerAppComponent


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {


    DrawerAppComponent(navController = navController)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}


