package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackToolbarExtendedFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.client.views.fragments.ShuffleRepeatFragment;
import com.example.mike.mp3player.client.views.fragments.SimpleTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.TrackInfoFragment;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.LibraryObject;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaPlayerActivity extends MediaActivityCompat {

    private final String STOP = "Stop";
    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";

    private MediaSessionCompat.Token token;
    private TrackInfoFragment trackInfoFragment;
    private PlaybackTrackerFragment playbackTrackerFragment;
    private PlaybackToolbarExtendedFragment playbackToolbarExtendedFragment;
    private PlaybackSpeedControlsFragment playbackSpeedControlsFragment;
    private SimpleTitleBarFragment simpleTitleBarFragment;
    private ShuffleRepeatFragment shuffleRepeatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (MediaSessionCompat.Token) retrieveIntentInfo(Constants.MEDIA_SESSION);

        if (token != null) {
            initialiseMediaControllerAdapter(token);
            String mediaId = (String) retrieveIntentInfo(Constants.MEDIA_ID);
            LibraryObject parentId = (LibraryObject) retrieveIntentInfo(Constants.REQUEST_OBJECT);
            initView();
            if (mediaId != null) { // if rq came with an media id it's a song request
                // Display the initial state
                Bundle extras = new Bundle();
                extras.putParcelable(REQUEST_OBJECT, parentId); // parent id will sure that the correct playlist is found in the media library
                getMediaControllerAdapter().prepareFromMediaId(mediaId, extras);
            }
            else {
//                setMetaData(mediaControllerAdapter.getMetaData());
  //              setPlaybackState(mediaControllerAdapter.getCurrentPlaybackState());
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
        /* TODO: setMediaId is possible redundant code and needs to be tested. Will temporarily comment it out in order to test.
        //setMediaId((String) retrieveIntentInfo(Constants.MEDIA_ID));
        */
        trackInfoFragment.init(getMediaControllerAdapter());
        playbackSpeedControlsFragment.init(getMediaControllerAdapter());
        playbackTrackerFragment.init(getMediaControllerAdapter());
        playbackToolbarExtendedFragment.init(getMediaControllerAdapter());
        shuffleRepeatFragment.init(getMediaControllerAdapter(), true);
        getMediaControllerAdapter().updateUiState();
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMediaControllerAdapter().disconnect();
    }
    /**
     *
     */
    private void initView() {
        setContentView(R.layout.activity_media_player);
        this.simpleTitleBarFragment = (SimpleTitleBarFragment) getSupportFragmentManager().findFragmentById(R.id.simpleTitleBarFragment);
        this.trackInfoFragment = (TrackInfoFragment) getSupportFragmentManager().findFragmentById(R.id.trackInfoFragment);
        this.playbackSpeedControlsFragment = (PlaybackSpeedControlsFragment) getSupportFragmentManager().findFragmentById(R.id.playbackSpeedControlsFragment);
        this.playbackTrackerFragment = (PlaybackTrackerFragment) getSupportFragmentManager().findFragmentById(R.id.playbackTrackerFragment);
        this.playbackToolbarExtendedFragment = (PlaybackToolbarExtendedFragment) getSupportFragmentManager().findFragmentById(R.id.playbackToolbarExtendedFragment);
        this.shuffleRepeatFragment = (ShuffleRepeatFragment) getSupportFragmentManager().findFragmentById(R.id.shuffleRepeatFragment);
        this.getPlaybackToolbarExtendedFragment().displayButtons();
    }

    private Object retrieveIntentInfo(String key) {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            return getIntent().getExtras().get(key);
        }
        return null;
    }

    public PlaybackTrackerFragment getPlaybackTrackerFragment() {
        return playbackTrackerFragment;
    }

    public PlaybackToolbarExtendedFragment getPlaybackToolbarExtendedFragment() {
        return playbackToolbarExtendedFragment;
    }

}