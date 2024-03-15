package com.example.keepnotes.navigation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Check
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItemScreen(val route: String, val icon: ImageVector, val title: String) {

    data object Home : BottomNavItemScreen("home_screen", Icons.Default.Home, "")
    data object EditNote : BottomNavItemScreen("edit_note_screen", Icons.Default.Home, "")

    data object CheckListNote : BottomNavItemScreen("check_list_note_screen", Icons.Outlined.Check, "")

    data object DrawNote : BottomNavItemScreen("draw_note_screen", Icons.Default.Edit, "")

    data object VoiceNote : BottomNavItemScreen("voice_note_screen", Icons.Default.Call, "")
    data object PictureNote : BottomNavItemScreen("picture_note_screen", Icons.Default.PlayArrow, "")

}