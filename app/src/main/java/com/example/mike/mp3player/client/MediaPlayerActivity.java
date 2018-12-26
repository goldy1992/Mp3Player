package com.example.mike.mp3player.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.view.PlayPauseButton;
import com.example.mike.mp3player.client.view.SeekerBar;
import com.example.mike.mp3player.client.view.TimeCounter;
import com.example.mike.mp3player.commons.Constants;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.PLAYLIST;
import static com.example.mike.mp3player.commons.Constants.PLAY_ALL;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaPlayerActivity extends MediaActivityCompat {

    private final String STOP = "Stop";
    private MediaControllerWrapper<MediaPlayerActivity> mediaControllerWrapper;
    private String mediaId;
    private TextView artist;
    private TextView track;
    private TextView playbackSpeed;
    private TextView duration;
    private PlayPauseButton playPauseButton;
    private ImageButton skipToPreviousButton;
    private ImageButton skipToNextButton;
    private AppCompatImageButton increasePlaybackSpeedButton;
    private AppCompatImageButton decreasePlaybackSpeedButton;
    private SeekerBar seekerBar;
    private TimeCounter counter;
    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";
    private MediaSessionCompat.Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        token = (MediaSessionCompat.Token) retrieveIntentInfo(Constants.MEDIA_SESSION);
        setMediaId((String) retrieveIntentInfo(Constants.MEDIA_ID));
        PlaybackStateWrapper playbackStateWrapper = (PlaybackStateWrapper) retrieveIntentInfo(Constants.PLAYBACK_STATE);
        if (playbackStateWrapper == null) {
            PlaybackStateCompat.Builder playbackStateCompat = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 0L, 0F);
            playbackStateWrapper = new PlaybackStateWrapper(playbackStateCompat.build());
        }

        if (token != null) {
            this.mediaControllerWrapper = new MediaControllerWrapper<MediaPlayerActivity>(this, token);
            mediaControllerWrapper.init(playbackStateWrapper);

            if (playNewSong()) {
                // Display the initial state
                Bundle extras = new Bundle();
                extras.putString(PLAYLIST, PLAY_ALL);
                mediaControllerWrapper.prepareFromMediaId(getMediaId(), extras);
            } else {
                setMetaData(mediaControllerWrapper.getMetaData());
                setPlaybackState(mediaControllerWrapper.getCurrentPlaybackState());
            }
        } else {
            /** TODO: Add functionality for when the playback bar is touched in the MainActivity and no
             * token is passed or when no track info is specified, a track must be sent to the mediacontrollerwrapper
             */
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getIntent() != null && getIntent().getExtras() != null) {
            token = (MediaSessionCompat.Token) getIntent().getExtras().get(Constants.MEDIA_SESSION);
            setMediaId((String) getIntent().getExtras().get(Constants.MEDIA_ID));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaControllerWrapper.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void playPause(View view)
    {
        int pbState = mediaControllerWrapper.getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            mediaControllerWrapper.pause();
            getPlayPauseButton().setPlayIcon();
        } else {
            mediaControllerWrapper.play();
            getPlayPauseButton().setPauseIcon();
        }
    }

    public void skipToNext(View view) {
        mediaControllerWrapper.skipToNext();
    }

    public void skipToPrevious(View view) {
        mediaControllerWrapper.skipToPrevious();
    }

    public void increasePlaybackSpeed(View view) {
        Bundle extras = new Bundle();
        mediaControllerWrapper.getMediaControllerCompat().getTransportControls()
                .sendCustomAction(INCREASE_PLAYBACK_SPEED, extras);
    }

    public void decreasePlaybackSpeed(View view) {
        Bundle extras = new Bundle();
        mediaControllerWrapper.getMediaControllerCompat().getTransportControls()
                .sendCustomAction(DECREASE_PLAYBACK_SPEED, extras);
    }

    public void stop(View view) {
        int pbState = mediaControllerWrapper.getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING ||
                pbState == PlaybackStateCompat.STATE_STOPPED ) {
            mediaControllerWrapper.stop();
        } // if
    }

    public PlayPauseButton getPlayPauseButton() {
        return playPauseButton;
    }

    public TimeCounter getCounter() {
        return counter;
    }


    public TextView getArtist() {
        return artist;
    }

    public void setArtist(String artist) {

        this.artist.setText(getString(R.string.ARTIST_NAME, artist));
    }

    public TextView getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track.setText(getString(R.string.TRACK_NAME, track));

    }

    private void initView() {
        setContentView(R.layout.activity_media_player);
        this.playPauseButton = this.findViewById(R.id.playPauseButton);
        this.playPauseButton.setOnClickListener((View view) -> playPause(view));

        this.skipToPreviousButton = this.findViewById(R.id.skip_to_previous);
        this.skipToPreviousButton.setOnClickListener((View view) -> skipToPrevious(view));

        this.skipToNextButton = this.findViewById(R.id.skip_to_next);
        this.skipToNextButton.setOnClickListener((View view) -> skipToNext(view));

        this.decreasePlaybackSpeedButton = this.findViewById(R.id.decreasePlaybackSpeed);
        this.decreasePlaybackSpeedButton.setOnClickListener((View view) -> decreasePlaybackSpeed(view));

        this.increasePlaybackSpeedButton = this.findViewById(R.id.increasePlaybackSpeed);
        this.increasePlaybackSpeedButton.setOnClickListener((View view) -> increasePlaybackSpeed(view));

        TextView counterView = this.findViewById(R.id.timer);
        this.counter = new TimeCounter(this, counterView);
        this.seekerBar = this.findViewById(R.id.seekBar);
        this.seekerBar.init();
        this.seekerBar.setTimeCounter(counter);
        this.seekerBar.setParentActivity(this);
        this.artist = this.findViewById(R.id.artistName);
        this.track = this.findViewById(R.id.trackName);
        this.duration = this.findViewById(R.id.duration);
        this.playbackSpeed = this.findViewById(R.id.playbackSpeedValue);
    }

    private Object retrieveIntentInfo(String key) {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            return getIntent().getExtras().get(key);
        }
        return null;
    }

    @Override
    public void setMetaData(MediaMetadataCompat metaData) {
        long duration = metaData.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        getCounter().setDuration(duration);
        this.duration.setText(TimerUtils.formatTime(duration));
        setArtist(metaData.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        setTrack(metaData.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        seekerBar.getMySeekerMediaControllerCallback().onMetadataChanged(metaData);
    }

    @Override
    public void setPlaybackState(PlaybackStateWrapper playbackState) {
        getPlayPauseButton().updateState(playbackState);
        getCounter().updateState(playbackState);
        float speed = playbackState.getPlaybackState().getPlaybackSpeed();
        if (speed > 0) {
            updatePlaybackSpeedText(speed);
        }
        seekerBar.getMySeekerMediaControllerCallback().onPlaybackStateChanged(playbackState);
    }

    private void updatePlaybackSpeedText(float speed) {
        Runnable r = () ->  playbackSpeed.setText(getString(R.string.PLAYBACK_SPEED_VALUE, speed));
        runOnUiThread(r);
    }

    @Override
    public MediaControllerWrapper getMediaControllerWrapper() {
       return mediaControllerWrapper;
    }

    private boolean playNewSong() {
        return null != getMediaId();
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}