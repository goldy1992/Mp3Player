package com.github.goldy1992.mp3player.service.library.search.managers;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.Folder;
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class FolderDatabaseManager extends SearchDatabaseManager<Folder> {

    @Inject
    public FolderDatabaseManager(ContentManager contentManager,
                               @Named("worker") Handler handler,
                               MediaItemTypeIds mediaItemTypeIds,
                               SearchDatabase searchDatabase) {
        super(contentManager, handler, searchDatabase.folderDao(), mediaItemTypeIds.getId(MediaItemType.FOLDERS));
    }

    @Override
    Folder createFromMediaItem(@NonNull MediaBrowserCompat.MediaItem item) {
        final String id = MediaItemUtils.getDirectoryPath(item);
        final String value = MediaItemUtils.getDirectoryName(item);
        return new Folder(id, value);
    }
}