package com.example.mike.mp3player.client;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import java.util.Set;

import javax.inject.Inject;

public class MockMediaControllerAdapter extends MediaControllerAdapter {


    @Inject
    public MockMediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        super(context, myMediaControllerCallback);
    }

    @Override
    public void setMediaToken(MediaSessionCompat.Token token) {
        // DO NOTHING
    }



    public void prepareFromMediaId(String mediaId, Bundle extras) {
        // DO NOTHING
    }

    public void play() {
        // DO NOTHING
    }

    public void setRepeatMode(@PlaybackStateCompat.RepeatMode int repeatMode) {
        // DO NOTHING
    }

    public void pause() {
        // DO NOTHING
    }

    public void seekTo(long position) {
        // DO NOTHING
    }

    public void stop() {
        // DO NOTHING
    }

    public void skipToNext() {
        // DO NOTHING
    }

    public void skipToPrevious() {
        // DO NOTHING
    }

    public void setShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) {
        // DO NOTHING
    }

    public void registerMetaDataListener(MetaDataListener metaDataListener) {
        // DO NOTHING
    }

    public void unregisterMetaDataListener(MetaDataListener metaDataListener) {
        // DO NOTHING
    }

    public void registerPlaybackStateListener(PlaybackStateListener playbackStateListener, Set<ListenerType> listenerTypes) {
        // DO NOTHING
    }

    public void unregisterPlaybackStateListener(PlaybackStateListener playbackStateListener, ListenerType listenerType) {
        // DO NOTHING
    }
    public int getPlaybackState() {
        return PlaybackStateCompat.STATE_NONE;
    }

    public PlaybackStateCompat getPlaybackStateAsCompat() {
        return new PlaybackStateCompat.Builder().setState(getPlaybackState(), 0L, 0F).build();
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

    public void updateUiState() { /* DO NOTING */ }

    public void sendCustomAction(String customAction, Bundle args) {
        // DO NOTHING
    }
}
