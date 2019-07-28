package com.example.mike.mp3player.client.activities;

import android.content.Intent;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.MetadataTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackToolbarExtendedFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.client.views.fragments.ShuffleRepeatFragment;
import com.example.mike.mp3player.client.views.fragments.AlbumArtFragment;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.LibraryRequest;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlayerActivity extends MediaActivityCompat {

    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";

    private PlaybackTrackerFragment playbackTrackerFragment;
    private PlaybackToolbarExtendedFragment playbackToolbarExtendedFragment;
    private PlaybackSpeedControlsFragment playbackSpeedControlsFragment;
    private MetadataTitleBarFragment metadataTitleBarFragment;
    private ShuffleRepeatFragment shuffleRepeatFragment;
    private AlbumArtFragment albumArtFragment;

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        FragmentManager fm = getSupportFragmentManager();
        this.metadataTitleBarFragment = (MetadataTitleBarFragment) fm.findFragmentById(R.id.metadataTitleBarFragment);
        this.playbackSpeedControlsFragment = (PlaybackSpeedControlsFragment) fm.findFragmentById(R.id.playbackSpeedControlsFragment);
        this.playbackTrackerFragment = (PlaybackTrackerFragment) fm.findFragmentById(R.id.playbackTrackerFragment);
        this.playbackToolbarExtendedFragment = (PlaybackToolbarExtendedFragment) fm.findFragmentById(R.id.playbackToolbarExtendedFragment);
        this.shuffleRepeatFragment = (ShuffleRepeatFragment) fm.findFragmentById(R.id.shuffleRepeatFragment);
        this.albumArtFragment = (AlbumArtFragment) fm.findFragmentById(R.id.albumArtFragment);
        return true;
    }

    /**
     * Callback method for when the activity connects to the MediaPlaybackService
     */
    @Override
    public void onConnected() {
        super.onConnected();
        initialiseView(R.layout.activity_media_player);
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
    public PlaybackTrackerFragment getPlaybackTrackerFragment() { return playbackTrackerFragment; }
    @VisibleForTesting
    public PlaybackSpeedControlsFragment getPlaybackSpeedControlsFragment() { return playbackSpeedControlsFragment; }
    @VisibleForTesting
    public MetadataTitleBarFragment getMetadataTitleBarFragment() { return metadataTitleBarFragment; }
    @VisibleForTesting
    public ShuffleRepeatFragment getShuffleRepeatFragment() { return shuffleRepeatFragment; }
    @VisibleForTesting
    public AlbumArtFragment getAlbumArtFragment() { return albumArtFragment;}
}