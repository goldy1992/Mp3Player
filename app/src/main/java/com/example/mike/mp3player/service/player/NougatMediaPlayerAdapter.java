package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.service.AudioFocusManager;

import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.LoggingUtils.logPlaybackParams;

public class NougatMediaPlayerAdapter extends MediaPlayerAdapter {
    private MediaPlayerPool mediaPlayerPool;
    private int position = DEFAULT_POSITION;
    private static final String LOG_TAG = "MSHMLW_PLY_ADPR";
    private Uri nextUri;
    private Uri currentUri;
    private boolean needToSetPlaybackParams = true;

    public NougatMediaPlayerAdapter(Context context, AudioFocusManager audioFocusManager) {
        super(context, audioFocusManager);
        this.mediaPlayerPool = new MediaPlayerPool(context);
    }

    @Override
    public void reset(Uri firstItemUri, Uri secondItemUri) {
        this.needToSetPlaybackParams = true;
        mediaPlayerPool.reset(firstItemUri);
        this.currentUri = firstItemUri;
        this.nextUri = secondItemUri;
        super.reset(firstItemUri, secondItemUri);
    }

    @Override
    public synchronized boolean play() {
        if (currentMediaPlayer != null && audioFocusManager.requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                setPlaybackParams(currentMediaPlayer);
                currentMediaPlayer.start();
                currentMediaPlayer.setLooping(isLooping());
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
    void changeSpeed(float newSpeed) {
        if (validSpeed(newSpeed)) {
            this.currentPlaybackSpeed = newSpeed;
            int originalState = currentState;
            int bufferedPosition = currentMediaPlayer.getCurrentPosition();
            this.currentMediaPlayer.release();
            this.currentMediaPlayer = mediaPlayerPool.take();
            this.needToSetPlaybackParams = true;
            currentMediaPlayer.seekTo(bufferedPosition);

            if (originalState == PlaybackStateCompat.STATE_PLAYING) {
                play();
            } else {
                prepare();
            }
        }
    }

    @Override
    void setPlaybackParams(MediaPlayer mediaPlayer) {
        if (needToSetPlaybackParams) {
            PlaybackParams playbackParams = new PlaybackParams();
            playbackParams = playbackParams.allowDefaults()
                    .setPitch(currentPitch)
                    .setSpeed(currentPlaybackSpeed)
                    .setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
            currentMediaPlayer.setPlaybackParams(playbackParams);
            needToSetPlaybackParams = false;
        }
    }
}