package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.AlbumArtFragment;
import com.example.mike.mp3player.client.views.fragments.MediaControlsFragment;
import com.example.mike.mp3player.client.views.fragments.MetadataTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.commons.MediaItemUtils;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlayerActivity extends MediaActivityCompat {

    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";

    private PlaybackTrackerFragment playbackTrackerFragment;
    private PlayToolBarFragment playToolBarFragment;
    private MetadataTitleBarFragment metadataTitleBarFragment;
    private MediaControlsFragment mediaControlsFragment;
    private AlbumArtFragment albumArtFragment;

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        FragmentManager fm = getSupportFragmentManager();
        this.metadataTitleBarFragment = (MetadataTitleBarFragment) fm.findFragmentById(R.id.metadataTitleBarFragment);
        this.playbackTrackerFragment = (PlaybackTrackerFragment) fm.findFragmentById(R.id.playbackTrackerFragment);
        this.playToolBarFragment = (PlayToolBarFragment) fm.findFragmentById(R.id.playbackToolbarExtendedFragment);
        this.mediaControlsFragment = (MediaControlsFragment) fm.findFragmentById(R.id.mediaControlsFragment);
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
    public PlayToolBarFragment getPlayToolBarFragment() { return playToolBarFragment; }
    @VisibleForTesting
    public PlaybackTrackerFragment getPlaybackTrackerFragment() { return playbackTrackerFragment; }
    @VisibleForTesting
    public MetadataTitleBarFragment getMetadataTitleBarFragment() { return metadataTitleBarFragment; }
    @VisibleForTesting
    public MediaControlsFragment getMediaControlsFragment() { return mediaControlsFragment; }
    @VisibleForTesting
    public AlbumArtFragment getAlbumArtFragment() { return albumArtFragment;}
}