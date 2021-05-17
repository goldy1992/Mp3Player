package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


val blueAppTheme = lightColors(
    primary = primaryBlue,
    primaryVariant = darkBlue,
    secondary = accentPink,
    background = white,
    error = red,
    surface = white,
    onPrimary = white,
    onSecondary = black,
    onBackground = black,
    onSurface = black,
    onError = white
)

val blueAppDarkTheme = darkColors(
    primary = grey,
    primaryVariant = darkBlue,
    secondary = grey,
    background = black,
    error = red,
    surface = black,
    onPrimary = white,
    onSecondary = white,
    onBackground = white,
    onSurface = white,
    onError = white
)

val orangeAppTheme = lightColors(
    primary = darkOrange,
    primaryVariant = darkBlue,
    secondary = accentOrane,
    background = white,
    error = red,
    surface = white,
    onPrimary = white,
//    onSecondary = black,
    onBackground = black,
    onSurface = black,
//    onError = white
)

val orangeAppDarkTheme = darkColors(
    primary = grey,
    primaryVariant = darkBlue,
    secondary = grey,
    background = black,
    error = red,
    surface = black,
    onPrimary = white,
    onSecondary = white,
    onBackground = white,
    onSurface = white,
    onError = white
)




@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

    val colors = if (darkTheme) {
        blueAppDarkTheme
    } else {
        blueAppTheme
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}

