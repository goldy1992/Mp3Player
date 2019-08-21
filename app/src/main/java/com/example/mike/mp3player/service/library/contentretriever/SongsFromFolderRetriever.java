package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mike.mp3player.commons.MediaItemType;

public class SongsFromFolderRetriever extends SongsRetriever {
    public SongsFromFolderRetriever(ContentResolver contentResolver, String typeId, String parentId) {
        super(contentResolver, typeId, parentId);
    }

    @Override
    public MediaItemType getParentType() {
        return MediaItemType.FOLDER;
    }

    @Override
    Cursor getResults(String id) {
        // TODO: implement
        String WHERE_CLAUSE = MediaStore.Audio.Media.DATA + " LIKE ? ";
        String[] WHERE_ARGS = {id + "%"};
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
                WHERE_CLAUSE , WHERE_ARGS, null);
    }
}
