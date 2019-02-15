package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;
import com.example.mike.mp3player.service.library.utils.IsDirectoryFilter;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;
import com.example.mike.mp3player.service.library.utils.MusicFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.ComparatorUtils.*;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ALBUM_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_DURATION;

public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;

    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();
    private Map<Category, LibraryCollection> categories;
    private Context context;
    private TreeSet<MediaItem> rootItems = new TreeSet<>(compareRootMediaItemsByCategory);
    private final String LOG_TAG = "MEDIA_LIBRARY";

    public MediaLibrary(Context context) {
        this.context = context;
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
        File externalStorageDirectory = MediaLibraryUtils.getExternalStorageDirectory();
        List<MediaItem> songList = buildSongList(externalStorageDirectory);
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


    private List<MediaItem> buildSongList(File directory) {
        List<File> directories = new ArrayList<>();
        List<MediaItem> mediaItems = new ArrayList<>();

        if (directory.list() != null) {
            for (String currentFileString : directory.list()) {

                if (isDirectoryFilter.accept(directory, currentFileString)) {
                    directories.add(new File(directory, currentFileString));
                } else if (musicFileFilter.accept(directory, currentFileString)) {

                    File fileToRetrieve = null;
                    try {
                        fileToRetrieve = new File(directory, currentFileString);
                        MediaItem mediaItem = createPlayableMediaItemFromFile(fileToRetrieve, directory);
                        mediaItems.add(mediaItem);
                    } catch (Exception ex) {
                        if (fileToRetrieve != null) {
                            Log.e(LOG_TAG, "could not add " + fileToRetrieve.getPath() + " to the media library");
                        } else {
                            Log.e(LOG_TAG, "file is null");
                        }
                    }
                }
            } // for
        }

        if (!directories.isEmpty()) {
            for (File directoryToBrowse : directories) {
                mediaItems.addAll(buildSongList(directoryToBrowse));
            }
        }
        return mediaItems;
    }

    public TreeSet<MediaItem> getRoot() {
        return rootItems;
    }
    public TreeSet<MediaItem> getSongList() {
        return categories.get(Category.SONGS).getKeys();
    }


    public TreeSet<MediaItem> getPlaylist(LibraryId libraryId) {
        Category category = libraryId.getCategory();
        if (category == Category.SONGS) {
            // song items don't have children therefore just return all songs
            return categories.get(category).getKeys();
        } else
        return categories.get(category).getChildren(libraryId);
    }

    public TreeSet<MediaItem> getChildren(LibraryId libraryId) {
        if (libraryId == null || libraryId.getCategory() == null) {
            return null;
        }

        if (Category.isCategory(libraryId.getId())) {
            return categories.get(libraryId.getCategory()).getKeys();
        } else {
            return categories.get(libraryId.getCategory()).getChildren(libraryId);
        }
    }

    private MediaItem createPlayableMediaItemFromFile(File file, File directory) throws Exception {
        Uri uri = Uri.fromFile(file);
        String mediaId = String.valueOf(uri.getPath().hashCode());
        String parentPath = file.getParentFile().getAbsolutePath();
        String fileName = file.getName();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);

        Bundle extras = new Bundle();
        extras.putString(STRING_METADATA_KEY_DURATION, mmr.extractMetadata(METADATA_KEY_DURATION));
        extras.putString(STRING_METADATA_KEY_ARTIST, mmr.extractMetadata(METADATA_KEY_ARTIST));
        extras.putString(STRING_METADATA_KEY_ALBUM_ARTIST, mmr.extractMetadata(METADATA_KEY_ALBUMARTIST));
        extras.putString(META_DATA_KEY_PARENT_PATH, parentPath);
        extras.putString(META_DATA_KEY_FILE_NAME, fileName);
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directory.getName());
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directory.getAbsolutePath());

        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setMediaUri(uri)
                .setTitle(MediaLibraryUtils.getSongTitle(mmr, fileName))
                .setExtras(extras);

        MediaItem mediaItem = new MediaItem(mediaDescriptionCompatBuilder.build(), MediaItem.FLAG_PLAYABLE);
        return mediaItem;
    }

    private class MediaItemComparator implements Comparator<MediaItem> {
        @Override
        public int compare(MediaItem o1, MediaItem o2) {
            String s1 = o1.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
            String s2 = o2.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
            return s1.compareTo(s2);
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
}
