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

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public abstract class FolderActivity extends MediaActivityCompat {

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
    String getWorkerId() {
        return "FLDER_ACTVY_WKR";
    }

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        this.simpleTitleBarFragment = (SimpleTitleBarFragment) getSupportFragmentManager().findFragmentById(R.id.simpleTitleBarFragment);
        this.playToolBarFragment = (PlayToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.playToolbarFragment);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.parentId = intent.getParcelableExtra(REQUEST_OBJECT);
        this.folderName = parentId.getTitle();
    }

    @Override
    public void onConnected() {
        super.onConnected();
        this.viewPageFragment.init(Category.SONGS, parentId);
        initialiseView(R.layout.activity_folder);
        getSupportFragmentManager().beginTransaction().add(R.id.songListFragment, viewPageFragment).commit();
        getSupportActionBar().setTitle(getString(R.string.FOLDER_NAME, this.folderName));
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Inject
    public void setViewPageFragment(ChildViewPagerFragment childViewPagerFragment) {
        this.viewPageFragment = childViewPagerFragment;
    }
}
