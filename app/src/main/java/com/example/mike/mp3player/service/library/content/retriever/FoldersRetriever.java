package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.Folder;
import com.example.mike.mp3player.service.library.search.FolderDao;

import static com.example.mike.mp3player.service.library.content.Projections.FOLDER_PROJECTION;

public class FoldersRetriever extends ContentResolverRetriever<Folder> {

    public FoldersRetriever(ContentResolver contentResolver, ResultsParser resultsParser,
                            FolderDao folderDao, Handler handler) {
        super(contentResolver, resultsParser, folderDao, handler);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }


    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
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
}
