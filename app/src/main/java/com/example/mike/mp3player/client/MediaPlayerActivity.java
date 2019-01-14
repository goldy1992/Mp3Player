package com.example.mike.mp3player.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.view.MediaPlayerActionListener;
import com.example.mike.mp3player.client.view.PlayPauseButton;
import com.example.mike.mp3player.client.view.SeekerBar;
import com.example.mike.mp3player.client.view.TimeCounter;
import com.example.mike.mp3player.client.view.fragments.PlaybackToolbarExtendedFragment;
import com.example.mike.mp3player.commons.Constants;

import androidx.appcompat.widget.AppCompatImageButton;

import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.PLAYLIST;
import static com.example.mike.mp3player.commons.Constants.PLAY_ALL;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaPlayerActivity extends MediaActivityCompat implements MediaPlayerActionListener {

    private final String STOP = "Stop";
    private MediaControllerWrapper<MediaPlayerActivity> mediaControllerWrapper;
    private String mediaId;
    private TextView artist;
    private TextView track;
    private TextView playbackSpeed;
    private TextView duration;
    private AppCompatImageButton increasePlaybackSpeedButton;
    private AppCompatImageButton decreasePlaybackSpeedButton;
    private SeekerBar seekerBar;
    private TimeCounter counter;
    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";
    private MediaSessionCompat.Token token;
    private PlaybackToolbarExtendedFragment playbackToolbarExtendedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        token = (MediaSessionCompat.Token) retrieveIntentInfo(Constants.MEDIA_SESSION);

        if (token != null) {
            this.mediaControllerWrapper = new MediaControllerWrapper<MediaPlayerActivity>(this, token);
            setMediaId((String) retrieveIntentInfo(Constants.MEDIA_ID));

            mediaControllerWrapper.init();
            if (playNewSong()) {
                // Display the initial state
                Bundle extras = new Bundle();
                extras.putString(PLAYLIST, PLAY_ALL);
                mediaControllerWrapper.prepareFromMediaId(getMediaId(), extras);
            }
            else {
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
        setMediaId((String) retrieveIntentInfo(Constants.MEDIA_ID));
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
        this.playbackToolbarExtendedFragment = (PlaybackToolbarExtendedFragment) getSupportFragmentManager().findFragmentById(R.id.playbackToolbarExtendedFragment);
        this.playbackToolbarExtendedFragment.setMediaPlayerActionListener(this);

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
    public void setPlaybackState(PlaybackStateCompat state) {

        if (state != null) {
            @PlaybackStateCompat.State
            final int newState = state.getState();
            PlayPauseButton playPauseButton = playbackToolbarExtendedFragment.getPlayPauseButton();
            playPauseButton.updateState(newState);
        }

        getCounter().updateState(state);
        float speed = state.getPlaybackSpeed();
        if (speed > 0) {
            updatePlaybackSpeedText(speed);
        }
        seekerBar.getMySeekerMediaControllerCallback().onPlaybackStateChanged(state);
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

    @Override
    public void playSelectedSong(String songId) {
        // no song should be selected in this view
    }

    @Override
    public void play() {
        mediaControllerWrapper.play();
    }

    @Override
    public void pause() {
        mediaControllerWrapper.pause();
    }

    @Override
    public void goToMediaPlayerActivity() {
        // do nothing we're already here
    }

    @Override
    public void skipToNext() {
        mediaControllerWrapper.skipToNext();
    }

    @Override
    public void skipToPrevious() {
        mediaControllerWrapper.skipToPrevious();
    }
}