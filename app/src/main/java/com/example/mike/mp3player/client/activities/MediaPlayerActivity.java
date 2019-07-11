package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackToolbarExtendedFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.client.views.fragments.ShuffleRepeatFragment;
import com.example.mike.mp3player.client.views.fragments.SimpleTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.TrackInfoFragment;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.LibraryRequest;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlayerActivity extends MediaActivityCompat {

    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";

    private TrackInfoFragment trackInfoFragment;
    private PlaybackTrackerFragment playbackTrackerFragment;
    private PlaybackToolbarExtendedFragment playbackToolbarExtendedFragment;
    private PlaybackSpeedControlsFragment playbackSpeedControlsFragment;
    private SimpleTitleBarFragment simpleTitleBarFragment;
    private ShuffleRepeatFragment shuffleRepeatFragment;
    private LibraryRequest initialLibraryRequest;

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        this.simpleTitleBarFragment = (SimpleTitleBarFragment) getSupportFragmentManager().findFragmentById(R.id.simpleTitleBarFragment);
        this.trackInfoFragment = (TrackInfoFragment) getSupportFragmentManager().findFragmentById(R.id.trackInfoFragment);
        this.playbackSpeedControlsFragment = (PlaybackSpeedControlsFragment) getSupportFragmentManager().findFragmentById(R.id.playbackSpeedControlsFragment);
        this.playbackTrackerFragment = (PlaybackTrackerFragment) getSupportFragmentManager().findFragmentById(R.id.playbackTrackerFragment);
        this.playbackToolbarExtendedFragment = (PlaybackToolbarExtendedFragment) getSupportFragmentManager().findFragmentById(R.id.playbackToolbarExtendedFragment);
        this.shuffleRepeatFragment = (ShuffleRepeatFragment) getSupportFragmentManager().findFragmentById(R.id.shuffleRepeatFragment);
        this.getPlaybackToolbarExtendedFragment().displayButtons();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseView(R.layout.activity_media_player);
    }
    @Override
    public void onStart() {
        super.onStart();
        getPlaybackToolbarExtendedFragment().init(getMediaControllerAdapter());
        getMediaControllerAdapter().updateUiState();
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
    }
    /**
     * Callback method for when the activity connects to the MediaPlaybackService
     */
    @Override
    public void onConnected() {
        super.onConnected();
        LibraryRequest libraryRequest = (LibraryRequest) retrieveIntentInfo(Constants.REQUEST_OBJECT);
        if (libraryRequest != null) { // if RQ came with an media id it's a song request
            String mediaId = libraryRequest.getId();
            // Display the initial state
            // parent id will sure that the correct playlist is found in the media library
            getMediaControllerAdapter().prepareFromMediaId(mediaId, getIntent().getExtras());
        }
    }

    /**
     * The MediaActivity does not subscribe to any type of media, but is the interface to connect
     * to the control the playback on the MediaPlaybackService
     * @return the subscription type
     */
    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkerId() {
        return "MDIA_PLYER_ACTVT_WKR";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMediaControllerAdapter().disconnect();
    }

    private Object retrieveIntentInfo(String key) {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            return getIntent().getExtras().get(key);
        }
        return null;
    }

    @VisibleForTesting
    public PlaybackToolbarExtendedFragment getPlaybackToolbarExtendedFragment() { return playbackToolbarExtendedFragment; }
    @VisibleForTesting
    public TrackInfoFragment getTrackInfoFragment() { return trackInfoFragment; }
    @VisibleForTesting
    public PlaybackTrackerFragment getPlaybackTrackerFragment() { return playbackTrackerFragment; }
    @VisibleForTesting
    public PlaybackSpeedControlsFragment getPlaybackSpeedControlsFragment() { return playbackSpeedControlsFragment; }
    @VisibleForTesting
    public SimpleTitleBarFragment getSimpleTitleBarFragment() { return simpleTitleBarFragment; }
    @VisibleForTesting
    public ShuffleRepeatFragment getShuffleRepeatFragment() { return shuffleRepeatFragment; }
}