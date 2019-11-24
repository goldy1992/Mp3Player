package com.github.goldy1992.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.callbacks.TrackViewPagerChangeCallback;
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener;
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter;
import com.github.goldy1992.mp3player.client.views.fragments.MediaControlsFragment;
import com.github.goldy1992.mp3player.client.views.fragments.PlayToolBarFragment;
import com.github.goldy1992.mp3player.client.views.fragments.PlaybackTrackerFragment;

import java.util.List;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlayerActivity extends MediaActivityCompat implements MetadataListener {

    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";

    private PlaybackTrackerFragment playbackTrackerFragment;
    private PlayToolBarFragment playToolBarFragment;
    private MediaControlsFragment mediaControlsFragment;
    private ViewPager2 viewPager2;
    private TrackViewAdapter trackViewAdapter;
    private TrackViewPagerChangeCallback trackViewPagerChangeCallback;
    private Uri trackToPlay = null;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            this.trackToPlay = intent.getData();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            this.trackToPlay = intent.getData();
            mediaControllerAdapter.playFromUri(trackToPlay, null);
        }
    }

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        FragmentManager fm = getSupportFragmentManager();
        this.viewPager2 = findViewById(R.id.track_view_pager);
        final Context context = getApplicationContext();
        AlbumArtPainter albumArtPainter = new AlbumArtPainter(Glide.with(context));
        this.trackViewPagerChangeCallback = new TrackViewPagerChangeCallback(mediaControllerAdapter);
        this.trackViewAdapter = new TrackViewAdapter(albumArtPainter, new Handler(getMainLooper()), mediaControllerAdapter.getQueue());
        this.mediaControllerAdapter.registerMetaDataListener(this);
        this.viewPager2.setAdapter(trackViewAdapter);
        this.viewPager2.registerOnPageChangeCallback(trackViewPagerChangeCallback);
        this.viewPager2.setCurrentItem(mediaControllerAdapter.getCurrentQueuePosition(), false);
        this.playbackTrackerFragment = (PlaybackTrackerFragment) fm.findFragmentById(R.id.playbackTrackerFragment);
        this.playToolBarFragment = (PlayToolBarFragment) fm.findFragmentById(R.id.playbackToolbarExtendedFragment);
        this.mediaControlsFragment = (MediaControlsFragment) fm.findFragmentById(R.id.mediaControlsFragment);
        return true;
    }

    /**
     * Callback method for when the activity connects to the MediaPlaybackService
     */
    @Override
    public void onConnected() {
        super.onConnected();
        if (null != trackToPlay) {
            mediaControllerAdapter.playFromUri(trackToPlay, null);
        }
        initialiseView(R.layout.activity_media_player);
    }

    @Override
    public void onMetadataChanged(@NonNull MediaMetadataCompat metadata) {
        // TODO: in future this should be a listener for when the queue has changed
        List<MediaSessionCompat.QueueItem> queueItems = mediaControllerAdapter.getQueue();
        int currentPosition  = mediaControllerAdapter.getCurrentQueuePosition();
        this.trackViewPagerChangeCallback.setCurrentPosition(currentPosition);
        this.viewPager2.setCurrentItem(currentPosition, false);
        this.trackViewAdapter.setQueue(queueItems);
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

    @VisibleForTesting
    public PlayToolBarFragment getPlayToolBarFragment() { return playToolBarFragment; }
    @VisibleForTesting
    public PlaybackTrackerFragment getPlaybackTrackerFragment() { return playbackTrackerFragment; }
    @VisibleForTesting
    public MediaControlsFragment getMediaControlsFragment() { return mediaControlsFragment; }
    @VisibleForTesting
    public Uri getTrackToPlay() {
        return trackToPlay;
    }
}