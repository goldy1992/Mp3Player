package com.example.mike.mp3player.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.service.library.ContentManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;
import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

public class MockMediaSessionCallback extends MediaSessionCallback {
    /**
     * new constructor to be used for testing and also for future use with dagger2 via the @Inject
     * annotation
     *
     * @param mediaLibrary        media library
     * @param playbackManager     playback manager
     * @param mediaPlayerAdapter  media player adapter
     * @param mediaSessionAdapter media session adapter
     * @param serviceManager      serviceManager
     * @param broadcastReceiver   broadcast receiver
     * @param handler             handler
     */
    public MockMediaSessionCallback(ContentManager mediaLibrary, PlaybackManager playbackManager, MediaPlayerAdapter mediaPlayerAdapter, MediaSessionAdapter mediaSessionAdapter, ServiceManager serviceManager, AudioBecomingNoisyBroadcastReceiver broadcastReceiver, Handler handler) {
        super(mediaLibrary, playbackManager, mediaPlayerAdapter, mediaSessionAdapter, serviceManager, broadcastReceiver, handler);
    }

    @Override
    public synchronized void onPlay() { /* DO NOTHING */ }

    @Override
    public synchronized void onSkipToNext(){ /* DO NOTHING */ }

    @Override
    public synchronized void onSkipToPrevious(){ /* DO NOTHING */ }

    @Override
    public synchronized boolean onMediaButtonEvent(Intent mediaButtonEvent) { return true; }

    @Override
    public synchronized void onPrepareFromMediaId(String mediaId, Bundle bundle){ /* DO NOTHING */ }

    @Override
    public synchronized void onStop(){ /* DO NOTHING */ }

    @Override
    public synchronized void onPause(){ /* DO NOTHING */ }

    @Override
    public void onSeekTo(long position ) { /* DO NOTHING */ }

    @Override
    public synchronized void onAddQueueItem(MediaDescriptionCompat description) { /* DO NOTHING */ }

    @Override
    public synchronized void onRemoveQueueItem(MediaDescriptionCompat description){ /* DO NOTHING */ }

    @Override
    public void onSetRepeatMode (int repeatMode) { /* DO NOTHING */ }

    @Override
    public void onSetShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) { /* DO NOTHING */ }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void onCompletion(MediaPlayer mediaPlayer) { /* DO NOTHING */ }

    @Override
    public synchronized void onCustomAction(String customAction, Bundle extras) { /* DO NOTHING */ }

    @Override
    public void onSeekComplete(MediaPlayer mp) { /* DO NOTHING */ }

}
