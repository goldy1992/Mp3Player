package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.Nullable;

import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.provider.BaseColumns._ID;
import static com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION;

@Singleton
public class MediaItemFromIdRetriever {
    private static final String[] PROJECTION = SONG_PROJECTION.toArray(new String[0]);
    private final ContentResolver contentResolver;
    private final SongResultsParser songResultsParser;

    @Inject
    public MediaItemFromIdRetriever(ContentResolver contentResolver,
                                SongResultsParser resultsParser) {
        this.contentResolver = contentResolver;
        this.songResultsParser = resultsParser;
    }

    @Nullable
    public MediaBrowserCompat.MediaItem getItem(long id) {
        final String where = _ID + " = ?";
        final String[] whereArgs = {String.valueOf(id)};

        Cursor cursor =  contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                PROJECTION, where, whereArgs, null);

        if (null != cursor) {
            List<MediaBrowserCompat.MediaItem> results = songResultsParser.create(cursor, "");
            if (CollectionUtils.isNotEmpty(results)) {
                return results.get(0);
            }
        }
        return null;
    }
}
