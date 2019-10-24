package com.example.mike.mp3player.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.metadata.MetadataListener;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

import javax.inject.Inject;

public class MediaControllerAdapter {

    private static final String LOG_TAG = "MDIA_CNTRLLR_ADPTR";
    private MediaControllerCompat mediaController;
    private MyMediaControllerCallback myMediaControllerCallback;
    private MediaSessionCompat.Token token = null;
    private final Context context;

    @Inject
    public MediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        this.context = context;
        this.myMediaControllerCallback = myMediaControllerCallback;
    }

    public void setMediaToken(@NonNull MediaSessionCompat.Token token) {
        if (!isInitialized()) {
            init(token);
        } else {
            Log.e(LOG_TAG, "MediaControllerAdapter already initialised");
        }
    }

    @VisibleForTesting
    public void init(@NonNull MediaSessionCompat.Token token) {
        try {
            this.mediaController = new MediaControllerCompat(context, token);
            this.mediaController.registerCallback(myMediaControllerCallback);
        } catch (RemoteException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
        }
        this.token = token;
    }

    public void prepareFromMediaId(String mediaId, Bundle extras) {
        getController().prepareFromMediaId(mediaId, extras);
    }

    public void playFromMediaId(String mediaId, Bundle extras) {
        getController().playFromMediaId(mediaId, extras);
    }

    public void play() {
        getController().play();
    }

    public void setRepeatMode(@PlaybackStateCompat.RepeatMode int repeatMode) {
        getController().setRepeatMode(repeatMode);
    }

    public void pause() {
        //Log.i(LOG_TAG, "pause hit");
        getController().pause();
    }

    public void seekTo(long position) {
        getController().seekTo(position);
    }

    public void stop() {
        getController().stop();
    }

    public void skipToNext() {
        getController().skipToNext();
    }

    public void skipToPrevious() {
        getController().skipToPrevious();
    }

    public void setShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) {
        getController().setShuffleMode(shuffleMode);
    }

    public void registerMetaDataListener(MetadataListener metaDataListener) {
        myMediaControllerCallback.getMyMetaDataCallback().registerMetaDataListener(metaDataListener);
    }

    public void unregisterMetaDataListener(MetadataListener metaDataListener) {
        myMediaControllerCallback.getMyMetaDataCallback().removeMetaDataListener(metaDataListener);
    }

    public void registerPlaybackStateListener(PlaybackStateListener playbackStateListener) {
        myMediaControllerCallback.getMyPlaybackStateCallback().registerPlaybackStateListener(playbackStateListener);
    }

    public void unregisterPlaybackStateListener(PlaybackStateListener playbackStateListener) {
        myMediaControllerCallback.getMyPlaybackStateCallback().removePlaybackStateListener(playbackStateListener);
    }

    public int getPlaybackState() {
        PlaybackStateCompat playbackStateCompat = getPlaybackStateCompat();
        return null == playbackStateCompat ? 0 : playbackStateCompat.getState();
    }

    public PlaybackStateCompat getPlaybackStateCompat() {
        if (mediaController != null) {
            return mediaController.getPlaybackState();
        }
        return null;
    }

    public MediaMetadataCompat getMetadata() {
        if (mediaController != null) {
            return mediaController.getMetadata();
        }
        return null;
    }

    public @PlaybackStateCompat.ShuffleMode int getShuffleMode() {
        return mediaController.getShuffleMode();
    }

    public @PlaybackStateCompat.RepeatMode int getRepeatMode() {
        return mediaController.getRepeatMode();
    }

    @Nullable
    public Uri getCurrentSongAlbumArtUri() {
        MediaMetadataCompat currentMetaData = getMetadata();
        String albumArtUriPath = currentMetaData.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
        if (albumArtUriPath != null) {
            Uri albumArtUri = null;
            try {
                 albumArtUri = Uri.parse(albumArtUriPath);
            } catch (NullPointerException ex) {
                Log.e(LOG_TAG, albumArtUriPath + ": is an invalid Uri");
                return null;
            }
            return  albumArtUri;
        }
        return null;
    }

    public MediaSessionCompat.Token getToken() {
        return token;
    }

    public void disconnect() {
        if (mediaController != null && myMediaControllerCallback != null) {
            mediaController.unregisterCallback(myMediaControllerCallback);
        }
    }

    public boolean isInitialized() {
        return mediaController != null && mediaController.isSessionReady();
    }


    public void sendCustomAction(String customAction, Bundle args) {
        getController().sendCustomAction(customAction, args);
    }

    @VisibleForTesting
    public MediaControllerCompat.TransportControls getController() {
        return mediaController.getTransportControls();
    }

    public List<MediaSessionCompat.QueueItem> getQueue() {
        return mediaController.getQueue();
    }

    @VisibleForTesting
    public MediaControllerCompat getMediaController() {
        return mediaController;
    }

    @VisibleForTesting
    public void setMediaController(MediaControllerCompat mediaController) {
        this.mediaController = mediaController;
    }
}
