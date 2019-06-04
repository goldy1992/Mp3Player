package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import java.util.ArrayList;
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
    private final Context context;
    private TreeSet<MediaItem> rootItems = new TreeSet<>(compareRootMediaItemsByCategory);
    private final String LOG_TAG = "MEDIA_LIBRARY";
    private boolean isInitialised = false;

    @Inject
    public MediaLibrary(Context context, MediaRetriever mediaRetriever) {
        this.context = context;
        this.mediaRetriever = mediaRetriever;
        this.categories = new HashMap<>();
        init();
    }
    private void init() {
        SongCollection songs = new SongCollection();
        FolderLibraryCollection folders = new FolderLibraryCollection();
        categories.put(songs.getRootId(), songs);
        categories.put(folders.getRootId(), folders);
        this.isInitialised = true;
    }

    public void buildMediaLibrary(){
        List<MediaItem> songList = mediaRetriever.retrieveMedia();
        for (Category category : categories.keySet()) {
            categories.get(category).index(songList);
        }

        List<MediaItem> rootCategoryMediaItems = new ArrayList<>();
        for (LibraryCollection collection : categories.values()) {
            rootCategoryMediaItems.add(collection.getRoot());
        }
        RootLibraryCollection rootLibraryCollection = new RootLibraryCollection();
        rootLibraryCollection.index(rootCategoryMediaItems);
        categories.put(rootLibraryCollection.getRootId(), rootLibraryCollection);
    }

    public TreeSet<MediaItem> getRoot() {
        return rootItems;
    }

    public TreeSet<MediaItem> getSongList() {
        return categories.get(Category.SONGS).getKeys();
    }

    public List<MediaItem> getPlaylist(LibraryObject libraryObject) {
        Category category = libraryObject.getCategory();
        if (category == Category.SONGS) {
            // song items don't have children therefore just return all songs
            return new ArrayList<>(categories.get(category).getKeys());
        } else
            return new ArrayList<>(categories.get(category).getChildren(libraryObject));
    }

    public TreeSet<MediaItem> getChildren(@NonNull LibraryRequest libraryRequest) {

        if (Category.isCategory(libraryRequest.getId())) {
            return categories.get(libraryRequest.getCategory()).getKeys();
        } else {
            return categories.get(libraryRequest.getCategory()).getChildren(libraryRequest);
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


}
