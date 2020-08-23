package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.LogTagger

/**
 *
 */
class PreferencesFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, LogTagger {

    companion object {
        const val DARK_MODE = "dark_mode"
    }
    /**
     * Called during [.onCreate] to supply the preferences for this fragment.
     * Subclasses are expected to call [.setPreferenceScreen] either
     * directly or via helper methods such as [.addPreferencesFromResource].
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     * this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     * [PreferenceScreen] with this key.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        Log.i(logTag(), "log preference created")
        val switchPreferenceCompat = preferenceScreen.findPreference<SwitchPreferenceCompat>(DARK_MODE)
        switchPreferenceCompat?.onPreferenceChangeListener = this
    }

    /**
     * Called when a preference has been changed by the user. This is called before the state
     * of the preference is about to be updated and before the state is persisted.
     *
     * @param preference The changed preference
     * @param newValue   The new value of the preference
     * @return `true` to update the state of the preference with the new value
     */
    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        Log.i(logTag(), "Preference changed: $preference, new value: $newValue")
        if (DARK_MODE == preference?.key) {
            updateDarkModePreference(newValue)
        }
        return true
    }

    override fun logTag(): String {
        return "PREF_FRGMNT"
    }

    private fun updateDarkModePreference(newValue: Any?) {
        val enableDarkMode : Boolean = newValue as Boolean
        val nightMode : Int = if (enableDarkMode)  MODE_NIGHT_YES else MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

}