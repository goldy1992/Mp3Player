package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;

    private MediaRetriever mediaRetriever;
    private Map<Category, LibraryCollection> categories;
    private Context context;
    private TreeSet<MediaItem> rootItems = new TreeSet<>(compareRootMediaItemsByCategory);
    private final String LOG_TAG = "MEDIA_LIBRARY";

    public MediaLibrary(Context context) {
        this.context = context;
        this.mediaRetriever = new ContentResolverMediaRetriever(context);
        categories = new HashMap<>();
    }
    public void init() {
        SongCollection songs = new SongCollection();
        FolderLibraryCollection folders = new FolderLibraryCollection();
        categories.put(songs.getRootId(), songs);
        categories.put(folders.getRootId(), folders);
        buildMediaLibrary();
    }

    private void buildMediaLibrary(){
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
