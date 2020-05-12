package com.github.goldy1992.mp3player.client.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import java.util.*
import javax.inject.Inject

class ThemeSpinnerController
    @Inject
    constructor( private val context: Context,
                 private val spinner: Spinner,
                 private val activity: Activity,
                 private val componentClassMapper : ComponentClassMapper)
    : OnItemSelectedListener, LogTagger {
    private var adapter // TODO: make a make from Theme name to resource
            : ArrayAdapter<String?>? = null
    private var themeResIds: MutableList<Int>? = null
    val attrs = intArrayOf(R.attr.themeName)


     var themeNameToResMap: BiMap<String, Int> = HashBiMap.create()

    private var selectCount: Long = 0
    var currentTheme: String? = null
        private set


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val res = themeResIds!![position]
        Log.d(logTag(), "selected " + themeNameToResMap.inverse()[res])
        if (selectCount >= 1) {
            Log.d(logTag(), "select count > 1")
            setThemePreference(res)
            activity.finish()
            val intent = Intent(context, componentClassMapper.mainActivity)
            intent.putExtra(Constants.THEME, res)
            activity.startActivity(intent)
        }
        selectCount++
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    /**
     *
     * @return
     */
    private val currentThemeId: Int
        get() {
            val activityTheme = activity.theme
            if (null != activityTheme) {
                val themeNameArray = activityTheme.obtainStyledAttributes(attrs)
                if (null != themeNameArray && themeNameArray.length() > 0) {
                    val themeName = themeNameArray.getString(0)
                    themeNameArray.recycle()
                    Log.d(logTag(), "current theme is: $themeName")
                    val result = themeNameToResMap[themeName]
                    return result ?: -1
                }
            }
            return -1
        }

    /**
     *
     * @param themeId the theme id
     */
    private fun setThemePreference(themeId: Int) {
        val settings = context.getSharedPreferences(Constants.THEME, Context.MODE_PRIVATE)
        if (settings != null) {
            val editor = settings.edit()
            if (editor != null) {
                editor.putInt(Constants.THEME, themeId)
                editor.apply()
            }
        }
    }

    /**
     *
     */
    private fun recycleTypedArray(typedArray: TypedArray?) {
        typedArray?.recycle()
    }



    init {
        adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        val themeArray = context.resources.obtainTypedArray(R.array.themes)
        themeResIds = ArrayList()
        if (themeArray.length() > 0) {
            val numberOfResources = themeArray.length()
            for (i in 0 until numberOfResources) { // for each theme in the theme array
                val res = themeArray.getResourceId(i, 0)
                themeResIds?.add(res)
                val themeNameArray = context.obtainStyledAttributes(res, attrs) // get the theme name GIVEN the themes res if.
                val themeName = themeNameArray.getString(0)
                adapter!!.add(themeName)
                themeNameToResMap[themeName] = res
                recycleTypedArray(themeNameArray)
            }
        }
        recycleTypedArray(themeArray)
        val currentThemeId = currentThemeId
        if (currentThemeId != -1) {
            currentTheme = themeNameToResMap.inverse()[currentThemeId]
            val position = adapter!!.getPosition(currentTheme)
            spinner.setSelection(position)
        }
    }

    override fun logTag(): String {
       return "THM_SPNR_CTLR"
    }
}