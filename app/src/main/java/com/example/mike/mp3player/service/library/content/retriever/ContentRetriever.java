package com.example.mike.mp3player.service.library.content.retriever;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.ContentRequest;
import com.example.mike.mp3player.service.library.utils.IdGenerator;

import java.util.List;

public abstract class ContentRetriever {

    public abstract List<MediaBrowserCompat.MediaItem> getChildren(ContentRequest request);
    public abstract MediaItemType getType();


}
