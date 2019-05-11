package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.MainActivityRootFragment;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import androidx.annotation.LayoutRes;

public class MainActivity extends MediaBrowserSubscriberActivityCompat {

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final int READ_REQUEST_CODE = 42;
    private MainActivityRootFragment rootFragment;
    private InputMethodManager inputMethodManager;
    private boolean viewInitialised = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        this.viewInitialised = initialiseView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    boolean initialiseView(@LayoutRes int layoutRes) {
        setContentView(layoutRes);
        this.rootFragment = (MainActivityRootFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivityRootFragment);
        this.rootFragment.init(inputMethodManager, getMediaBrowserAdapter(), getMediaControllerAdapter());
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selected = rootFragment.getMainFrameFragment().onOptionsItemSelected(item);
        return selected || super.onOptionsItemSelected(item);
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        super.onConnected();
        LibraryRequest libraryRequest = new LibraryRequest(Category.ROOT, Category.ROOT.name());
        getMediaBrowserAdapter().subscribe(libraryRequest);
    }

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.MEDIA_ID;
    }
}