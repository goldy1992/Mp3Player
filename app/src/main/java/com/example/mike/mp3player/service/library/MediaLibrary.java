package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.Range;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.commons.library.LibraryResponse;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

public class MediaLibrary {
    private static final int INITAL_ITEMS_BUFFER = 10;
    private static final Range INITAL_ITEMS_RANGE = Range.create(0, INITAL_ITEMS_BUFFER);

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

    public Set<MediaItem> getSongList() {
        return categories.get(Category.SONGS).getKeys();
    }

    /**
     * Used by MediaSessionCallback to get the children of the MediaItem requested in the
     * libraryRequest param.
     * @param libraryRequest libraryRequest
     * @return A list of MediaItems in the requested libraryRequest
     */
    public List<MediaItem> getPlaylist(LibraryRequest libraryRequest) {
        Category category = libraryRequest.getCategory();
        if (category == Category.SONGS) {
            // song items don't have children therefore just return all songs
            return new ArrayList<>(categories.get(category).getKeys());
        } else {
            return new ArrayList<>(categories.get(category).getChildren(libraryRequest, null));
        }
    }

    public MediaItem getItem(LibraryRequest libraryRequest) {
        return null;
    }

    public LibraryResponse populateLibraryResponse(LibraryResponse libraryResponse) {

        LibraryCollection collection = categories.get(libraryResponse.getCategory());

        if (Category.isCategory(libraryResponse.getId())) {
            libraryResponse.setTotalNumberOfChildren(collection.getKeys().size());
        } else {
            libraryResponse.setTotalNumberOfChildren(collection.getNumberOfChildren(libraryResponse.getId()));
        }
        libraryResponse.setMediaItem(collection.getRoot());
        return libraryResponse;
    }

    /**
     *
     * @param libraryResponse the response object
     * @param resultSize the resultSize
     * @return the updated library response
     */
    public LibraryResponse updateResponse(LibraryResponse libraryResponse, int resultSize) {
        int newResultSize = libraryResponse.getRange().getLower() + resultSize;
        libraryResponse.setResultSize(newResultSize);
        return libraryResponse;
    }

    public Set<MediaItem> getChildren(LibraryRequest libraryRequest) {
        if (libraryRequest == null || libraryRequest.getCategory() == null) {
            return null;
        }
        Range range = libraryRequest.getRange();
        LibraryCollection collection = categories.get(libraryRequest.getCategory());

        if (Category.isCategory(libraryRequest.getId())) {
            return collection.getKeys(range);
        } else {
            return collection.getChildren(libraryRequest, range);
        }
    }

    /**
     * returns all of the Category ids as well as the first 10 of each category as to avoid a
     * @link{android.os.TransactionTooLargeException} when passing the initial data to the
     * MainActivity.
     *
     * @return A TreeSet of MediaItems
     */
    private Set<MediaItem> getRootItems() {
        Set<MediaItem> toReturn = new TreeSet<>();
        toReturn.addAll(categories.get(Category.ROOT).getKeys());
        // get first ten of each item
        for (Category category : categories.keySet()) {
            if (category != Category.ROOT) {
                LibraryCollection lc = categories.get(category);
                Set<MediaItem> subSet = MediaLibraryUtils.getSubSetFromRange((TreeSet<MediaItem>) lc.getKeys(), INITAL_ITEMS_RANGE);
                toReturn.addAll(subSet);
            }
        }
        return toReturn;
    }

    public Uri getMediaUriFromMediaId(String mediaId){
        for (MediaItem i : getSongList()) {
            if (i.getDescription().getMediaId().equals(mediaId)) {
                return i.getDescription().getMediaUri();
            }
        }
        return null;
     }
}
