package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.SimpleTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.dagger.components.DaggerFolderActivityComponent;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public class FolderActivity extends MediaActivityCompat {

    private static final String LOG_TAG = "FOLDER_ACTIVITY";
    private ChildViewPagerFragment viewPageFragment;
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
        this.playToolBarFragment = (PlayToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.playToolbarFragment);
//        this.playToolBarFragment = PlayToolBarFragment.createAndInitialisePlayToolbarFragment(getMediaControllerAdapter(), false);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initialiseDependencies();
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
    public void onConnected() {
        super.onConnected();

        this.viewPageFragment = ChildViewPagerFragment.createViewPageFragment(Category.SONGS, parentId, getMediaBrowserAdapter());
        getSupportFragmentManager().beginTransaction().add(R.id.songListFragment, viewPageFragment).commit();
        this.playToolBarFragment.init(getMediaControllerAdapter(), false);
        playToolBarFragment.displayButtons();
//        getSupportFragmentManager().beginTransaction().add(R.id.playToolbarFragment, playToolBarFragment).commitNow();


        getSupportActionBar().setTitle(getString(R.string.FOLDER_NAME, this.folderName));
        getMediaControllerAdapter().updateUiState();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void initialiseDependencies() {
        DaggerFolderActivityComponent.
            factory()
            .create(getApplicationContext(), "FLDER_ACTVY_WKR",
                getSubscriptionType(),this)
            .inject(this);
    }
}
