package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.os.Build.VERSION_CODES.S
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferences
import com.github.goldy1992.mp3player.commons.VersionUtils

private const val LOG_TAG = "Theme"
//enum class Theme(
//    val displayName : String,
//    val lightColors : ColorScheme,
//    val darkColors : ColorScheme
//) {
//    BLUE("Blue", blueAppTheme, blueAppDarkTheme),
//    ORANGE("Orange", orangeAppTheme, orangeAppDarkTheme),
//}
//
//
//val blueAppTheme = ColorScheme(
//    primary = primaryBlue,
//    primaryContainer = darkBlue,
//    secondary = accentPink,
//    secondaryContainer = darkPink,
//    error = red,
//
//)

//val blueAppDarkTheme = darkColors(
//    primary = primaryBlue,
//    primaryVariant = darkBlue,
//    secondary = grey,
//    error = red,
//
//)

//val orangeAppTheme = lightColors(
//    primary = darkOrange,
//    primaryVariant = darkBlue,
//    secondary = accentOrange,
//    background = white,
//    error = red,
//    surface = white,
//    onPrimary = white,
////    onSecondary = black,
//    onBackground = black,
//    onSurface = black,
////    onError = white
//)
//
//val orangeAppDarkTheme = darkColors(
//    primary = grey,
//    primaryVariant = darkBlue,
//    secondary = grey,
//    background = black,
//    error = red,
//    surface = black,
//    onPrimary = white,
//    onSecondary = white,
//    onBackground = white,
//    onSurface = white,
//    onError = white
//)

internal val LocalIsDarkMode = staticCompositionLocalOf { false }

@Composable
fun AppTheme(userPreferencesRepository: IUserPreferencesRepository,
             content: @Composable () -> Unit) {

    val context : Context = LocalContext.current
    val systemInDarkTheme = isSystemInDarkTheme()
    val userPreferences by userPreferencesRepository.userPreferencesFlow().collectAsState(initial = UserPreferences.DEFAULT)
    val useSystemDarkThemePref = userPreferences.systemDarkMode
    val useDarkThemePref = userPreferences.darkMode
    val useDynamicColor = userPreferences.useDynamicColor

    Log.d(LOG_TAG, "getColorScheme() uiMode: ${LocalConfiguration.current.uiMode}")
    val useDarkTheme = if (useSystemDarkThemePref) {
        systemInDarkTheme
    } else {
        useDarkThemePref
    }

    MaterialTheme(
        colorScheme = getColorScheme(context, useDarkTheme, useDynamicColor),
        content = {
            CompositionLocalProvider(LocalIsDarkMode provides useDarkTheme) {
                content()
            }
        }
    )
}

private fun getColorScheme(context: Context, useDarkTheme: Boolean, useDynamicColor: Boolean) : ColorScheme {
    return if (VersionUtils.isAndroid12OrHigher()) {
        getColorSchemeAndroid12OrHigher(context, useDarkTheme, useDynamicColor)
    } else {
        getNoneDynamicColorScheme(useDarkTheme)
    }

}

@RequiresApi(S)
fun getColorSchemeAndroid12OrHigher(
    context: Context,
    useDarkTheme : Boolean = true,
    useDynamicColor : Boolean = true) : ColorScheme {
    return if (useDynamicColor) {
        if (useDarkTheme) {
           dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    } else {
        getNoneDynamicColorScheme(useDarkTheme)
    }
}

private fun getNoneDynamicColorScheme(useDarkTheme: Boolean) : ColorScheme {
    return if (useDarkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }
}