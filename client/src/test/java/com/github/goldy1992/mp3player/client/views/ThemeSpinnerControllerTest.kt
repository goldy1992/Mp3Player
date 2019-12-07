package com.github.goldy1992.mp3player.client.views

import android.content.Context
import android.view.View
import android.widget.Spinner
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.views.ThemeSpinnerController
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.google.common.collect.BiMap
import org.apache.commons.lang3.reflect.FieldUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThemeSpinnerControllerTest {
    private lateinit var attrs: IntArray
    private var themeSpinnerController: ThemeSpinnerController? = null
    @Mock
    private val mainActivity = Mockito.mock(MainActivity::class.java)
    @Mock
    private val spinner: Spinner? = null

    @Mock
    private val view: View? = null
    private var context: Context? = null
    @Before
    @Throws(IllegalAccessException::class)
    fun setup() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().context
        attrs = FieldUtils.readDeclaredStaticField(ThemeSpinnerController::class.java, "attrs", true) as IntArray
    }

    @Test
    fun onItemSelected() {
        themeSpinnerController = ThemeSpinnerController(context!!, spinner!!, mainActivity, ComponentClassMapper.Builder().build())
        themeSpinnerController!!.onItemSelected(null, view!!, 1, 2342L)
        // first time called by android, we don't want to change anything
        Mockito.verify(mainActivity, Mockito.never()).finish()
        themeSpinnerController!!.onItemSelected(null, view!!, 1, 2342L)
        // second time called by user
        Mockito.verify(mainActivity, Mockito.times(1)).finish()
        Assert.assertTrue(true)
    }

    @Test
    fun createThemeSpinnerControllerWithExistingThemes() {
        val themesArray = context!!.resources.obtainTypedArray(R.array.themes)
        val res = themesArray.getResourceId(0, 0) // res if of the FIRST THEME
        val themeNameArray = context!!.obtainStyledAttributes(res, attrs)
        val expectedThemeName = themeNameArray.getString(0)
        // WHEN: the activities theme name is requested, return id of the FIRST theme
        val theme = context!!.theme
        Mockito.`when`(mainActivity.theme).thenReturn(theme)
        // SET CURRENT THEME to be expectedThemeName
        theme.applyStyle(res, true)
        //when(theme.obtainStyledAttributes(attrs)).thenReturn(themeNameArray);
        themeSpinnerController = ThemeSpinnerController(context!!, spinner!!, mainActivity, ComponentClassMapper.Builder().build())
        // WHEN we use the id of the FIRST theme in the themeSpinners map we get the correct theme name returned
        val map: BiMap<Int, String> = themeSpinnerController?.themeNameToResMap!!.inverse()
        Assert.assertEquals(expectedThemeName, map[res])
        Assert.assertEquals(expectedThemeName, themeSpinnerController!!.currentTheme)
    }

    @Test
    fun createThemeSpinnerControllerForActivityWithNullId() {
        Mockito.`when`(mainActivity.theme).thenReturn(null)
        themeSpinnerController = ThemeSpinnerController(context!!, spinner!!, mainActivity, ComponentClassMapper.Builder().build())
    }
}