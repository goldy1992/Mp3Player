package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
//import androidx.compose.material.MaterialTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.github.goldy1992.mp3player.client.UserPreferencesRepository

enum class Theme(
    val displayName : String,
    val lightColors : Colors,
    val darkColors : Colors
) {
    BLUE("Blue", blueAppTheme, blueAppDarkTheme),
    ORANGE("Orange", orangeAppTheme, orangeAppDarkTheme),
}


val blueAppTheme = lightColors(
    primary = primaryBlue,
    primaryVariant = darkBlue,
    secondary = accentPink,
    secondaryVariant = darkPink,
    error = red,

)

val blueAppDarkTheme = darkColors(
    primary = primaryBlue,
    primaryVariant = darkBlue,
    secondary = grey,
    error = red,

)

val orangeAppTheme = lightColors(
    primary = darkOrange,
    primaryVariant = darkBlue,
    secondary = accentOrange,
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
fun AppTheme(systemInDarkTheme: Boolean = isSystemInDarkTheme(),
             userPreferencesRepository: UserPreferencesRepository,
             content: @Composable() () -> Unit) {
    val context : Context = LocalContext.current
    val theme = userPreferencesRepository.getTheme().collectAsState(initial = Theme.BLUE)

   // val colorScheme = ColorScheme()

    val useSystemDarkThemePref = userPreferencesRepository.getSystemDarkMode().collectAsState(initial = true)
    val useDarkThemePref = userPreferencesRepository.getDarkMode().collectAsState(initial = false)

    Log.i("logg", "config: ${LocalConfiguration.current.uiMode}")
    val useDarkTheme = if (useSystemDarkThemePref.value) {
        systemInDarkTheme
    } else {
        useDarkThemePref.value
    }

    MaterialTheme(
        colorScheme = if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context),
//        colors = if (useDarkTheme) theme.value.darkColors else theme.value.lightColors,
        content = content
    )
}

