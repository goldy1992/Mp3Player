package com.github.goldy1992.mp3player.client.views

import android.content.Context
import android.view.View
import android.widget.Spinner
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MainActivity

import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.google.common.collect.BiMap
import com.nhaarman.mockitokotlin2.*

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThemeSpinnerControllerTest {

    private lateinit var themeSpinnerController: ThemeSpinnerController
    private lateinit var context: Context
    private val mainActivity = mock<MainActivity>()
    private val spinner: Spinner = mock<Spinner>()
    private val view: View = mock<View>()

    @Before
    @Throws(IllegalAccessException::class)
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
      }

    @Test
    fun onItemSelected() {
        themeSpinnerController = ThemeSpinnerController(context, spinner, mainActivity, ComponentClassMapper.Builder()
                .mainActivity(ThemeSpinnerController::class.java).build())
        themeSpinnerController.onItemSelected(null, view, 1, 2342L)
        // first time called by android, we don't want to change anything
        verify(mainActivity, never()).finish()
        themeSpinnerController.onItemSelected(null, view, 1, 2342L)
        // second time called by user
        verify(mainActivity, times(1)).finish()
        Assert.assertTrue(true)
    }

    @Test
    fun createThemeSpinnerControllerWithExistingThemes() {
        val themesArray = context.resources.obtainTypedArray(R.array.themes)
        val res = themesArray.getResourceId(0, 0) // res if of the FIRST THEME
        // WHEN: the activities theme name is requested, return id of the FIRST theme
        val theme = context.theme
        whenever(mainActivity.theme).thenReturn(theme)
        // SET CURRENT THEME to be expectedThemeName
        theme.applyStyle(res, true)
        //when(theme.obtainStyledAttributes(attrs)).thenReturn(themeNameArray);
        themeSpinnerController = ThemeSpinnerController(context, spinner, mainActivity, ComponentClassMapper.Builder().build())
        val themeNameArray = context.obtainStyledAttributes(res, themeSpinnerController.attrs)
        val expectedThemeName = themeNameArray.getString(0)
        // WHEN we use the id of the FIRST theme in the themeSpinners map we get the correct theme name returned
        val map: BiMap<Int, String> = themeSpinnerController.themeNameToResMap.inverse()
        Assert.assertEquals(expectedThemeName, map[res])
        Assert.assertEquals(expectedThemeName, themeSpinnerController.currentTheme)
    }

    @Test
    fun createThemeSpinnerControllerForActivityWithNullId() {
        whenever(mainActivity.theme).thenReturn(null)
        themeSpinnerController = ThemeSpinnerController(context, spinner, mainActivity, ComponentClassMapper.Builder().build())
    }
}