package com.example.keepnotes.presentation.screen.drawnote

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawNote(){
    Box(modifier = Modifier) {
        Text(text = "DrawNote", color = Color.Green)
    }
}