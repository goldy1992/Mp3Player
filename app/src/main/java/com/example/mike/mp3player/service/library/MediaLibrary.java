package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;

import com.example.mike.mp3player.service.library.utils.IsDirectoryFilter;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;
import com.example.mike.mp3player.service.library.utils.MusicFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ALBUM_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_DURATION;

public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;
    private FolderLibraryCollection folders;
    private SongCollection songs;
    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();
    private Context context;
    private List<MediaBrowserCompat.MediaItem> rootItems = new ArrayList<>();
    private final String LOG_TAG = "MEDIA_LIBRARY";

    public MediaLibrary(Context context)
    {
        this.context = context;
    }
    public void init() {
        songs = new SongCollection();
        rootItems.add(songs.getRoot());
        folders = new FolderLibraryCollection();
        rootItems.add(folders.getRoot());
        buildMediaLibrary();
    }

    public void buildMediaLibrary(){
        File externalStorageDirectory = MediaLibraryUtils.getExternalStorageDirectory();
        List<MediaBrowserCompat.MediaItem> songList = buildSongList(externalStorageDirectory);
        songs.index(songList);
        folders.index(songList);
    }


    private List<MediaBrowserCompat.MediaItem> buildSongList(File directory) {
        List<File> directories = new ArrayList<>();
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

        if (directory.list() != null) {
            for (String currentFileString : directory.list()) {

                if (isDirectoryFilter.accept(directory, currentFileString)) {
                    directories.add(new File(directory, currentFileString));
                } else if (musicFileFilter.accept(directory, currentFileString)) {

                    File fileToRetrieve = null;
                    try {
                        fileToRetrieve = new File(directory, currentFileString);
                        MediaBrowserCompat.MediaItem mediaItem = createPlayableMediaItemFromFile(fileToRetrieve, directory);
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

    public List<MediaBrowserCompat.MediaItem> getRoot() {
        return rootItems;
    }
    public List<MediaBrowserCompat.MediaItem> getSongList() {
        return songs.getSongs();
    }

    public List<MediaBrowserCompat.MediaItem> getChildren(String id) {
        if (id.equals(folders.getRootId())) {
            return getRoot();
        }
        return null;
    }

    private MediaBrowserCompat.MediaItem createPlayableMediaItemFromFile(File file, File directory) throws Exception {
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

        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        return mediaItem;
    }

    private class MediaItemComparator implements Comparator<MediaBrowserCompat.MediaItem> {
        @Override
        public int compare(MediaBrowserCompat.MediaItem o1, MediaBrowserCompat.MediaItem o2) {
            String s1 = o1.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
            String s2 = o2.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
            return s1.compareTo(s2);
        }
    }

    public Uri getMediaUriFromMediaId(String mediaId){
        for (MediaBrowserCompat.MediaItem i : songs.getSongs()) {
            if (i.getDescription().getMediaId().equals(mediaId)) {
                return i.getDescription().getMediaUri();
            }
        }
        return null;
     }
}
