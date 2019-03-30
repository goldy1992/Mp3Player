package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.SimpleTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.SongViewPageFragment;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.List;

import static com.example.mike.mp3player.commons.Constants.FOLDER_CHILDREN;
import static com.example.mike.mp3player.commons.Constants.FOLDER_NAME;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public class FolderActivity extends MediaSubscriberActivityCompat {

    private static final String LOG_TAG = "FOLDER_ACTIVITY";
    private SongViewPageFragment viewPageFragment;
    private List<MediaBrowserCompat.MediaItem> mediaItems;
    private String folderName;
    private LibraryRequest parentId;
    private PlayToolBarFragment playToolBarFragment;
    private SimpleTitleBarFragment simpleTitleBarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMediaBrowserService(SubscriptionType.NOTIFY_ALL);
        Intent intent = getIntent();
        this.simpleTitleBarFragment = (SimpleTitleBarFragment) getSupportFragmentManager().findFragmentById(R.id.simpleTitleBarFragment);
        this.folderName= intent.getStringExtra(FOLDER_NAME);
        this.mediaItems = intent.getParcelableArrayListExtra(FOLDER_CHILDREN);
        this.parentId = intent.getParcelableExtra(REQUEST_OBJECT);

//        this.viewPageFragment = SongViewPageFragment.createAndInitialiseViewPageFragment(parentId, mediaItems,getMediaBrowserAdapter(), getMediaControllerAdapter());
  //      this.playToolBarFragment = PlayToolBarFragment.createAndInitialisePlayToolbarFragment(getMediaControllerAdapter(), false);
        getSupportFragmentManager().beginTransaction().add(R.id.songListFragment, viewPageFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.playToolbarFragment, playToolBarFragment).commitNow();
        playToolBarFragment.displayButtons();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getMediaControllerAdapter() != null) {
            getMediaControllerAdapter().updateUiState();
            //       setPlaybackState(mediaControllerAdapter.getCurrentPlaybackState());
        }
        // If it is null it will initialised when the MediaBrowserAdapter has connected
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConnected() {
        super.onConnected();
        setContentView(R.layout.activity_folder);
        getSupportActionBar().setTitle(getString(R.string.FOLDER_NAME, this.folderName));
        getMediaControllerAdapter().updateUiState();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.NOTIFY_ALL;
    }
}
