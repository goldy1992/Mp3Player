package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.service.AudioFocusManager;

import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.LoggingUtils.logPlaybackParams;

public class MarshmallowMediaPlayerAdapter extends MediaPlayerAdapter {

    private MediaPlayerPool mediaPlayerPool;
    private int position = DEFAULT_POSITION;
    private static final String LOG_TAG = "MSHMLW_PLY_ADPR";
    private Uri nextUri;
    private Uri currentUri;

    public MarshmallowMediaPlayerAdapter(Context context, AudioFocusManager audioFocusManager) {
        super(context, audioFocusManager);
        this.mediaPlayerPool = new MediaPlayerPool(context);
    }
    @Override
    public void reset(Uri firstItemUri, Uri secondItemUri) {
        this.currentUri = firstItemUri;
        this.nextUri = secondItemUri;
        if (null != firstItemUri) {
            mediaPlayerPool.reset(firstItemUri);
        }
        super.reset(firstItemUri, secondItemUri);
    }

    @Override
    public synchronized boolean play() {
        if (null != currentMediaPlayer && audioFocusManager.requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                currentMediaPlayer.start();
                getCurrentMediaPlayer().setLooping(isLooping());
                currentState = PlaybackStateCompat.STATE_PLAYING;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    MediaPlayer createMediaPlayer(Uri uri) {
        MediaPlayer mediaPlayer = super.createMediaPlayer(uri);
        Log.d(LOG_TAG, "creating media player with URI: " + uri);
        if (mediaPlayer != null) {
            setPlaybackParams(mediaPlayer);
        }
        return  mediaPlayer;
    }

    @Override
    void setPlaybackParams(MediaPlayer currentMediaPlayer) {
        PlaybackParams playbackParams = new PlaybackParams();
        playbackParams = playbackParams.allowDefaults()
                .setPitch(currentPitch)
                .setSpeed(currentPlaybackSpeed)
                .setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
        currentMediaPlayer.setPlaybackParams(playbackParams);
    }

    @Override
    public void pause() {
        if (!isPrepared() || isPaused()) {
            return;
        }
        this.currentPlaybackSpeed = currentMediaPlayer.getPlaybackParams().getSpeed();
        // Update metadata and state
        currentMediaPlayer.pause();
        audioFocusManager.playbackPaused();
        currentState = PlaybackStateCompat.STATE_PAUSED;
        logPlaybackParams(currentMediaPlayer.getPlaybackParams(), LOG_TAG);
    }

    @Override
    public void onComplete(Uri nextUriToPrepare) {
        if (null != nextMediaPlayer) {
            setPlaybackParams(nextMediaPlayer);
        }

        super.onComplete(nextUriToPrepare);
        this.currentUri = this.nextUri;
        this.nextUri = nextUriToPrepare;
        mediaPlayerPool.reset(currentUri);
    }

    @Override
    void changeSpeed(float newSpeed) {
        if (validSpeed(newSpeed)) {
            this.currentPlaybackSpeed = newSpeed;
            int originalState = currentState;
            int bufferedPosition = currentMediaPlayer.getCurrentPosition();
            this.currentMediaPlayer.release();
            this.currentMediaPlayer = takeFromMediaPlayerPool();
            setPlaybackParams(currentMediaPlayer);
            currentMediaPlayer.seekTo(bufferedPosition);

            if (originalState == PlaybackStateCompat.STATE_PLAYING) {
                play();
            } else {
                prepare();
            }
        }
    }

    private MediaPlayer takeFromMediaPlayerPool() {
        MediaPlayer mediaPlayer = this.mediaPlayerPool.take();
        return  setListeners(mediaPlayer);
    }
}
