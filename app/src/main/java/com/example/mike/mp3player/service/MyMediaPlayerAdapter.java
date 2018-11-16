package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;

public class MyMediaPlayerAdapter implements MediaPlayer.OnPreparedListener {

    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    private MediaPlayer mediaPlayer;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private Uri currentUri;
    private Context context;
    private int currentState;
    private boolean isPrepared = false;
    private int stateOnPrepared;

    public MyMediaPlayerAdapter(Context context) {
        this.context = context;
    }

    public void init() {
        if (getMediaPlayer() == null) {
            setMediaPlayer(new MediaPlayer());
            this.getMediaPlayer().setOnPreparedListener(this);
           // mediaPlayer.setPlaybackParams(new PlaybackParams());
        }
        this.afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int i) {
            }
        };
    }

    public void play() {
        if (!prepare()) {
            return;
        }

        if (requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                currentState = PlaybackStateCompat.STATE_PLAYING;
                // start the player (custom call)
                getMediaPlayer().start();
                //            // Register BECOME_NOISY BroadcastReceiver
//            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
//            // Put the service in the foreground, post notification
//            service.startForeground(myPlayerNotification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareFromUri(Uri uri) {
        resetPlayer();
        setCurrentUri(uri);
        stateOnPrepared = PlaybackStateCompat.STATE_PAUSED;
        prepare();
    }

    private void resetPlayer() {
        getMediaPlayer().reset();
        this.isPrepared = false;
    }

    public void stop() {
        if (!isPrepared) {
            return;
        }
        // unregisterReceiver(myNoisyAudioStreamReceiver);
        currentState= PlaybackStateCompat.STATE_STOPPED;
        isPrepared = false;
        getMediaPlayer().stop();
        resetPlayer();
        // Take the service out of the foreground
    }

    public void pause() {
        if (!isPrepared) {
            return;
        }
        // Update metadata and state
        getMediaPlayer().pause();
        currentState = PlaybackStateCompat.STATE_PAUSED;
    }


    public void seekTo(long position) {
        if (!prepare()) {
            return;
        }
        getMediaPlayer().seekTo((int)position);
    }

    private boolean requestAudioFocus() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // Request audio focus for playback, this registers the afChangeListener
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }

    private void setCurrentUri(Uri uri) {
        try {
            getMediaPlayer().setDataSource(context, uri);
            this.currentUri = uri;
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getStackTrace().toString());
        }
    }

    private boolean prepare() {
        if (!isPrepared) {
            try {
                getMediaPlayer().prepare();
                isPrepared = true;
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }
        return isPrepared;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public PlaybackStateCompat getMediaPlayerState() {
        return new PlaybackStateCompat.Builder()
                .setState(getCurrentState(),
                        mediaPlayer.getCurrentPosition(),
                        mediaPlayer.getPlaybackParams().getSpeed(),
                        System.currentTimeMillis())
                .build();
    }

    public MediaMetadataCompat getCurrentMetaData() {
        return  new MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration())
                .build();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        isPrepared = true;
        switch (stateOnPrepared) {
            case PlaybackStateCompat.STATE_PLAYING:
                getMediaPlayer().start();
                currentState = PlaybackStateCompat.STATE_PLAYING;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                currentState = PlaybackStateCompat.STATE_PAUSED;
        }
    }

    public int getCurrentState() {
        return currentState;
    }
}
