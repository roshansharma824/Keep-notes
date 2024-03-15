package com.example.keepnotes.presentation.common

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun ProgressIndicator(){
    Dialog(onDismissRequest = { }) {
        CircularProgressIndicator()
    }
}