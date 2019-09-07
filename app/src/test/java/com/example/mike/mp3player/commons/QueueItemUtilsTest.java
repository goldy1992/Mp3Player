package com.example.mike.mp3player.commons;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;

import org.junit.jupiter.api.Test;

import static android.support.v4.media.session.MediaSessionCompat.QueueItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QueueItemUtilsTest {

    @Test
    void testGetExtrasNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertNull(QueueItemUtils.getExtras(QueueItem));
    }

    @Test
    void testGetExtrasNotNull() {
        final Bundle bundle = new Bundle();
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setExtras(bundle).build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertEquals(bundle, QueueItemUtils.getExtras(QueueItem));
    }

    @Test
    void testGetMediaIdNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertNull(QueueItemUtils.getMediaId(QueueItem));
    }

    @Test
    void testGetMediaIdNotNull() {
        final String mediaId = "MEDIA_ID";
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setMediaId(mediaId).build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertEquals(mediaId, QueueItemUtils.getMediaId(QueueItem));
    }

    @Test
    void testGetTitleNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertNull(QueueItemUtils.getTitle(QueueItem));
    }

    @Test
    void testGetTitleNotNull() {
        final String title = "TITLE";
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setTitle(title).build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertEquals(title, QueueItemUtils.getTitle(QueueItem));
    }

    @Test
    void testGetDescriptionNull() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertNull(QueueItemUtils.getDescription(QueueItem));
    }

    @Test
    void testGetDescriptionNotNull() {
        final String description = "description";
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder().setDescription(description).build();
        QueueItem QueueItem = new QueueItem(mediaDescription, 0);
        assertEquals(description, QueueItemUtils.getDescription(QueueItem));
    }

}