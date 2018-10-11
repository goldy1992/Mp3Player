package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;

public class MyMediaPlayerAdapter {

    private MediaPlayer mediaPlayer;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private Uri currentUri;
    private Context context;
    private PlaybackStateCompat.Builder stateBuilder;
    private MediaSessionCompat mediaSession;
    private int currentState;

    public MyMediaPlayerAdapter(Context context, MediaSessionCompat mediaSession)
    {
        this.context = context;
        this.mediaSession = mediaSession;
    }

    public void init()
    {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
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
                stateBuilder = new PlaybackStateCompat.Builder().setState(currentState, mediaPlayer.getCurrentPosition(), 1f);
                mediaSession.setPlaybackState(stateBuilder.build());
                mediaSession.setActive(true);
                //            // Register BECOME_NOISY BroadcastReceiver
//            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
//            // Put the service in the foreground, post notification
//            service.startForeground(myPlayerNotification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playFromuri(Uri uri) {
        if (requestAudioFocus()) {
                     // Set the session active  (and update metadata and state)
            mediaSession.setActive(true);
            // start the player (custom call)
            try {
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                currentState = PlaybackStateCompat.STATE_PLAYING;
                stateBuilder = new PlaybackStateCompat.Builder().setState(currentState, 0L, mediaPlayer.getPlaybackParams().getSpeed());
                MediaMetadataCompat.Builder mediaMetadataCompatBuilder = new MediaMetadataCompat.Builder().putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
                mediaSession.setMetadata(mediaMetadataCompatBuilder.build());
                mediaSession.setPlaybackState(stateBuilder.build());
            } catch (IOException ex) {
                Log.e("MediaSessionCallback", "" + ex);
            }
        }
    }

    public void prepareFromUri(Uri uri) {
        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, uri);
            this.currentUri = uri;
            currentState = PlaybackStateCompat.STATE_STOPPED;
            stateBuilder = new PlaybackStateCompat.Builder().setState(currentState, 0L, 0f);
            MediaMetadataCompat.Builder mediaMetadataCompatBuilder = new MediaMetadataCompat.Builder().putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
            mediaSession.setMetadata(mediaMetadataCompatBuilder.build());
            mediaSession.setPlaybackState(stateBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        // unregisterReceiver(myNoisyAudioStreamReceiver);

        currentState= PlaybackStateCompat.STATE_STOPPED;
        mediaPlayer.stop();
        mediaPlayer.reset();
        stateBuilder = new PlaybackStateCompat.Builder().setState(currentState, 0L, 0f);
        mediaSession.setPlaybackState(stateBuilder.build());

        // Set the session inactive  (and update metadata and state)
        mediaSession.setActive(false);
        // Take the service out of the foreground
    }

    public void pause() {
        // Update metadata and state
        mediaPlayer.pause();
        currentState = PlaybackStateCompat.STATE_PAUSED;
        stateBuilder = new PlaybackStateCompat.Builder().setState(currentState, mediaPlayer.getCurrentPosition(), mediaPlayer.getPlaybackParams().getSpeed());
        mediaSession.setPlaybackState(stateBuilder.build());
    }


    public void seekTo(long position) {
        mediaPlayer.seekTo((int)position);
        stateBuilder = new PlaybackStateCompat.Builder().setState(currentState, mediaPlayer.getCurrentPosition(), mediaPlayer.getPlaybackParams().getSpeed());
        mediaSession.setPlaybackState(stateBuilder.build());
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
}
