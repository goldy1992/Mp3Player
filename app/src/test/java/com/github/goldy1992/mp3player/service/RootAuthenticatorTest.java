package com.github.goldy1992.mp3player.service;

import androidx.media.MediaBrowserServiceCompat;

import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME;
import static com.github.goldy1992.mp3player.service.RootAuthenticator.REJECTED_MEDIA_ROOT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class RootAuthenticatorTest {

    private MediaItemTypeIds mediaItemTypeIds;
    private RootAuthenticator rootAuthenticator;
    private final String ACCEPTED_MEDIA_ID = "ACCEPTED_MEDIA_ID";

    @Before
    public void setup() {
        this.mediaItemTypeIds = new MediaItemTypeIds();
        this.rootAuthenticator = new RootAuthenticator(mediaItemTypeIds);
    }

    @Test
    public void testGetAcceptedId() {
        String packageNameToAccept = new StringBuilder()
                .append("myPackage")
                .append(PACKAGE_NAME)
                .toString();
        MediaBrowserServiceCompat.BrowserRoot result = rootAuthenticator.authenticate(packageNameToAccept, 0, null);
        assertEquals(ACCEPTED_MEDIA_ID, result.getRootId());
    }

    @Test
    public void testGetRejectedId() {
        String packageNameToAccept = new StringBuilder()
                .append("myPackage")
                .toString();
        MediaBrowserServiceCompat.BrowserRoot result = rootAuthenticator.authenticate(packageNameToAccept, 0, null);
        assertEquals(REJECTED_MEDIA_ROOT_ID, result.getRootId());
    }

    @Test
    public void subscribeWithRejectedId() {
        assertTrue(rootAuthenticator.rejectRootSubscription(REJECTED_MEDIA_ROOT_ID));
    }

}