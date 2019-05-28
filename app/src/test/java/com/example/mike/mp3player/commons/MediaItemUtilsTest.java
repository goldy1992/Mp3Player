package com.example.mike.mp3player.commons;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;

import org.junit.jupiter.api.Test;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MediaItemUtilsTest {

    @Test
    void testGetExtrasNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertNull(MediaItemUtils.getExtras(mediaItem));
    }

    @Test
    void testGetExtrasNotNull() {
        final Bundle bundle = new Bundle();
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setExtras(bundle).build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertEquals(bundle, MediaItemUtils.getExtras(mediaItem));
    }

    @Test
    void testGetMediaIdNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertNull(MediaItemUtils.getMediaId(mediaItem));
    }

    @Test
    void testGetMediaIdNotNull() {
        final String mediaId = "MEDIA_ID";
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setMediaId(mediaId).build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertEquals(mediaId, MediaItemUtils.getMediaId(mediaItem));
    }

    @Test
    void testGetTitleNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertNull(MediaItemUtils.getTitle(mediaItem));
    }

    @Test
    void testGetTitleNotNull() {
        final String title = "TITLE";
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setTitle(title).build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertEquals(title, MediaItemUtils.getTitle(mediaItem));
    }

    @Test
    void testGetDescriptionNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertNull(MediaItemUtils.getDescription(mediaItem));
    }

    @Test
    void testGetDescriptionNotNull() {
        final String description = "description";
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setDescription(description).build();
        MediaItem mediaItem = new MediaItem(mediaDescription, 0);
        assertEquals(description, MediaItemUtils.getDescription(mediaItem));
    }

}