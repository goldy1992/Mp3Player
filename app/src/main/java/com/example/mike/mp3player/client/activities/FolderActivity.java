package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.SimpleTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.GenericViewPageFragment;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public class FolderActivity extends MediaBrowserSubscriberActivityCompat {

    private static final String LOG_TAG = "FOLDER_ACTIVITY";
    private GenericViewPageFragment viewPageFragment;
    private String folderName;
    private LibraryRequest parentId;
    private PlayToolBarFragment playToolBarFragment;
    private SimpleTitleBarFragment simpleTitleBarFragment;

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.MEDIA_ID;
    }

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        this.simpleTitleBarFragment = (SimpleTitleBarFragment) getSupportFragmentManager().findFragmentById(R.id.simpleTitleBarFragment);
        this.playToolBarFragment = PlayToolBarFragment.createAndInitialisePlayToolbarFragment(getMediaControllerAdapter(), false);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseView(R.layout.activity_folder);
        Intent intent = getIntent();
        this.parentId = intent.getParcelableExtra(REQUEST_OBJECT);
        this.folderName = parentId.getTitle();
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

        this.viewPageFragment = GenericViewPageFragment.createViewPageFragment(Category.SONGS, parentId, getMediaBrowserAdapter());
      getSupportFragmentManager().beginTransaction().add(R.id.songListFragment, viewPageFragment).commit();
         getSupportFragmentManager().beginTransaction().add(R.id.playToolbarFragment, playToolBarFragment).commitNow();
        playToolBarFragment.displayButtons();

        getSupportActionBar().setTitle(getString(R.string.FOLDER_NAME, this.folderName));
        getMediaControllerAdapter().updateUiState();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
