package com.example.keepnotes.navigation.screen

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.keepnotes.R

sealed class BottomNavItemScreen(val route: String, val icon: Int, val title: String) {

    data object Home : BottomNavItemScreen("home_screen", R.drawable.icon_checkbox, "")
    data object EditNote : BottomNavItemScreen("edit_note_screen", R.drawable.icon_checkbox, "")

    data object CheckListNote : BottomNavItemScreen("check_list_note_screen", R.drawable.icon_checkbox, "")

    data object DrawNote : BottomNavItemScreen("draw_note_screen", R.drawable.icon_brush, "")

    data object VoiceNote : BottomNavItemScreen("voice_note_screen", R.drawable.icon_mike, "")
    data object PictureNote : BottomNavItemScreen("picture_note_screen", R.drawable.icon_gallery, "")

}