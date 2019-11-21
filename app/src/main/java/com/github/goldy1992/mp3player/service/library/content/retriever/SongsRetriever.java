package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;
import com.github.goldy1992.mp3player.service.library.search.Song;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION;

@Singleton
public class SongsRetriever extends ContentResolverRetriever<Song> {

    @Inject
    public SongsRetriever(ContentResolver contentResolver,
                          SongResultsParser resultsParser,
                          SongDao songDao,
                          @Named("worker") Handler handler) {
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

    @Override
    boolean isSearchable() {
        return true;
    }

}
