package com.example.mike.mp3player.client.activities;


import androidx.test.core.app.ActivityScenario;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {
    private MainActivity mainActivity;
    private ActivityScenario<MainActivity> activityScenario;

    @Before
    public void setup() {
        this.mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();

    }

    @Test
    public void testGetSubscriptionType() {
        SubscriptionType expectedSubscriptionType = SubscriptionType.MEDIA_ID;
        SubscriptionType actualSubscriptionType = mainActivity.getSubscriptionType();
        assertEquals(expectedSubscriptionType, actualSubscriptionType);
    }

    private void getSubscriptionType(MainActivity activity) {
    }
    private void initActivity(MainActivity activity) {
    //    activity.
    }
}