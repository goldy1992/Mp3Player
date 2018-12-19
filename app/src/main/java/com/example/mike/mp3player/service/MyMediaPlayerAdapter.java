package com.example.mike.mp3player.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.AndroidUtils;

import java.io.IOException;

public class MyMediaPlayerAdapter {

    private static final float PAUSED = 0.0f;
    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    private MediaPlayer mediaPlayer;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private Context context;
    @PlaybackStateCompat.State
    private int currentState;
    private float currentPlaybackSpeed = PAUSED;
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

        if (requestAudioFocus() || isPlaying()) {
            try {
                // Set the session active  (and update metadata and state)
                currentState = PlaybackStateCompat.STATE_PLAYING;
                // start the player (custom call)
                getMediaPlayer().start();
                //            // Register BECOME_NOISY BroadcastReceiver
//            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
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
                setPlaybackParams();
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

    /**
     * we never want to use stop when calling the player,
     * because we can just reset the mediaplayer and when a song is
     * prepared we can put it into the paused state.
     */
    @Deprecated
    public void stop() {
        if (!isPrepared()) {
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
        if (!isPrepared() || isPaused()) {
            return;
        }
        // Update metadata and state
        getMediaPlayer().pause();
        currentState = PlaybackStateCompat.STATE_PAUSED;
    }

    public void increaseSpeed(float by) {
        float currentSpeed = getMediaPlayer().getPlaybackParams().getSpeed();
        float newSpeed = currentSpeed + by;
        if (newSpeed <= 2f) {
            this.currentPlaybackSpeed = newSpeed;
            setPlaybackParams();
        }
    }

    public void decreaseSpeed(float by) {
        float currentSpeed = getMediaPlayer().getPlaybackParams().getSpeed();
        float newSpeed = currentSpeed - by;
        if (newSpeed >= 0.25f) {
            this.currentPlaybackSpeed = newSpeed;
            setPlaybackParams();
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
        return AndroidUtils.isAndroidOreoOrHigher() ?
                requestAudioFocusOreo(am)  :
                requestAudioFocusBelowApi26(am);
    }

    private void setCurrentUri(Uri uri) {
        try {
            getMediaPlayer().setDataSource(context, uri);
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getStackTrace().toString());
        }
    }

    private boolean prepare() {
        if (!isPrepared()) {
            try {
                getMediaPlayer().prepare();
                currentState = PlaybackStateCompat.STATE_PAUSED;
                isPrepared = true;
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }
        return isPrepared();
    }

    private void setPlaybackParams() {
        if (getMediaPlayer() != null && getMediaPlayer().getPlaybackParams() != null) {
            PlaybackParams newParams = getMediaPlayer().getPlaybackParams();
            newParams.setSpeed(getCurrentPlaybackSpeed());
            getMediaPlayer().setPlaybackParams(newParams);
        }
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
        return new MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
    }

    public int getCurrentTrackPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getCurrentState() {
        return currentState;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @SuppressWarnings("deprecation")
    private boolean requestAudioFocusBelowApi26( AudioManager am) {

        // Request audio focus for playback, this registers the afChangeListener
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private boolean requestAudioFocusOreo( AudioManager am) {

        AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        AudioAttributes audioAttributes = audioAttributesBuilder.build();
        // Request audio focus for playback, this registers the afChangeListener
        AudioFocusRequest.Builder audioFocusRequestBuilder = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setOnAudioFocusChangeListener(afChangeListener)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setWillPauseWhenDucked(false);

        AudioFocusRequest audioFocusRequest = audioFocusRequestBuilder.build();
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.requestAudioFocus(audioFocusRequest);
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public boolean isPlaying() {
        return currentState == PlaybackStateCompat.STATE_PLAYING;
    }

    public boolean isPaused() {
        return currentState == PlaybackStateCompat.STATE_PAUSED;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * init playbackspeed to be paused i.e 0.0f
     */
    public float getCurrentPlaybackSpeed() {
        return currentPlaybackSpeed;
    }
}
