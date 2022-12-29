package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository

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
fun AppTheme(userPreferencesRepository: IUserPreferencesRepository,
             content: @Composable() () -> Unit) {
    MaterialTheme(
        colorScheme = getColorScheme(userPreferencesRepository),
        content = content
    )
}

@Composable
private fun getColorScheme(userPreferencesRepository: IUserPreferencesRepository) : ColorScheme {
    val context : Context = LocalContext.current
    val systemInDarkTheme = isSystemInDarkTheme()
    val useSystemDarkThemePref = userPreferencesRepository.getSystemDarkMode().collectAsState(initial = true)
    val useDarkThemePref = userPreferencesRepository.getDarkMode().collectAsState(initial = false)

    Log.i("logg", "config: ${LocalConfiguration.current.uiMode}")
    val useDarkTheme = if (useSystemDarkThemePref.value) {
        systemInDarkTheme
    } else {
        useDarkThemePref.value
    }

    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (useDarkTheme) darkColorScheme() else lightColorScheme()
    }

}