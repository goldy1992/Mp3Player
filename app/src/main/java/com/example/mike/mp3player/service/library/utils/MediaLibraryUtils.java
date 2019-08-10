package com.example.mike.mp3player.service.library.utils;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaSessionCompat;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;

public final class MediaLibraryUtils {
    private static final String LOG_TAG = "MDIA_LBRY_UTLS";
    private static final String STORAGE_DIR = "/storage/";

    public static String appendToPath(String path, String toAppend) {
        return path + File.separator + toAppend;
    }

    public static File appendToFilePath(File file, String toAppend) {
        return new File(file.getPath() +  File.separator + toAppend);
    }

    public static File getRootDirectory(){
        return Environment.getRootDirectory();
    }

    public static File getExternalStorageDirectory(){
        return new File(STORAGE_DIR);
    }

    public static File getInternalStorageDirectory() { return Environment.getExternalStorageDirectory(); }

    /**
     * This should make a list of pre-ordered QueueItems since the parameter is a tree set, which is
     * orered by definition.
     * @param mediaItems
     * @return
     */
    public static List<MediaSessionCompat.QueueItem> convertMediaItemsToQueueItem(List<MediaItem> mediaItems) {
        List<MediaSessionCompat.QueueItem> queueItemList = new ArrayList<>();
        if (mediaItems != null) {
            for (MediaItem mediaItem : mediaItems) {
                queueItemList.add(
                        new MediaSessionCompat.QueueItem(mediaItem.getDescription(), Long.parseLong(mediaItem.getMediaId()))
                );
            }
        }
        return queueItemList;
    }

    public static Integer findIndexOfTrackInPlaylist(List<MediaBrowserCompat.MediaItem> queue, String mediaId) {
        if (queue == null|| queue.isEmpty() || mediaId == null) {
            return null;
        }

        for (int currentIndex = 0; currentIndex < queue.size(); currentIndex++) {
            MediaBrowserCompat.MediaItem queueItem = queue.get(currentIndex);
            if (queueItem == null || queueItem.getDescription() == null) {
                continue;
            }
            if (queue.get(currentIndex).getDescription().getMediaId().equals(mediaId)) {
                return currentIndex;
            }
        }

        return null;
    }

    public static String getSongTitle(MediaMetadataRetriever mmr, String fileName) {
        if (mmr != null) {
            String title = mmr.extractMetadata(METADATA_KEY_TITLE);
            if (StringUtils.isBlank(title)) {
                return fileName.trim();
            }
            return title;
        }
        return fileName.trim();
    }

    public static MediaItem getMediaItemFolderFromMap(String directoryName,
                                           Set<MediaItem> mediaItemSet) {
        for (MediaItem i : mediaItemSet) {
            if (i.getDescription().getTitle().equals(directoryName)) {
                return i;
            }
        }
        return null;

    }

}
