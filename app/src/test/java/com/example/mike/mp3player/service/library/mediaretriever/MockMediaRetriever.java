package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.mike.mp3player.TestUtils.createMediaItem;

/**
 * Mock Media Retriever used to test the MediaLibrary
 */
public class MockMediaRetriever extends MediaRetriever {

    public static final int NUM_OF_SONGS = 5;
    public MockMediaRetriever(Context context) {
        super(context);
    }

    @Override
    public List<MediaItem> retrieveMedia() {
        List<MediaItem> mediaItems = new ArrayList<>();
        IntStream.rangeClosed(1, NUM_OF_SONGS).forEach(i -> {
            String id = String.valueOf(i);
            mediaItems.add(
                    createMediaItem(id, id, id));

        });
        return mediaItems;
    }
}
