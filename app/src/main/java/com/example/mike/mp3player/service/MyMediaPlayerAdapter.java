package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;

public class MyMediaPlayerAdapter {

    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    private MediaPlayer mediaPlayer;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private Uri currentUri;
    private Context context;
    private MediaSessionCompat mediaSession;
    private int currentState;
    private PlayBackNotifier playBackNotifier;
    private MetaDataNotifier metaDataNotifier;
    private boolean isPrepared = false;

    public MyMediaPlayerAdapter(Context context, PlayBackNotifier playBackNotifier, MetaDataNotifier metaDataNotifier) {
        this.context = context;
        this.playBackNotifier = playBackNotifier;
        this.metaDataNotifier = metaDataNotifier;
    }

    public void init()
    {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
           // mediaPlayer.setPlaybackParams(new PlaybackParams());
        }
        this.afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int i) {
            }
        };
    }

    public void play() {
        if (requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                currentState = PlaybackStateCompat.STATE_PLAYING;
                // start the player (custom call)
                mediaPlayer.start();
                playBackNotifier.notifyPlay(mediaPlayer.getCurrentPosition(), mediaPlayer.getPlaybackParams().getSpeed());
                //            // Register BECOME_NOISY BroadcastReceiver
//            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
//            // Put the service in the foreground, post notification
//            service.startForeground(myPlayerNotification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playFromUri(Uri uri) {
        if (requestAudioFocus()) {
                     // Set the session active  (and update metadata and state)
            // start the player (custom call)
            setCurrentUri(uri);
            if(prepare()) {
                mediaPlayer.start();
                playBackNotifier.notifyPlay(0L, mediaPlayer.getPlaybackParams().getSpeed());
                metaDataNotifier.notifyMetaDataChange(mediaPlayer);
            }
        }
    }

    public void prepareFromUri(Uri uri) {
        mediaPlayer.reset();
        setCurrentUri(uri);
        if (prepare()) {
            currentState = PlaybackStateCompat.STATE_PAUSED;
            playBackNotifier.notifyPause(0L);
            metaDataNotifier.notifyMetaDataChange(mediaPlayer);
        }
    }

    public void stop() {
        // unregisterReceiver(myNoisyAudioStreamReceiver);
        currentState= PlaybackStateCompat.STATE_STOPPED;
        isPrepared = false;
        mediaPlayer.stop();
        mediaPlayer.reset();
        playBackNotifier.notifyStop();
        // Take the service out of the foreground
    }

    public void pause() {
        // Update metadata and state
        mediaPlayer.pause();
        currentState = PlaybackStateCompat.STATE_PAUSED;
        playBackNotifier.notifyPause(mediaPlayer.getCurrentPosition());
    }


    public void seekTo(long position) {
        mediaPlayer.seekTo((int)position);
        playBackNotifier.notifySeekTo(currentState, position, mediaPlayer.getPlaybackParams().getSpeed());
    }

    private boolean requestAudioFocus()
    {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // Request audio focus for playback, this registers the afChangeListener
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }

    private void setCurrentUri(Uri uri){
        try {
            mediaPlayer.setDataSource(context, uri);
            this.currentUri = uri;
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

    private boolean prepare() {
        if (!isPrepared) {
            try {
                mediaPlayer.prepare();
                isPrepared = true;
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }

        return isPrepared;

    }
}
