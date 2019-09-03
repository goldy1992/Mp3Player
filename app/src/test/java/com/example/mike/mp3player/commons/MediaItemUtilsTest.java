package com.example.mike.mp3player.commons;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(RobolectricTestRunner.class)
public class MediaItemUtilsTest {

    @Test
    public void testGetExtrasNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder()
                .setMediaId("anId")
                .build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertNull(MediaItemUtils.getExtras(mediaItem));
    }

    @Test
    public void testGetExtrasNotNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle("algo") // sets an extra
                .build();
        Bundle result = MediaItemUtils.getExtras(mediaItem);
        assertNotNull(result);
    }

    @Test
    public void testGetMediaIdNotNull() {
        final String mediaId = "MEDIA_ID";
        MediaItem mediaItem = new MediaItemBuilder(mediaId).build();
        assertEquals(mediaId, MediaItemUtils.getMediaId(mediaItem));
    }

    @Test
    public void testGetTitleNull() {
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(null)
                .build();
        assertNull(MediaItemUtils.getTitle(mediaItem));
    }

    @Test
    public void testGetTitleNotNull() {
        final String title = "TITLE";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setTitle(title)
                .build();
        assertEquals(title, MediaItemUtils.getTitle(mediaItem));
    }

    @Test
    public void testGetDescriptionNull() {

        MediaItem mediaItem = new MediaItemBuilder("id")
                .setDescription(null)
                .build();
        assertNull(MediaItemUtils.getDescription(mediaItem));
    }

    @Test
    public void testGetDescriptionNotNull() {
        final String description = "description";
        MediaItem mediaItem = new MediaItemBuilder("id")
                .setDescription(description)
                .build();
        assertEquals(description, MediaItemUtils.getDescription(mediaItem));
    }

}