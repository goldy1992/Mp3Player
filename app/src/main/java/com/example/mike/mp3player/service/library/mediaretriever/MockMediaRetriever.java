package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.service.library.utils.IsDirectoryFilter;
import com.example.mike.mp3player.service.library.utils.MusicFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MockMediaRetriever extends MediaRetriever {

    private static final String TEST_DATA_DIR = "/sdcard/test-data";
    private static final String LOG_TAG = "MCK_MDIA_RTVR";
    private MusicFileFilter musicFileFilter = new MusicFileFilter();
    private IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();

    public MockMediaRetriever(Context context) {
        super(context);
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> retrieveMedia() {
        File file = new File(TEST_DATA_DIR);
        return buildSongList(file);
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
}