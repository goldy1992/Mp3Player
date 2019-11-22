package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser;
import com.github.goldy1992.mp3player.service.library.search.Folder;
import com.github.goldy1992.mp3player.service.library.search.FolderDao;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.github.goldy1992.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

@Singleton
public class FoldersRetriever extends ContentResolverRetriever<Folder> {

    @Inject
    public FoldersRetriever(ContentResolver contentResolver,
                            FolderResultsParser resultsParser,
                            FolderDao folderDao,
                            @Named("worker") Handler handler) {
        super(contentResolver, resultsParser, folderDao, handler);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }


    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
                null, null, null);
    }


    @Override
    public String[] getProjection() {
        return FOLDER_PROJECTION.toArray(new String[0]);
    }

    @Override
    Folder createFromMediaItem(@NonNull MediaItem item) {
        final String id = MediaItemUtils.getDirectoryPath(item);
        final String value = MediaItemUtils.getDirectoryName(item);
        return new Folder(id, value);
    }

    @Override
    boolean isSearchable() {
        return true;
    }
}
