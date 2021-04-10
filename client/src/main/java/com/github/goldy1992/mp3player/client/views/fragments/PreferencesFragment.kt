package com.github.goldy1992.mp3player.client.views.fragments

import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.commons.Constants.CURRENT_THEME
import com.github.goldy1992.mp3player.commons.Constants.DARK_MODE
import com.github.goldy1992.mp3player.commons.LogTagger

/**
 *
 */
class PreferencesFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, LogTagger,
SharedPreferences.OnSharedPreferenceChangeListener {

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

        val themeSelect = preferenceScreen.findPreference<ListPreference>(CURRENT_THEME)
        val themeMap = getThemes()

        // map theme names to resource ids in settings
        themeSelect?.entries = themeMap.keys.toTypedArray()
        val intValues : Collection<Int> = themeMap.values
        val charSeqValues : MutableList<CharSequence> = ArrayList()
        intValues.forEachIndexed { index: Int, i: Int ->
            charSeqValues.add(index, i.toString())
        }
        themeSelect?.entryValues = charSeqValues.toTypedArray()
   }

    private fun registerSharedPreferencesListener() {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .registerOnSharedPreferenceChangeListener(this)
    }

    private fun unregisterSharedPreferencesListener() {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .unregisterOnSharedPreferenceChangeListener(this)
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
        when (preference?.key) {
            DARK_MODE -> updateDarkModePreference(newValue)
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

    /**
     * Returns a map of themeNames to their corresponding resourceIds
     */
    private fun getThemes() : Map<String, Int> {
        val themeNameMap : MutableMap<String, Int> = HashMap()
        val attrs = intArrayOf(R.attr.themeName)
        val context = requireContext()
        val themeArray = context.resources.obtainTypedArray(R.array.themes)
        if (themeArray.length() > 0) {
            val numberOfThemes = themeArray.length()
            for (i in 0 until numberOfThemes) { // for each theme in the theme array
                val res = themeArray.getResourceId(i, 0)
                val themeTypedArray = context.obtainStyledAttributes(res, attrs) // get the theme name GIVEN the themes res if.
                val themeName : String? = themeTypedArray.getString(0)

                if (null != themeName) {
                    themeNameMap[themeName] = res
                }

                Log.i(logTag(), "onCreate preferences")
                recycleTypedArray(themeTypedArray)
            }

        }
        recycleTypedArray(themeArray)
        return HashMap(themeNameMap)
    }

    /**
     *
     */
    private fun recycleTypedArray(typedArray: TypedArray?) {
        typedArray?.recycle()
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     *
     *
     * This callback will be run on your main thread.
     *
     * @param sharedPreferences The [SharedPreferences] that received
     * the change.
     * @param key The key of the preference that was changed, added, or
     * removed.
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key) {
            CURRENT_THEME -> {
                /* MUST: unregister listener before recreating activity to avoid null pointer if the
                * fragment is still in memory after being detached. */
                PreferenceManager.getDefaultSharedPreferences(requireContext()).unregisterOnSharedPreferenceChangeListener(this)
                requireActivity().recreate()
                Log.i(logTag(), "hit after activity recreate")
                this.onDestroy()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerSharedPreferencesListener()
    }

    override fun onPause() {
        super.onPause()
        unregisterSharedPreferencesListener()
    }

}
