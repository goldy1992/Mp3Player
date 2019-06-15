package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.widget.Spinner;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.google.common.collect.BiMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.client.views.ThemeSpinnerController.attrs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ThemeSpinnerControllerTest {


    private ThemeSpinnerController themeSpinnerController;
    @Mock
    private MainActivity mainActivity = mock(MainActivity.class);
    @Mock
    private Spinner spinner;
    private Context context;
    @Before
    public void setup() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
     }
    @Test
    public void onItemSelected() {
        this.themeSpinnerController = new ThemeSpinnerController(context, spinner, mainActivity);
        themeSpinnerController.onItemSelected(null, null, 1, 2342L);
        // first time called by android, we don't want to change anything
        verify(mainActivity, never()).finish();
        themeSpinnerController.onItemSelected(null, null, 1, 2342L);
        // second time called by user
        verify(mainActivity, times(1)).finish();
        assertTrue(true);
    }

    @Test
    public void createThemeSpinnerControllerWithExistingThemes() {
        TypedArray themesArray = context.getResources().obtainTypedArray(R.array.themes);
        int res = themesArray.getResourceId(0, 0); // res if of the FIRST THEME
        TypedArray themeNameArray = context.obtainStyledAttributes(res, ThemeSpinnerController.attrs);
        String expectedThemeName = themeNameArray.getString(0);

        // WHEN: the activities theme name is requested, return id of the FIRST theme
        Resources.Theme theme = context.getTheme();
        when(mainActivity.getTheme()).thenReturn(theme);
        // SET CURRENT THEME to be expectedThemeName
        theme.applyStyle(res, true);
        //when(theme.obtainStyledAttributes(attrs)).thenReturn(themeNameArray);

        this.themeSpinnerController = new ThemeSpinnerController(context, spinner, mainActivity);
        // WHEN we use the id of the FIRST theme in the themeSpinners map we get the correct theme name returned
        BiMap<Integer, String> map = themeSpinnerController.getThemeNameToResMap().inverse();
        assertEquals(expectedThemeName, map.get(res));
        assertEquals(expectedThemeName, themeSpinnerController.getCurrentTheme());
    }

    @Test
    public void createThemeSpinnerControllerForActivityWithNullId() {
        when(mainActivity.getTheme()).thenReturn(null);
        this.themeSpinnerController = new ThemeSpinnerController(context, spinner, mainActivity);


    }
}