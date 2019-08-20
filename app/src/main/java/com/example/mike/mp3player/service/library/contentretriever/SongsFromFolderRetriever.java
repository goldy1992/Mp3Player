package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

public class SongsFromFolderRetriever extends SongsRetriever {
    public SongsFromFolderRetriever(ContentResolver contentResolver, String typeId) {
        super(contentResolver, typeId);
    }

    @Override
    Cursor getResults(String id) {
        // TODO: implement
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
                null, null, null);
    }
}
