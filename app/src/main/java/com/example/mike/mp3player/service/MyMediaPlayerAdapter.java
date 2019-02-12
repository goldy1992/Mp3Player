package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;

import androidx.annotation.IntRange;

public class MyMediaPlayerAdapter {

    private static final float DEFAULT_SPEED = 1.0f;
    private static final float DEFAULT_PITCH = 1.0f;
    private static final int DEFAULT_POSITION = 0;
    private static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    private static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";

    private MediaPlayer mediaPlayer;
    private AudioFocusManager audioFocusManager;
    private Context context;
    private Uri currentUri;
    /**
     * initialise to paused so the player doesn't start playing immediately
     */
    @PlaybackStateCompat.State
    private int currentState = PlaybackStateCompat.STATE_PAUSED;;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private float currentPitch = DEFAULT_PITCH;
    private int position = DEFAULT_POSITION;
    private int bufferedPosition = DEFAULT_POSITION;
    private boolean isPrepared = false;
    private boolean dataSourceSet = false;
    private boolean useBufferedPosition = false;

    public MyMediaPlayerAdapter(Context context) {
        this.context = context;
    }

    /**
     * TODO: Provide a track that is prepared for when the service starts, to stop the Activities from
     * crashing
     */
    public void init(Uri uri) {
        resetPlayer();
        //currentPlaybackSpeed = 0f;
        setCurrentUri(uri);// set player to paused
        //prepareFromUri(uri);
        prepare();
        audioFocusManager = new AudioFocusManager(context, this);
        audioFocusManager.init();
    }

    public synchronized void playFromUri(Uri uri) {
        resetPlayer();
        setCurrentUri(uri);
        play();
    }

    public synchronized void play() {
        if (!prepare()) {
            return;
        }

        if (audioFocusManager.requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                setPlaybackParamsAndPosition();
                getMediaPlayer().start();
                currentState = PlaybackStateCompat.STATE_PLAYING;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean prepareFromUri(Uri uri) {
        if (null != uri) {
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
            mediaPlayer = new MediaPlayer();
        }
        this.isPrepared = false;
        this.currentState = PlaybackStateCompat.STATE_NONE;
        this.dataSourceSet = false;
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
        this.currentPlaybackSpeed = getMediaPlayer().getPlaybackParams().getSpeed();
        // Update metadata and state
        getMediaPlayer().pause();
        audioFocusManager.playbackPaused();
        currentState = PlaybackStateCompat.STATE_PAUSED;
        //logPlaybackParams(mediaPlayer.getPlaybackParams());
    }

    public void increaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed + by;
        if (newSpeed < MAXIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            //Log.i(LOG_TAG, "current speed ¡ " + this.currentPlaybackSpeed);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                setBufferedPosition(mediaPlayer.getCurrentPosition());
                this.isPrepared = false;
                resetPlayer();
                setCurrentUri(currentUri);
                play();
            } else {
                prepare();
            }
        }
    }

    public void decreaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed - by;
        if (newSpeed > MINIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            Log.i(LOG_TAG, "current speed ¡ " + this.currentPlaybackSpeed);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                setBufferedPosition(mediaPlayer.getCurrentPosition());
                this.isPrepared = false;
                resetPlayer();
                setCurrentUri(currentUri);
                play();
            } else {
                prepare();
            }
        }
    }

    public void seekTo(long position) {
        if (!prepare()) {
            return;
        }
        getMediaPlayer().seekTo((int)position);
    }

    private boolean setCurrentUri(Uri uri) {
       try {
           this.mediaPlayer.setDataSource(context, uri);
       } catch (IOException ex) {
           Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
           return false;
       }
        this.dataSourceSet = true;
        this.position = DEFAULT_POSITION;
        this.currentUri = uri;
        return true;
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


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public PlaybackStateCompat getMediaPlayerState() {
        return new PlaybackStateCompat.Builder()
                .setState(getCurrentState(),
                        mediaPlayer.getCurrentPosition(),
                        getCurrentPlaybackSpeed())
                .build();
    }

    public MediaMetadataCompat.Builder getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        return builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
    }

    public int getCurrentState() {
        return currentState;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    public boolean isPlaying() {
        return currentState == PlaybackStateCompat.STATE_PLAYING;
    }

    public boolean isPaused() {
        return currentState == PlaybackStateCompat.STATE_PAUSED;
    }

    public float getCurrentPlaybackSpeed() {
        return currentPlaybackSpeed;
    }

    private boolean validSpeed(float speed) {
        return speed >= MINIMUM_PLAYBACK_SPEED &&
                speed <= MAXIMUM_PLAYBACK_SPEED;
    }

    private void logPlaybackParams(PlaybackParams playbackParams) {
        StringBuilder sb = new StringBuilder();

        String pitch = "pitch: " + playbackParams.getPitch()+ "\n";
        String audioFallbackMode = "audiofallbackmode: ";
        switch (playbackParams.getAudioFallbackMode()) {
            case PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_DEFAULT"; break;
            case PlaybackParams.AUDIO_FALLBACK_MODE_FAIL: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_FAIL"; break;
            case PlaybackParams.AUDIO_FALLBACK_MODE_MUTE: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_MUTE"; break;
            default: audioFallbackMode = "none";
        }
        audioFallbackMode += "\n";
        String speed = "speed: " + currentPlaybackSpeed + "\n";

        String log = sb.append(pitch).append(audioFallbackMode).append(speed).toString();
        Log.i(LOG_TAG, log);
    }

    private void setPlaybackParamsAndPosition() {
        PlaybackParams playbackParams = new PlaybackParams();
        playbackParams = playbackParams.allowDefaults()
                .setPitch(currentPitch)
                .setSpeed(currentPlaybackSpeed)
                .setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
        int currentPlaybackPosition = getCurrentPlaybackPosition();
        mediaPlayer.seekTo(currentPlaybackPosition);
        mediaPlayer.setPlaybackParams(playbackParams);
    }

    public void setBufferedPosition(int position) {
        this.bufferedPosition = position;
        this.useBufferedPosition = true;
    }

    public int getCurrentPlaybackPosition() {
        int playbackPosition = mediaPlayer.getCurrentPosition();
        if (useBufferedPosition) {
            useBufferedPosition = false;
            playbackPosition = bufferedPosition;
        }

        /**
         * This is to resolve a bug on some phones where the playback position is 0 (using <= 0 to
         * resolve any negative position errors) :- the MediaPlayer JNI throws an IllegalStateException.
         * To resolve this problem 1 is returned instead of zero (assuming in 99.99% the duration is >= 1)
         */
        if (playbackPosition <= 0) { // if at the beginning at track, or for some reason negative
            if (mediaPlayer.getDuration() >= 1) {
                return 1;
            }
        }
        return playbackPosition;
    }
}
