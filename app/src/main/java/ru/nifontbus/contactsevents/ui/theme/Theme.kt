package ru.nifontbus.contactsevents.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryColor,
    secondary = SecondaryColor,
    background = Dark2Gray,
    surface = HalfGrayDark,
    onBackground = TextWhite,
    onSecondary = Dark3Gray
)

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryDarkColor,
    secondary = SecondaryLightColor,
    background = Color.White,
    surface = HalfGray,
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
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}