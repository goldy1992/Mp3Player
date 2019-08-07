package com.example.mike.mp3player.service.library;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.Map;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;
    private Map<Category, LibraryCollection> categories;
    private final String LOG_TAG = "MEDIA_LIBRARY";

    @Inject
    public MediaLibrary(Map<Category, LibraryCollection> categoriesMap) {
        this.categories = categoriesMap;
    }

    public TreeSet<MediaItem> getChildren(@NonNull LibraryRequest libraryRequest) {

        String id = libraryRequest.getId();
        if (Category.isCategory(id)) {
            Category category = Category.valueOf(id);
            return categories.get(category).getAllChildren();
        } else {
            LibraryCollection libraryCollection = categories.get(libraryRequest.getCategory());
            if (null != libraryCollection) {
                return libraryCollection.getChildren(libraryRequest);
            }
        }
        return null;
    }

    public Uri getMediaUriFromMediaId(String mediaId){
        for (MediaItem i : getSongList()) {
            if (i.getDescription().getMediaId().equals(mediaId)) {
                return i.getDescription().getMediaUri();
            }
        }
        return null;
    }


    @VisibleForTesting
    Map<Category, LibraryCollection> getCategories() {
        return categories;
    }



}
