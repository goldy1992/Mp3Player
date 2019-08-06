package com.example.mike.mp3player.service.library;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.service.library.db.AppDatabase;
import com.example.mike.mp3player.service.library.db.Folder;
import com.example.mike.mp3player.service.library.db.Song;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

@Singleton
public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;
    private final MediaRetriever mediaRetriever;
    private final AppDatabase database;

    private Map<Category, LibraryCollection> categories;
    private TreeSet<MediaItem> rootItems = new TreeSet<>(compareRootMediaItemsByCategory);
    private final String LOG_TAG = "MEDIA_LIBRARY";

    @Inject
    public MediaLibrary(MediaRetriever mediaRetriever, AppDatabase appDatabase) {
        this.mediaRetriever = mediaRetriever;
        this.database = appDatabase;
        this.categories = new EnumMap<>(Category.class);
        init();
    }
    private void init() {
        RootLibraryCollection rootLibraryCollection = new RootLibraryCollection(database.rootDao(), mediaRetriever.getContext());
        categories.put(rootLibraryCollection.getRootId(), rootLibraryCollection);
        SongCollection songs = new SongCollection(database.songDao(), mediaRetriever.getContext());
        FolderLibraryCollection folders = new FolderLibraryCollection(database.folderDao(), mediaRetriever.getContext());
        getCategories().put(songs.getRootId(), songs);
        getCategories().put(folders.getRootId(), folders);
    }

//   public void buildDbMediaLibrary() {
//        RootLibraryCollection rootLibraryCollection = new RootLibraryCollection(database.rootDao(), mediaRetriever.getContext());
//        categories.put(rootLibraryCollection.getRootId(), rootLibraryCollection);
//
//        List<MediaItem> songList = mediaRetriever.retrieveMedia();
//
//        Set<Folder> folders = new HashSet<>();
//        Set<Song> songs = new HashSet<>();
//        for (MediaItem m : songList) {
//            Bundle extras = m.getDescription().getExtras();
//            String directoryName = extras.getString(META_DATA_PARENT_DIRECTORY_NAME);
//            String directoryPath = extras.getString(META_DATA_PARENT_DIRECTORY_PATH);
//            Folder folder = new Folder();
//            folder.name = directoryName;
//            folder.path = directoryPath;
//            folders.add(folder);
//            Song song = new Song();
//            song.uri = m.getMediaId();
//            song.duration = extras.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
//            song.artist = extras.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
//            song.folder = folder;
//            m.getDescription().getExtras();
//            songs.add(song);
//        }
//
//        database.songDao().insertAll(songs.toArray(new Song[songs.size()]));
//        database.folderDao().insertAll(folders.toArray(new Folder[folders.size()]));
//    }
//
//    public void buildMediaLibrary(){
//        List<MediaItem> songList = mediaRetriever.retrieveMedia();
//        for (Category category : getCategories().keySet()) {
//            getCategories().get(category).index(songList);
//        }
//
//        List<MediaItem> rootCategoryMediaItems = new ArrayList<>();
//        for (LibraryCollection collection : getCategories().values()) {
//            rootCategoryMediaItems.add(collection.getRoot());
//        }
//        RootLibraryCollection rootLibraryCollection = new RootLibraryCollection(null, mediaRetriever.getContext());
//        rootLibraryCollection.index(rootCategoryMediaItems);
//        categories.put(rootLibraryCollection.getRootId(), rootLibraryCollection);
//    }



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

    public TreeSet<MediaItem> getSongList() {
        return getCategories().get(Category.SONGS).getKeys();
    }

//    public List<MediaItem> getPlaylist(LibraryObject libraryObject) {
//        LibraryRequest libraryRequest = new LibraryRequest(libraryObject);
//        Category category = libraryObject.getCategory();
//        if (category == Category.SONGS) {
//            // song items don't have children therefore just return all songs
//
//            return categories.get(Category.SONGS).getAllChildren();
//        } else
//            return new ArrayList<>(getCategories().get(category).getChildren(libraryObject));
//    }

    @VisibleForTesting
    Map<Category, LibraryCollection> getCategories() {
        return categories;
    }



}
