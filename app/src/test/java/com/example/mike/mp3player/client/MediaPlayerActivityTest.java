package com.example.mike.mp3player.client;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.service.ShadowMediaSessionCompat_Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 26, shadows = {ShadowMediaSessionCompat_Token.class})
public class MediaPlayerActivityTest {

    private static final String MOCK_MEDIA_ID = "MOCK_MEDIA_ID";

    MediaPlayerActivity mediaPlayerActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        MediaSessionCompat ms = new MediaSessionCompat(context, "TAG");
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra(Constants.MEDIA_SESSION, ms.getSessionToken());
        intent.putExtra(Constants.MEDIA_ID, MOCK_MEDIA_ID);
        mediaPlayerActivity = Robolectric.buildActivity(MediaPlayerActivity.class, intent).create().get();
    }

    @Before
    public void oldSetup() {
        setup();
    }

    /**
     * GIVEN: Intent i, with Uri u to MediaPlayerActivity m,
     * WHEN: m is created
     * THEN: i contains u.
     */
    @Test
    public void onCreateSetMediaIdTest() {
       assertEquals(MOCK_MEDIA_ID, mediaPlayerActivity.getMediaId());
    }
}