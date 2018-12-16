package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;

import static com.example.mike.mp3player.commons.Constants.TIMESTAMP;

public class MyMediaPlayerAdapter {

    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    private MediaPlayer mediaPlayer;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private Context context;
    private int currentState;
    private boolean isPrepared = false;

    public MyMediaPlayerAdapter(Context context) {
        this.context = context;
    }

    /**
     * TODO: Provide a track that is prepared for when the service starts, to stop the Activities from
     * crashing
     */
    public void init(Uri uri) {
        resetPlayer();
        prepareFromUri(uri);

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

    public boolean prepareFromUri(Uri uri) {
        if (null != uri)
        {
            try {
                resetPlayer();
                setCurrentUri(uri);
                prepare();
                return true;
            } catch (Exception ex) {
                Log.e(LOG_TAG, ex.getMessage());
                return false;
            }
        }
        return false;
    }

    private void resetPlayer() {
        if (null != getMediaPlayer()) {
            getMediaPlayer().reset();
        } else {
            this.mediaPlayer = new MediaPlayer();
        }
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

    public void increaseSpeed( float by) {
        float currentSpeed = getMediaPlayer().getPlaybackParams().getSpeed();
        float newSpeed = currentSpeed + by;
        if (newSpeed <= 2f) {
            PlaybackParams newParams = getMediaPlayer().getPlaybackParams();
            newParams.setSpeed(newSpeed);
            getMediaPlayer().setPlaybackParams(newParams);
        }
    }

    public void decreaseSpeed( float by) {
        float currentSpeed = getMediaPlayer().getPlaybackParams().getSpeed();
        float newSpeed = currentSpeed - by;
        if (newSpeed >= 0.25f) {
            PlaybackParams newParams = getMediaPlayer().getPlaybackParams();
            newParams.setSpeed(newSpeed);
            getMediaPlayer().setPlaybackParams(newParams);
        }
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
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getStackTrace().toString());
        }
    }

    private boolean prepare() {
        if (!isPrepared) {
            try {
                getMediaPlayer().prepare();
                currentState = PlaybackStateCompat.STATE_PAUSED;
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

    public PlaybackStateCompat getMediaPlayerState() {
        return new PlaybackStateCompat.Builder()
                .setState(getCurrentState(),
                        mediaPlayer.getCurrentPosition(),
                        mediaPlayer.getPlaybackParams().getSpeed())
                .build();
    }

    public MediaMetadataCompat.Builder getCurrentMetaData() {
        return  new MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
    }

    public int getCurrentTrackPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getCurrentState() {
        return currentState;
    }
}
