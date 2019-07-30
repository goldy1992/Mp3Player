package com.example.mike.mp3player.client.utils;

import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;


@RunWith(RobolectricTestRunner.class)
public class IntentUtilsTest {

    @Test
    public void testCreateGoToMediaPlayerActivity() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent result = IntentUtils.createGoToMediaPlayerActivity(context);
        assertNotNull(result);
    }
}