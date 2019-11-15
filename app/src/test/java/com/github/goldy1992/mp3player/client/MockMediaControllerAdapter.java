package com.github.goldy1992.mp3player.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback;
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener;
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE;

public class MockMediaControllerAdapter extends MediaControllerAdapter {


    @Inject
    public MockMediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        super(context, myMediaControllerCallback);
    }

    @Override
    public void setMediaToken(MediaSessionCompat.Token token) {
        // DO NOTHING
    }

    @Override
    public void prepareFromMediaId(String mediaId, Bundle extras) {
        // DO NOTHING
    }

    @Override
    public void playFromMediaId(String mediaId, Bundle extras) {
        // DO NOTHING
    }

    @Override
    public void playFromUri(Uri uri, Bundle extras) {
        // DO NOTHING
    }

    @Override
    public void play() {
        // DO NOTHING
    }
    @Override
    public void setRepeatMode(@PlaybackStateCompat.RepeatMode int repeatMode) {
        // DO NOTHING
    }
    @Override
    public void pause() {
        // DO NOTHING
    }
    @Override
    public void seekTo(long position) {
        // DO NOTHING
    }
    @Override
    public void stop() {
        // DO NOTHING
    }
    @Override
    public void skipToNext() {
        // DO NOTHING
    }
    @Override
    public void skipToPrevious() {
        // DO NOTHING
    }
    @Override
    public void setShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) {
        // DO NOTHING
    }
    @Override
    public void registerMetaDataListener(MetadataListener metaDataListener) {
        // DO NOTHING
    }
    @Override
    public void unregisterMetaDataListener(MetadataListener metaDataListener) {
        // DO NOTHING
    }
    @Override
    public void registerPlaybackStateListener(PlaybackStateListener playbackStateListener) {
        // DO NOTHING
    }

    @Override
    public int getPlaybackState() {
        return PlaybackStateCompat.STATE_NONE;
    }

    @Override
    public PlaybackStateCompat getPlaybackStateCompat() {
        return new PlaybackStateCompat.Builder().setState(getPlaybackState(), 0L, 0F).build();
    }
    @Override
    public MediaMetadataCompat getMetadata() {
        return new MediaMetadataCompat.Builder()
            .putString(METADATA_KEY_ARTIST, "artist")
            .putString(METADATA_KEY_TITLE, "title")
            .build();
    }

    @Override
    public List<MediaSessionCompat.QueueItem> getQueue() {
        return new ArrayList<>();
    }

    @Override
    public long getActiveQueueItemId() {
        return 0L;
    }

    public @PlaybackStateCompat.ShuffleMode int getShuffleMode() {
        return PlaybackStateCompat.SHUFFLE_MODE_ALL;
    }

    public @PlaybackStateCompat.RepeatMode int getRepeatMode() {
        return PlaybackStateCompat.REPEAT_MODE_ALL;
    }

    public MediaSessionCompat.Token getToken() {
        return null;
    }

    public void disconnect() {
        // DO NOTHING
    }

    public boolean isInitialized() {
        return true;
    }


    public void sendCustomAction(String customAction, Bundle args) {
        // DO NOTHING
    }
}