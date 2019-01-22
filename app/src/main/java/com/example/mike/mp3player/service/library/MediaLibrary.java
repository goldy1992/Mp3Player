package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.MetaDataKeys;
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
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ALBUM_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_DURATION;

public class MediaLibrary {
    private boolean playlistRecursInSubDirectory = false;
    private List<MediaBrowserCompat.MediaItem> library;

    private Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> folders;
    private Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> foldersAndSubFolders;
    private Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> albums;
    private Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> artists;
    private Map<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>> genre;
    private List<MediaBrowserCompat.MediaItem> songs;

    MediaBrowserCompat.MediaItem FOLDERS;
    MediaBrowserCompat.MediaItem SONGS;

    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();
    private Context context;
    private final String LOG_TAG = "MEDIA_LIBRARY";

    public MediaLibrary(Context context)
    {
        this.context = context;
    }
    public void init() {
        library = new ArrayList<>();
        songs = new ArrayList<>();
        folders = new HashMap<>();
        MediaDescriptionCompat foldersDescription = new MediaDescriptionCompat.Builder()
                .setDescription("A list of all folders with music inside them")
                .setTitle("Folders")
                .setMediaId(String.valueOf(MetaDataKeys.FOLDERS.hashCode()))
                .build();
        FOLDERS = new MediaBrowserCompat.MediaItem(foldersDescription, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);

        MediaDescriptionCompat songsDescription = new MediaDescriptionCompat.Builder()
                .setDescription("A list of all songs in the library")
                .setTitle("Songs")
                .setMediaId(String.valueOf(MetaDataKeys.SONGS.hashCode()))
                .build();
        SONGS = new MediaBrowserCompat.MediaItem(songsDescription, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);

        library.add(SONGS);
        library.add(FOLDERS);

        buildMediaLibrary();
    }

    public void buildMediaLibrary(){
        File externalStorageDirectory = MediaLibraryUtils.getExternalStorageDirectory();
        buildDirectoryPlaylist(externalStorageDirectory);
        Collections.sort(getSongs(), new MediaItemComparator());
    }

    private void buildDirectoryPlaylist(File directory)
    {   String directoryName = directory.getName();
        MediaBrowserCompat.MediaItem currentDirectoryMediaItem = MediaLibraryUtils.getMediaItemFolderFromMap(directoryName, folders.keySet());

        if (musicFileFilter.accept(directory, null)) {
            if (null == currentDirectoryMediaItem) {
                Uri uri = Uri.fromFile(directory);
                MediaDescriptionCompat descr = new MediaDescriptionCompat.Builder()
                        .setTitle(directoryName)
                        .setMediaUri(uri)
                        .setMediaId(String.valueOf(uri.getPath().hashCode()))
                        .build();
                currentDirectoryMediaItem = new MediaBrowserCompat.MediaItem(descr, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
                folders.put(currentDirectoryMediaItem, new ArrayList<>());
            }
        }

        List<File> directories = new ArrayList<>();
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

        if (directory.list() != null) {
            for (String currentFileString : directory.list()) {

                if (isDirectoryFilter.accept(directory, currentFileString)) {
                    directories.add(new File(directory, currentFileString));
                } else if (musicFileFilter.accept(directory, currentFileString)) {

                    List directoryMediaList = folders.get(currentDirectoryMediaItem);
                    File fileToRetrieve = null;
                    try {
                        fileToRetrieve = new File(directory, currentFileString);
                        MediaBrowserCompat.MediaItem mediaItem = createPlayableMediaItemFromFile(fileToRetrieve);
                        mediaItems.add(mediaItem);
                        directoryMediaList.add(mediaItem);
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

        getSongs().addAll(mediaItems);
    }

    public List<MediaBrowserCompat.MediaItem> getLibrary() {
        return library;
    }

    private MediaBrowserCompat.MediaItem createPlayableMediaItemFromFile(File file) throws Exception {
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
                .setMediaUri(uri)
                .setTitle(MediaLibraryUtils.getSongTitle(mmr, fileName))
                .setExtras(extras);

        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
        return mediaItem;
    }

    public List<MediaBrowserCompat.MediaItem> getSongs() {
        return songs;
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
        for (MediaBrowserCompat.MediaItem i : getSongs()) {
            if (i.getDescription().getMediaId().equals(mediaId)) {
                return i.getDescription().getMediaUri();
            }
        }
        return null;
     }
}
