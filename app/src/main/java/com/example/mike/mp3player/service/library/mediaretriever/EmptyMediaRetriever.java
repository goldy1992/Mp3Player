package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to test when there are no retrievable media on the device
 */
public class EmptyMediaRetriever extends MediaRetriever {

    public EmptyMediaRetriever(Context context) {
        super(context);
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> retrieveMedia() {
        return new ArrayList<>();
    }
}
