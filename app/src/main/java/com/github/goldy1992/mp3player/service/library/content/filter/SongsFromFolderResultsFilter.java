package com.github.goldy1992.mp3player.service.library.content.filter;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

public class SongsFromFolderResultsFilter implements ResultsFilter {


    @Inject
    public SongsFromFolderResultsFilter() {}


    @Override
    public List<MediaItem> filter(@NonNull String query,
                                  @NonNull List<MediaItem> results) {
        File queryPath = new File(query);
        Iterator<MediaItem> iterator = results.listIterator();

        while(iterator.hasNext()) {
            MediaItem currentItem = iterator.next();
            String directoryPath = MediaItemUtils.getDirectoryPath(currentItem);
            if (directoryPath == null || !directoryPath.equalsIgnoreCase(queryPath.getAbsolutePath().toUpperCase())) {
                iterator.remove();
            }
        }
        return results;
    }
}
