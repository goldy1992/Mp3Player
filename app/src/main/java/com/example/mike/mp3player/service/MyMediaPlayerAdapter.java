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

import static android.media.MediaPlayer.MEDIA_INFO_STARTED_AS_NEXT;

public class MyMediaPlayerAdapter implements MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener {

    private static final float DEFAULT_SPEED = 1.0f;
    private static final float DEFAULT_PITCH = 1.0f;
    private static final int DEFAULT_POSITION = 0;
    private static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    private static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";

    private MediaPlayer currentMediaPlayer;
    private MediaPlayer nextMediaPlayer;
    private AudioFocusManager audioFocusManager;
    private Context context;
    /**
     * initialise to paused so the player doesn't start playing immediately
     */
    @PlaybackStateCompat.State
    private int currentState = PlaybackStateCompat.STATE_PAUSED;;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private float currentPitch = DEFAULT_PITCH;
    private boolean isPrepared = true;

    public MyMediaPlayerAdapter(Context context) {
        this.context = context;
    }

    /**
     * TODO: Provide a track that is prepared for when the service starts, to stop the Activities from
     * crashing
     */
    public void reset(Uri firstItemUri, Uri secondItemUri, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (audioFocusManager != null && audioFocusManager.hasFocus) {
            audioFocusManager.abandonAudioFocus();
        }
        if (this.currentMediaPlayer != null) {
            currentMediaPlayer.release();
            currentMediaPlayer = null;
        }

        if (this.nextMediaPlayer != null) {
            nextMediaPlayer.release();
            nextMediaPlayer = null;
        }
        this.currentMediaPlayer = createMediaPlayer(firstItemUri, onCompletionListener);
        this.nextMediaPlayer = secondItemUri == null ? null : createMediaPlayer(secondItemUri, onCompletionListener);
        this.currentMediaPlayer.setNextMediaPlayer(nextMediaPlayer);
        this.audioFocusManager = new AudioFocusManager(context, this);
        this.audioFocusManager.init();
        this.currentState = PlaybackStateCompat.STATE_PAUSED;
    }


    public synchronized void play() {
        if (!prepare()) {
            return;
        }

        if (audioFocusManager.requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                getCurrentMediaPlayer().start();
                PlaybackParams playbackParams = currentMediaPlayer.getPlaybackParams();
                playbackParams.setSpeed(currentPlaybackSpeed);
                getCurrentMediaPlayer().setPlaybackParams(playbackParams);
                currentState = PlaybackStateCompat.STATE_PLAYING;
            } catch (Exception e) {
               Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(e));
            }
        }
        Log.i(LOG_TAG, "finished mplayer_adapter onPlay");
    }

    public boolean prepareFromUri(Uri uri) {
        if (null != uri) {
            try {
         //       resetPlayer();
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

    /**
     * 1) complete the current completed MediaPlayer
     * 2) set the currentMediaPlayer to me the next one that is currently playing
     * 3) create the next mediaPlayer and set it.
     * @param nextUriToPrepare next URI that needs to be prepared.
     */
    public void onComplete(Uri nextUriToPrepare, MediaPlayer.OnCompletionListener newOnCompletionListener) {
        this.currentMediaPlayer.release();
//        this.currentMediaPlayer = null;
        this.currentMediaPlayer = this.nextMediaPlayer;
        // TODO: we might want to make this an asynchronous task in the future
        if (nextUriToPrepare != null) {
            this.nextMediaPlayer = createMediaPlayer(nextUriToPrepare, newOnCompletionListener);
            this.currentMediaPlayer.setNextMediaPlayer(nextMediaPlayer);
        }
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
        getCurrentMediaPlayer().stop();
        // Take the service out of the foreground
    }

    public void pause() {
        if (isPaused()) {
            return;
        }
        this.currentPlaybackSpeed = getCurrentMediaPlayer().getPlaybackParams().getSpeed();
        // Update metadata and state
        getCurrentMediaPlayer().pause();
        audioFocusManager.playbackPaused();
        currentState = PlaybackStateCompat.STATE_PAUSED;
        //logPlaybackParams(currentMediaPlayer.getPlaybackParams());
    }

    public void increaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed + by;
        if (newSpeed <= MAXIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            Log.i(LOG_TAG, "current speed ¡ " + this.currentPlaybackSpeed);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                PlaybackParams newPlaybackParams = this.currentMediaPlayer.getPlaybackParams();
                newPlaybackParams.setSpeed(newSpeed);
                this.currentMediaPlayer.setPlaybackParams(newPlaybackParams);
            }
        }
    }

    public void decreaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed - by;
        if (newSpeed >= MINIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            Log.i(LOG_TAG, "current speed ¡ " + this.currentPlaybackSpeed);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                PlaybackParams newPlaybackParams = this.currentMediaPlayer.getPlaybackParams();
                newPlaybackParams.setSpeed(newSpeed);
                this.currentMediaPlayer.setPlaybackParams(newPlaybackParams);
            }
        }
    }

    public void seekTo(long position) {
        if (!prepare()) {
            return;
        }
        getCurrentMediaPlayer().seekTo((int)position);
    }

    private boolean setCurrentUri(Uri uri) {
        try {
       this.currentMediaPlayer = MediaPlayer.create(context, uri);
       } catch (Exception ex) {
           Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
           return false;
       }
        return true;
    }

    private boolean prepare() {
        if (!isPrepared()) {
            try {
                getCurrentMediaPlayer().prepare();
                currentState = PlaybackStateCompat.STATE_PAUSED;
                isPrepared = true;
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }
        return isPrepared();
    }


    public MediaPlayer getCurrentMediaPlayer() {
        return currentMediaPlayer;
    }

    public PlaybackStateCompat getMediaPlayerState() {
        return new PlaybackStateCompat.Builder()
                .setState(getCurrentState(),
                        currentMediaPlayer.getCurrentPosition(),
                        getCurrentPlaybackSpeed())
                .build();
    }

    public MediaMetadataCompat.Builder getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        return builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, currentMediaPlayer.getDuration());
    }

    public int getCurrentState() {
        return currentState;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setVolume(float volume) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.setVolume(volume, volume);
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

    private void setPlaybackParams(MediaPlayer mediaPlayer) {
        if (null != mediaPlayer) {
            PlaybackParams playbackParams = mediaPlayer.getPlaybackParams();
            playbackParams.setSpeed(currentPlaybackSpeed);
            mediaPlayer.setPlaybackParams(playbackParams);
        }
    }


    public int getCurrentPlaybackPosition() {
        return currentMediaPlayer.getCurrentPosition();
//        int playbackPosition = currentMediaPlayer.getCurrentPosition();
//        if (useBufferedPosition) {
//            useBufferedPosition = false;
//            playbackPosition = bufferedPosition;
//        }
//
//        /**
//         * This is to resolve a bug on some phones where the playback position is 0 (using <= 0 to
//         * resolve any negative position errors) :- the MediaPlayer JNI throws an IllegalStateException.
//         * To resolve this problem 1 is returned instead of zero (assuming in 99.99% the duration is >= 1)
//         */
//        if (playbackPosition <= 0) { // if at the beginning at track, or for some reason negative
//            if (currentMediaPlayer.getDuration() >= 1) {
//                return 1;
//            }
//        }
//        return playbackPosition;
    }

    private MediaPlayer createMediaPlayer(Uri uri, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setOnInfoListener(this::onInfo);
        mediaPlayer.setOnErrorListener(this::onError);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        return mediaPlayer;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(LOG_TAG, "what: " + what + ", extra " + extra);
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MEDIA_INFO_STARTED_AS_NEXT) {
            setPlaybackParams(mp);
        }
        Log.i(LOG_TAG, "what: " + what + ", extra " + extra);
        return true;
    }}
