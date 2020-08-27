package com.github.goldy1992.mp3player


import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.PreferenceManager
import com.github.goldy1992.mp3player.commons.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val currentThemeResId : Int? = sharedPreferences.getString(Constants.CURRENT_THEME, null)?.toInt()// Int? = currentTheme?.
        if (currentThemeResId != null) {
            setTheme(currentThemeResId)
        }

        val darkMode = if (sharedPreferences.getBoolean("dark_mode", false)) MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(darkMode)

    }
}