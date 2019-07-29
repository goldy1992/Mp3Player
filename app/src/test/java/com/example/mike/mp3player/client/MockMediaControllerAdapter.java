package com.example.mike.mp3player.client;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.Set;

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
    public void registerMetaDataListener(MetaDataListener metaDataListener) {
        // DO NOTHING
    }
    @Override
    public void unregisterMetaDataListener(MetaDataListener metaDataListener) {
        // DO NOTHING
    }
    @Override
    public void registerPlaybackStateListener(PlaybackStateListener playbackStateListener, Set<ListenerType> listenerTypes) {
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
