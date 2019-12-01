package com.github.goldy1992.mp3player.service.library.content.parser;

import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemType;

import java.util.List;

public class SongsFromFolderResultsParser extends SongResultsParser {
    @Override
    public List<MediaBrowserCompat.MediaItem> create(@NonNull Cursor cursor, String mediaIdPrefix) {
        return null;
    }

    @Override
    public MediaItemType getType() {
        return null;
    }

    @Override
    public int compare(MediaBrowserCompat.MediaItem o1, MediaBrowserCompat.MediaItem o2) {
        return 0;
    }
}
