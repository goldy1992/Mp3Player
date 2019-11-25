package com.github.goldy1992.mp3player.service.library.search.managers;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase;
import com.github.goldy1992.mp3player.service.library.search.Song;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class SongDatabaseManager extends SearchDatabaseManager<Song> {

    @Inject
    public SongDatabaseManager(ContentManager contentManager,
                               @Named("worker") Handler handler,
                               MediaItemTypeIds mediaItemTypeIds,
                               SearchDatabase searchDatabase) {
        super(contentManager, handler, searchDatabase.songDao(), mediaItemTypeIds.getId(MediaItemType.SONGS));
    }

    @Override
    Song createFromMediaItem(@NonNull MediaBrowserCompat.MediaItem item) {
        final String id = MediaItemUtils.getMediaId(item);
        final String value = MediaItemUtils.getTitle(item);
        return new Song(id, value);
    }
}
