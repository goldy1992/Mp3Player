package com.example.mike.mp3player.service.library.utils;

import android.os.Environment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class MediaLibraryUtils {

    public static String appendToPath(String path, String toAppend) {
        return path + File.separator + toAppend;
    }

    public static File appendToFilePath(File file, String toAPpend) {
        return new File(file.getPath() +  File.separator + toAPpend);
    }

    public static File getRootDirectory(){
        return Environment.getRootDirectory();
    }

    public static File getExternalStorageDirectory(){
        return Environment.getExternalStorageDirectory();
    }

    public static List<MediaSessionCompat.QueueItem> convertMediaItemsToQueueItem(List<MediaBrowserCompat.MediaItem> mediaItems) {
        List<MediaSessionCompat.QueueItem> queueItemList = new ArrayList<>();
        for (MediaBrowserCompat.MediaItem  mediaItem : mediaItems) {
            queueItemList.add(
                    new MediaSessionCompat.QueueItem( mediaItem.getDescription(), Long.parseLong(mediaItem.getMediaId() ) )
            );
        }
        return queueItemList;
    }

    public static Integer findIndexOfTrackInPlaylist(List<MediaSessionCompat.QueueItem> queue, String mediaId) {
        if (queue == null|| queue.isEmpty() || mediaId == null) {
            return null;
        }

        for (int currentIndex = 0; currentIndex < queue.size(); currentIndex++) {
            MediaSessionCompat.QueueItem queueItem = queue.get(currentIndex);
            if (queueItem == null || queueItem.getDescription() == null) {
                break;
            }
            if (queue.get(currentIndex).getDescription().getMediaId().equals(mediaId)) {
                return currentIndex;
            }
        }

        return null;
    }
}
