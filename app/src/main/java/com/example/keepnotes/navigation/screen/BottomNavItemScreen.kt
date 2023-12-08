package com.example.keepnotes.navigation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItemScreen(val route: String, val icon: ImageVector, val title: String) {

    object Home : BottomNavItemScreen("home_screen", Icons.Default.Home, "Shop")

    object Favorite : BottomNavItemScreen("favorite_screen", Icons.Default.Favorite, "Favorite")

    object Cart : BottomNavItemScreen("cart_screen", Icons.Default.ShoppingCart, "Cart")

    object About : BottomNavItemScreen("about_screen", Icons.Default.Person, "About")

}