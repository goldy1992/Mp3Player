package com.example.mike.mp3player.client.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(RobolectricTestRunner.class)
public class IntentUtilsTest {

    @Test
    public void testCreateGoToMediaPlayerActivity() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");
        Intent result = IntentUtils.createGoToMediaPlayerActivity(context);
        assertNotNull(result);
        assertEquals(mediaSessionCompat.getSessionToken(), result.getParcelableExtra(MEDIA_SESSION));
    }
}