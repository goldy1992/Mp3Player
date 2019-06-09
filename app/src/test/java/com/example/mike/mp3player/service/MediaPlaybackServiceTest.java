package com.example.mike.mp3player.service;

import androidx.media.MediaBrowserServiceCompat;

import com.example.mike.mp3player.UnitTestMikesMp3Player;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.example.mike.mp3player.commons.Constants.ACCEPTED_MEDIA_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.PACKAGE_NAME;
import static com.example.mike.mp3player.commons.Constants.REJECTED_MEDIA_ROOT_ID;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(application = UnitTestMikesMp3Player.class)
public class MediaPlaybackServiceTest {
    /** object to test*/
    public MediaPlaybackService mediaPlaybackService;
    @Before
    public void setup() {
        this.mediaPlaybackService = Robolectric.buildService(MediaPlaybackService.class).create().get();
    }

    @Test
    public void testGetRootValid() {
        MediaBrowserServiceCompat.BrowserRoot result =
                mediaPlaybackService.onGetRoot(PACKAGE_NAME, 0, null);
        assertEquals(ACCEPTED_MEDIA_ROOT_ID, result.getRootId());
    }

    @Test
    public void testGetRootInvalid() {
        final String badPackageName = "bad.package.name";
        MediaBrowserServiceCompat.BrowserRoot result =
                mediaPlaybackService.onGetRoot(badPackageName, 0, null);
        assertEquals(REJECTED_MEDIA_ROOT_ID, result.getRootId());
    }

}
