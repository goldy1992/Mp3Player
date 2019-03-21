package com.example.mike.mp3player.service.library.utils;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Range;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;

public final class MediaLibraryUtils {
    private static final String LOG_TAG = "MDIA_LBRY_UTLS";
    private static final String STORAGE_DIR = "/storage/";

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
        for (MediaItem  mediaItem : mediaItems) {
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
    /**
     *
     */
    public static Set<MediaItem> getSubSetFromRange(Set<MediaItem> set, Range<Integer> range) {

        if (range == null) {
            return set;
        }

        if (!validRange(set, range)) {
            return null;
        }
        List<MediaItem> l = new ArrayList<>(set);
       l.subList(range.getLower(), range.getUpper() + 1);

       TreeSet<MediaItem> ts = new TreeSet<>();
       ts.addAll(l);
        return ts;
    }

    /**
     *
     */
    public static boolean validRange(Set<MediaItem> set, Range<Integer> range) {
        if (set == null || range == null) {
            return false;
        }
        return  range.getLower() >= 0 && range.getUpper() < set.size();
    }
}
