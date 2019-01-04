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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ALBUM_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_DURATION;

public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;
    private List<MediaBrowserCompat.MediaItem> library;
    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();
    private Context context;
    private final String LOG_TAG = "MEDIA_LIBRARY";
    private Map<String, Uri> mediaIdToUriMap;

    public MediaLibrary(Context context)
    {
        this.context = context;
    }
    public void init() {
        library = new ArrayList<>();
        mediaIdToUriMap = new HashMap<>();
        buildMediaLibrary();
    }

    public void buildMediaLibrary(){
        File externalStorageDirectory = MediaLibraryUtils.getExternalStorageDirectory();
        buildDirectoryPlaylist(externalStorageDirectory);
        Collections.sort(library, new MediaItemComparator());
    }

    private void buildDirectoryPlaylist(File directory)
    {
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
                        MediaBrowserCompat.MediaItem mediaItem = createMediaItemFromFile(fileToRetrieve);
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
                buildDirectoryPlaylist(directoryToBrowse);
            }
        }

        library.addAll(mediaItems);
    }

    public List<MediaBrowserCompat.MediaItem> getLibrary() {
        return library;
    }

    private MediaBrowserCompat.MediaItem createMediaItemFromFile(File file) throws Exception {
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

        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setTitle(MediaLibraryUtils.getSongTitle(mmr, fileName))
                .setExtras(extras);

        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        mediaIdToUriMap.put(mediaId, uri);
        return mediaItem;
    }

    public Uri getMediaUri(String id) {
        return mediaIdToUriMap.get(id);
    }

    private class MediaItemComparator implements Comparator<MediaBrowserCompat.MediaItem> {
        @Override
        public int compare(MediaBrowserCompat.MediaItem o1, MediaBrowserCompat.MediaItem o2) {
            String s1 = o1.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
            String s2 = o2.getDescription().getTitle().toString().toUpperCase(Locale.getDefault());
            return s1.compareTo(s2);
        }
    }
}
