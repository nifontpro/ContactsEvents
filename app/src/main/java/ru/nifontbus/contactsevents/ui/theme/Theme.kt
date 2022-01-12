package ru.nifontbus.contactsevents.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryColor,
    secondary = SecondaryDarkColor,
    background = Dark2Gray,
    surface = HalfGrayDark,
    onBackground = TextWhite,
    onSecondary = Dark3Gray,
    error = LightRed
)

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryDarkColor,
    secondary = SecondaryColor,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onSecondary = TextWhite,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun ContactsEventsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        if (Build.VERSION.SDK_INT >= 29) {
            LightColorPalette
        } else {
            DarkColorPalette
        }

    }
    
    CompositionLocalProvider(LocalSpacing provides Spacing()) {

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}