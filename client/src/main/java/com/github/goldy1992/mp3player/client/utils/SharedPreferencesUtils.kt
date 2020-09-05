package com.github.goldy1992.mp3player.client.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.github.goldy1992.mp3player.commons.Constants

class SharedPreferencesUtils {

    companion object {

        fun getCurrentThemeResourceId(context: Context) : Int? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(Constants.CURRENT_THEME, null)?.toInt()
        }

        fun isSetDarkMode(context: Context) : Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean("dark_mode", false)
        }

    }
}