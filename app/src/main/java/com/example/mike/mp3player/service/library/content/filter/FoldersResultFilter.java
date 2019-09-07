package com.example.mike.mp3player.service.library.content.filter;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;

public class FoldersResultFilter extends ResultsFilter {

    @Override
    public List<MediaItem> filter(@NonNull String query, @NonNull List<MediaItem> results) {
        Iterator<MediaItem> iterator = results.listIterator();

        while(iterator.hasNext()) {
            MediaItem currentItem = iterator.next();
            String directoryName = MediaItemUtils.getDirectoryName(currentItem);
            if (null != directoryName && !StringUtils.contains(directoryName.toUpperCase(), query.toUpperCase())) {
                 iterator.remove();
            }
        }
        return results;
    }
}
