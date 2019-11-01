package com.example.mike.mp3player.service.player;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.MyControlDispatcher;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.library.ContentManager;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.ID_DELIMITER;

public class MyPlaybackPreparer implements MediaSessionConnector.PlaybackPreparer {

    private static final String LOG_TAG = "PLAYBACK_PREPARER";

    private final ContentManager contentManager;
    private final ExoPlayer exoPlayer;
    private final MyControlDispatcher myControlDispatcher;
    private final FileDataSource fileDataSource;
    private final ContentDataSource contentDataSource;
    private final PlaybackManager playbackManager;

    public MyPlaybackPreparer(ExoPlayer exoPlayer,
                              ContentManager contentManager,
                              List<MediaItem> items,
                              FileDataSource fileDataSource,
                              ContentDataSource contentDataSource,
                              MyControlDispatcher myControlDispatcher,
                              PlaybackManager playbackManager) {
        this.exoPlayer = exoPlayer;
        this.contentManager = contentManager;
        this.myControlDispatcher = myControlDispatcher;
        this.fileDataSource = fileDataSource;
        this.contentDataSource = contentDataSource;
        this.playbackManager = playbackManager;
        if (CollectionUtils.isNotEmpty(items)) {
            String trackId = MediaItemUtils.getMediaId(items.get(0));
            preparePlaylist(false, trackId, items);
        }
    }
    @Override
    public long getSupportedPrepareActions() {
        return MediaSessionConnector.PlaybackPreparer.ACTIONS;
    }

    @Override
    public void onPrepare(boolean playWhenReady) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onPrepareFromMediaId(String mediaId, boolean playWhenReady, Bundle extras) {
        String trackId = extractTrackId(mediaId);
        if (null != trackId) {
            List<MediaItem> results = contentManager.getPlaylist(mediaId);
            this.playbackManager.createNewPlaylist(results);
            preparePlaylist(playWhenReady, trackId, results);
        }
    }

    private void preparePlaylist(boolean playWhenReady, String trackId, List<MediaItem> results) {
        if (null != results) {
            ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
            int uriToPlayIndex = 0;
            for (int i = 0; i < results.size(); i++) {
                MediaItem currentMediaItem = results.get(i);
                String id = MediaItemUtils.getMediaId(currentMediaItem);
                Uri currentUri = MediaItemUtils.getMediaUri(currentMediaItem);

                if (id != null && id.equals(trackId)) {
                    uriToPlayIndex = i;
                } // if
                DataSpec dataSpec = new DataSpec(currentUri);
                MyDataSourceFactory dataSrcFactory = null;
                try {
                    if (ContentResolver.SCHEME_FILE.equals(currentUri.getScheme())) {
                        fileDataSource.open(dataSpec);
                        dataSrcFactory = new MyDataSourceFactory(fileDataSource);
                    } else {
                        contentDataSource.open(dataSpec);
                        dataSrcFactory = new MyDataSourceFactory(contentDataSource);
                    }
                    ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(dataSrcFactory);
                    MediaSource src = factory.createMediaSource(currentUri);
                    concatenatingMediaSource.addMediaSource(src);
                } catch (FileDataSource.FileDataSourceException | ContentDataSource.ContentDataSourceException  ex) {
                    Log.e(LOG_TAG, "error adding song to playlist");
                }
            } // for
            this.exoPlayer.prepare(concatenatingMediaSource);
            this.myControlDispatcher.dispatchSeekTo(exoPlayer, uriToPlayIndex, 0L);
            this.myControlDispatcher.dispatchSetPlayWhenReady(exoPlayer, playWhenReady);
        } // if
    }

    @Override
    public void onPrepareFromSearch(String query, boolean playWhenReady, Bundle extras) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onPrepareFromUri(Uri uri, boolean playWhenReady, Bundle extras) {

        MediaItem result = contentManager.getItem(uri);
        List<MediaItem> playlist = new ArrayList<>();
        playlist.add(result);
        this.playbackManager.createNewPlaylist(playlist);

        preparePlaylist(playWhenReady, MediaItemUtils.getMediaId(result), playlist);

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
