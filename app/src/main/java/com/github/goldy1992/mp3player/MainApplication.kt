package com.github.goldy1992.mp3player


import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.github.goldy1992.mp3player.client.utils.SharedPreferencesUtils.Companion.getCurrentThemeResourceId
import com.github.goldy1992.mp3player.client.utils.SharedPreferencesUtils.Companion.isSetDarkMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val currentThemeResId = getCurrentThemeResourceId(applicationContext)
        if (currentThemeResId != null) {
            setTheme(currentThemeResId)
        }

        val darkMode = if (isSetDarkMode(applicationContext)) MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(darkMode)

    }
}