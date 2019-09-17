package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.library.ContentManager;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;

import java.util.Arrays;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.ID_DELIMITER;

public class MyPlaybackPreparer implements MediaSessionConnector.PlaybackPreparer {

    private static final String LOG_TAG = "PLAYBACK_PREPARER";

    private final ContentManager contentManager;
    private final Context context;
    private final ExoPlayer exoPlayer;

    public MyPlaybackPreparer(Context context, ExoPlayer exoPlayer, ContentManager contentManager) {
        this.context = context;
        this.exoPlayer = exoPlayer;
        this.contentManager = contentManager;
    }

    @Override
    public long getSupportedPrepareActions() {
        return MediaSessionConnector.PlaybackPreparer.ACTIONS;
    }

    @Override
    public void onPrepare(boolean playWhenReady) {

    }

    @Override
    public void onPrepareFromMediaId(String mediaId, boolean playWhenReady, Bundle extras) {
        String trackId = extractTrackId(mediaId);
        if (null != trackId) {
            List<MediaBrowserCompat.MediaItem> results = contentManager.getPlaylist(mediaId);
            if (null != results) {
                ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
                int uriToPlayIndex = 0;
                for (int i = 0; i < results.size(); i++) {
                    MediaBrowserCompat.MediaItem currentMediaItem = results.get(i);
                    String id = MediaItemUtils.getMediaId(currentMediaItem);
                    Uri currentUri = MediaItemUtils.getMediaUri(currentMediaItem);

                    if (id != null && id.equals(trackId)) {
                        uriToPlayIndex = i;
                    } // if
                    DataSpec dataSpec = new DataSpec(currentUri);
                    FileDataSource fileDataSource = new FileDataSource();
                    try {
                        fileDataSource.open(dataSpec);

                        MyDataSourceFactory dataSrcFactory = new MyDataSourceFactory(fileDataSource);
                        ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(dataSrcFactory);
                        MediaSource src = factory.createMediaSource(currentUri);
                        concatenatingMediaSource.addMediaSource(src);
                    } catch (FileDataSource.FileDataSourceException ex) {
                        Log.e(LOG_TAG, "error adding song to playlist");
                    }
                } // for
                this.exoPlayer.prepare(concatenatingMediaSource);
                this.exoPlayer.seekTo(uriToPlayIndex, 0L);
                this.exoPlayer.setPlayWhenReady(playWhenReady);
            } // for
        }
    }

    @Override
    public void onPrepareFromSearch(String query, boolean playWhenReady, Bundle extras) {

    }

    @Override
    public void onPrepareFromUri(Uri uri, boolean playWhenReady, Bundle extras) {

    }

    @Override
    public boolean onCommand(Player player, ControlDispatcher controlDispatcher, String command, Bundle extras, ResultReceiver cb) {
        return false;
    }

    private String extractTrackId(String mediaId) {
        if (null != mediaId) {
            List<String> splitId = Arrays.asList(mediaId.split(ID_DELIMITER));
            if (!splitId.isEmpty()) {
                return splitId.get(splitId.size() - 1);
            }
        }
        else {
            Log.e(LOG_TAG, "received null mediaId");
        }
        return null;
    }
}
