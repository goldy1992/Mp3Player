package com.example.mike.mp3player.client.activities;

import android.view.MenuItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private ActivityController<TestMainActivity> mainActivityTestActivityController;

    @Before
    public void setup() {
        mainActivityTestActivityController = Robolectric.buildActivity(TestMainActivity.class).setup();
     }

    @After
    public void tearDown() {
        mainActivityTestActivityController.destroy();
    }

    @Test
    public void testOnItemSelected() {
        MenuItem menuItem = mock(MenuItem.class);
        boolean result = mainActivityTestActivityController.get().onOptionsItemSelected(menuItem);
        assertFalse(result);
    }

    @Test
    public void testOnItemSelectedHomeButton() {
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        boolean result = mainActivityTestActivityController.get().onOptionsItemSelected(menuItem);
        assertTrue(result);
   }

}