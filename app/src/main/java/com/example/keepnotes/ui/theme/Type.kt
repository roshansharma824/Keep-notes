package com.example.keepnotes.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.keepnotes.R

val NoteFontFamily = FontFamily(
    Font(R.font.notes_black, FontWeight.Black),
    Font(R.font.notes_extrabold, FontWeight.ExtraBold),
    Font(R.font.notes_bold, FontWeight.Bold),
    Font(R.font.notes_semibold, FontWeight.SemiBold),
    Font(R.font.notes_medium, FontWeight.Medium),
    Font(R.font.notes_regular, FontWeight.W400),
)

val TypographyStyle = Typography(
    h1 = TextStyle(
        fontFamily = NoteFontFamily,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 49.sp
    ),
    body1 = TextStyle(
        fontFamily = NoteFontFamily,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 24.sp
    ),
)