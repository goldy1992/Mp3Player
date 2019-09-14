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
import com.example.mike.mp3player.service.library.search.Song;
import com.example.mike.mp3player.service.library.search.SongDao;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongsRetriever extends ContentResolverRetriever<Song> {

    public SongsRetriever(ContentResolver contentResolver,
                          ResultsParser resultsParser, SongDao songDao, Handler handler) {
        super(contentResolver, resultsParser, songDao, handler);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getProjection(),
                null, null, null);
    }


    @Override
    public String[] getProjection() {
        return SONG_PROJECTION.toArray(new String[0]);
    }

    @Override
    Song createFromMediaItem(@NonNull MediaItem item) {
        final String id = MediaItemUtils.getMediaId(item);
        final String value = MediaItemUtils.getTitle(item);
        return new Song(id, value);
    }

}
