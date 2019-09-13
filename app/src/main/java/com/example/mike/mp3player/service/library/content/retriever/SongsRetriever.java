package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.Song;
import com.example.mike.mp3player.service.library.search.SongDao;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongsRetriever extends ContentResolverRetriever {

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
    void updateSearchDatabase(List<MediaItem> results) {
        handler.post(() -> {
            final int resultsSize = results.size();
            final int count = dao.getCount();

            if (count != resultsSize) { // INSERT NORMALISED VALUES
                List<Song> songs = new ArrayList<>();
                for (MediaItem mediaItem : results) {
                    Song song = new Song(MediaItemUtils.getMediaId(mediaItem));
                    song.setValue(StringUtils.stripAccents(MediaItemUtils.getTitle(mediaItem)));
                    songs.add(song);
                }
                dao.insertAll(songs);
            }
        });
    }


    @Override
    public String[] getProjection() {
        return SONG_PROJECTION.toArray(new String[0]);
    }
}
