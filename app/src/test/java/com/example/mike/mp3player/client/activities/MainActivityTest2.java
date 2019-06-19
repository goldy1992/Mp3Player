package com.example.mike.mp3player.client.activities;

import android.view.Menu;

import androidx.test.core.app.ActivityScenario;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest2 {

    MainActivity mainActivity;
    @Before
    public void setup() {
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);

    }

    @Test
    public void testGetSubscriptionType() {
        SubscriptionType expectedSubscriptionType = SubscriptionType.MEDIA_ID;
        SubscriptionType actualSubscriptionType = mainActivity.getSubscriptionType();
        assertEquals(expectedSubscriptionType, actualSubscriptionType);
    }

    @Test
    public void testCreateOptionsMenu() {
        Menu menu = mock(Menu.class);
        boolean result = this.mainActivity.onCreateOptionsMenu(menu);
        assertFalse(result);
    }
}
