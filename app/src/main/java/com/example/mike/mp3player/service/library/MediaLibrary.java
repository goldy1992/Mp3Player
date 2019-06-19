package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

@Singleton
public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;
    private MediaRetriever mediaRetriever;

    private Map<Category, LibraryCollection> categories;
    private TreeSet<MediaItem> rootItems = new TreeSet<>(compareRootMediaItemsByCategory);
    private final String LOG_TAG = "MEDIA_LIBRARY";

    @Inject
    public MediaLibrary(MediaRetriever mediaRetriever) {
        this.mediaRetriever = mediaRetriever;
        this.categories = new EnumMap<>(Category.class);
        init();
    }
    private void init() {
        SongCollection songs = new SongCollection();
        FolderLibraryCollection folders = new FolderLibraryCollection();
        getCategories().put(songs.getRootId(), songs);
        getCategories().put(folders.getRootId(), folders);
    }

    public void buildMediaLibrary(){
        List<MediaItem> songList = mediaRetriever.retrieveMedia();
        for (Category category : getCategories().keySet()) {
            getCategories().get(category).index(songList);
        }

        List<MediaItem> rootCategoryMediaItems = new ArrayList<>();
        for (LibraryCollection collection : getCategories().values()) {
            rootCategoryMediaItems.add(collection.getRoot());
        }
        RootLibraryCollection rootLibraryCollection = new RootLibraryCollection();
        rootLibraryCollection.index(rootCategoryMediaItems);
        getCategories().put(rootLibraryCollection.getRootId(), rootLibraryCollection);
    }

    public TreeSet<MediaItem> getRoot() {
        return rootItems;
    }

    public TreeSet<MediaItem> getSongList() {
        return getCategories().get(Category.SONGS).getKeys();
    }

    public List<MediaItem> getPlaylist(LibraryObject libraryObject) {
        Category category = libraryObject.getCategory();
        if (category == Category.SONGS) {
            // song items don't have children therefore just return all songs
            return new ArrayList<>(getCategories().get(category).getKeys());
        } else
            return new ArrayList<>(getCategories().get(category).getChildren(libraryObject));
    }

    public TreeSet<MediaItem> getChildren(@NonNull LibraryRequest libraryRequest) {

        if (Category.isCategory(libraryRequest.getId())) {
            return getCategories().get(libraryRequest.getCategory()).getKeys();
        } else {
            LibraryCollection libraryCollection = getCategories().get(libraryRequest.getCategory());
            if (null != libraryCollection) {
                return libraryCollection.getChildren(libraryRequest);
            }
            return null;

        }
    }

    public Uri getMediaUriFromMediaId(String mediaId){
        for (MediaItem i : getSongList()) {
            if (i.getDescription().getMediaId().equals(mediaId)) {
                return i.getDescription().getMediaUri();
            }
        }
        return null;
    }

    public boolean isPopulated() {
        return getSongList() != null && !getSongList().isEmpty();
    }


    @VisibleForTesting
    Map<Category, LibraryCollection> getCategories() {
        return categories;
    }
}
