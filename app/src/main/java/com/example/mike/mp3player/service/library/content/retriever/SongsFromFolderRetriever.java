package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.SearchDao;
import com.example.mike.mp3player.service.library.search.Song;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongsFromFolderRetriever extends ContentResolverRetriever<Song> {
    public SongsFromFolderRetriever(ContentResolver contentResolver, ResultsParser resultsParser, SearchDao<Song> dao, Handler handler) {
        super(contentResolver, resultsParser, dao, handler);
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
        return SONG_PROJECTION.toArray(new String[0]);
    }

    @Override
    Song createFromMediaItem(@NonNull MediaBrowserCompat.MediaItem item) {
        return null;
    }

    @Override
    boolean isSearchable() {
        return false;
    }
}
