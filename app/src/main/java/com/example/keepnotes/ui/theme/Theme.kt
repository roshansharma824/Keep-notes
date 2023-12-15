package com.example.keepnotes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = TopBarBackgroundColor,
    primaryVariant = TopBarBackgroundColor,
    secondary = TopBarBackgroundColor,
    background = BackgroundColor
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun KeepNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        systemUiController.setNavigationBarColor(
            color = BackgroundColor
        )
        systemUiController.setSystemBarsColor(
            color = BackgroundColor
        )
        DarkColorPalette
    } else {
        systemUiController.setNavigationBarColor(
            color = BackgroundColor
        )
        systemUiController.setSystemBarsColor(
            color = BackgroundColor
        )
        DarkColorPalette
    }


    MaterialTheme(
        colors = colors,
        typography = TypographyStyle,
        shapes = Shapes,
        content = content,
    )
}