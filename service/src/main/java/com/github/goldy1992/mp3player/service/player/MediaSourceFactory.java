package com.github.goldy1992.mp3player.service.player;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.github.goldy1992.mp3player.commons.LogTagger;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;

public class MediaSourceFactory implements LogTagger {

    private final FileDataSource fileDataSource;
    private final ContentDataSource contentDataSource;

    @Inject
    public MediaSourceFactory(FileDataSource fileDataSource,
                              ContentDataSource contentDataSource) {
        this.fileDataSource = fileDataSource;
        this.contentDataSource = contentDataSource;
    }

    public MediaSource createMediaSource(Uri uri) {
        DataSpec dataSpec = new DataSpec(uri);
        MyDataSourceFactory dataSrcFactory = openDataSpec(dataSpec);

        if (null == dataSrcFactory) {
            return null;
        }

        ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(dataSrcFactory);
        return factory.createMediaSource(uri);
    }

    private MyDataSourceFactory openDataSpec(DataSpec dataSpec) {
        final Uri dataSpecUri = dataSpec.uri;
        final String scheme = dataSpecUri.getScheme();
        try {
            if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                fileDataSource.open(dataSpec);
                return new MyDataSourceFactory(fileDataSource);
            } else {
                contentDataSource.open(dataSpec);
                return new MyDataSourceFactory(contentDataSource);
            }
        } catch (ContentDataSource.ContentDataSourceException |
                FileDataSource.FileDataSourceException ex) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }

    @Override
    public String getLogTag () {
        return "MDIA_SRC_FACTORY";
    }
}