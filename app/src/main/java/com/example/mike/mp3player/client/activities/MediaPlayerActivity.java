package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.PlayPauseButton;
import com.example.mike.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackToolbarExtendedFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.client.views.fragments.TrackInfoFragment;
import com.example.mike.mp3player.commons.Constants;

import static com.example.mike.mp3player.commons.Constants.PLAYLIST;
import static com.example.mike.mp3player.commons.Constants.PLAY_ALL;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaPlayerActivity extends MediaActivityCompat {

    private final String STOP = "Stop";
    private MediaControllerAdapter mediaControllerAdapter;
    private String mediaId;


    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";
    private MediaSessionCompat.Token token;
    private TrackInfoFragment trackInfoFragment;
    private PlaybackTrackerFragment playbackTrackerFragment;
    private PlaybackToolbarExtendedFragment playbackToolbarExtendedFragment;
    private PlaybackSpeedControlsFragment playbackSpeedControlsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        token = (MediaSessionCompat.Token) retrieveIntentInfo(Constants.MEDIA_SESSION);

        if (token != null) {
            this.mediaControllerAdapter = new MediaControllerAdapter(this, token);
            setMediaId((String) retrieveIntentInfo(Constants.MEDIA_ID));

            mediaControllerAdapter.init();
            if (playNewSong()) {
                // Display the initial state
                Bundle extras = new Bundle();
                extras.putString(PLAYLIST, PLAY_ALL);
                mediaControllerAdapter.prepareFromMediaId(getMediaId(), extras);
            }
            else {
                setMetaData(mediaControllerAdapter.getMetaData());
                setPlaybackState(mediaControllerAdapter.getCurrentPlaybackState());
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
        mediaControllerAdapter.disconnect();
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

    private void initView() {
        setContentView(R.layout.activity_media_player);
        this.trackInfoFragment = (TrackInfoFragment) getSupportFragmentManager().findFragmentById(R.id.trackInfoFragment);
        this.playbackSpeedControlsFragment = (PlaybackSpeedControlsFragment) getSupportFragmentManager().findFragmentById(R.id.playbackSpeedControlsFragment);
        this.getPlaybackSpeedControlsFragment().setMediaControllerAdapter(mediaControllerAdapter);
        this.playbackTrackerFragment = (PlaybackTrackerFragment) getSupportFragmentManager().findFragmentById(R.id.playbackTrackerFragment);
        this.getPlaybackTrackerFragment().getSeekerBar().setMediaControllerAdapter(mediaControllerAdapter);
        this.playbackToolbarExtendedFragment = (PlaybackToolbarExtendedFragment) getSupportFragmentManager().findFragmentById(R.id.playbackToolbarExtendedFragment);
        this.getPlaybackToolbarExtendedFragment().setMediaControllerAdapter(mediaControllerAdapter);
        this.getPlaybackToolbarExtendedFragment().displayButtons();
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
        getPlaybackTrackerFragment().setMetaData(metaData);
        getTrackInfoFragment().setMetaData(metaData);
    }

    @Override
    public void setPlaybackState(PlaybackStateCompat state) {
        if (state != null) {
            @PlaybackStateCompat.State
            final int newState = state.getState();
            PlayPauseButton playPauseButton = getPlaybackToolbarExtendedFragment().getPlayPauseButton();
            playPauseButton.updateState(newState);
            getPlaybackTrackerFragment().setPlaybackState(state);
            getPlaybackSpeedControlsFragment().setPlaybackState(state);
        }
    }

    @Override
    public MediaControllerAdapter getMediaControllerAdapter() {
       return mediaControllerAdapter;
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

    public TrackInfoFragment getTrackInfoFragment() {
        return trackInfoFragment;
    }

    public PlaybackTrackerFragment getPlaybackTrackerFragment() {
        return playbackTrackerFragment;
    }

    public PlaybackToolbarExtendedFragment getPlaybackToolbarExtendedFragment() {
        return playbackToolbarExtendedFragment;
    }

    public PlaybackSpeedControlsFragment getPlaybackSpeedControlsFragment() {
        return playbackSpeedControlsFragment;
    }
}