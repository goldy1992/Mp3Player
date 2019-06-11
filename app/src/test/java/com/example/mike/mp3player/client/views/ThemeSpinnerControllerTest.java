package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.widget.Spinner;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.activities.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ThemeSpinnerControllerTest {

    private ThemeSpinnerController themeSpinnerController;
    @Mock
    private MainActivity mainActivity = mock(MainActivity.class);
    @Mock
    private Spinner spinner;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.themeSpinnerController = new ThemeSpinnerController(context, spinner, mainActivity);
    }
    @Test
    public void onItemSelected() {
        themeSpinnerController.onItemSelected(null, null, 1, 2342L);
        // first time called by android, we don't want to change anything
        verify(mainActivity, never()).finish();
        themeSpinnerController.onItemSelected(null, null, 1, 2342L);
        // second time called by user
        verify(mainActivity, times(1)).finish();
        assertTrue(true);
    }
}