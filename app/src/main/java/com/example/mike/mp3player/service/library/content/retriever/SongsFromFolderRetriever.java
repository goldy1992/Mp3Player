package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.builder.MediaItemBuilder;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongsFromFolderRetriever extends ContentResolverRetriever {
    public SongsFromFolderRetriever(ContentResolver contentResolver, MediaItemBuilder mediaItemBuilder) {
        super(contentResolver, mediaItemBuilder);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    @Override
    Cursor performGetChildrenQuery(String id) {
        String WHERE_CLAUSE = MediaStore.Audio.Media.DATA + " LIKE ? ";
        String[] WHERE_ARGS = {id + "%"};
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,getProjection(),
                WHERE_CLAUSE , WHERE_ARGS, null);
    }

    @Override
    String[] getProjection() {
        return SONG_PROJECTION;
    }

}
