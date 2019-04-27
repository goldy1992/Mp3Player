package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;

import java.util.List;

public class MockMediaRetriever extends MediaRetriever {

    public MockMediaRetriever(Context context) {
        super(context);
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> retrieveMedia() {
        return null;
    }
}