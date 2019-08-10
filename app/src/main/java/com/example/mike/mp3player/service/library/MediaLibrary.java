package com.example.mike.mp3player.service.library;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.contentretriever.ContentResolverRetriever;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaLibrary {

    private boolean playlistRecursInSubDirectory = false;

    private Map<MediaItemType, ContentResolverRetriever> contentRetrieverMap;
    private ContentManager contentManager;
    private final String LOG_TAG = "MEDIA_LIBRARY";


    @Inject
    public MediaLibrary(ContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public List<MediaItem> getChildren(@NonNull String parentId) {
        return contentManager.getChildren(parentId);
    }





    public Uri getMediaUriFromMediaId(String mediaId){
        throw new UnsupportedOperationException();
    }

}
