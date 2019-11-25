package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION;

@Singleton
public class SongsRetriever extends ContentResolverRetriever {

    @Inject
    public SongsRetriever(ContentResolver contentResolver,
                          SongResultsParser resultsParser,
                          @Named("worker") Handler handler) {
        super(contentResolver, resultsParser, handler);
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

}
