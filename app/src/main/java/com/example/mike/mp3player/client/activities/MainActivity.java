package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.MainActivityRootFragment;

import androidx.annotation.LayoutRes;

public class MainActivity extends MediaSubscriberActivityCompat{

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private MainActivityRootFragment rootFragment;
    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMediaBrowserService(getSubscriptionType());
        this.inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        initialiseView(R.layout.activity_main);
    }

    @Override
    void initialiseView(@LayoutRes int layoutRes) {
        setContentView(layoutRes);
        this.rootFragment = (MainActivityRootFragment)getSupportFragmentManager().findFragmentById(R.id.mainActivityRootFragment);
        this.rootFragment.init(inputMethodManager, getPreSubscribedItems().getItemMap());
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
    protected void onPause  () {
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
        getMediaBrowserAdapter().setAutoSubscribe(true);
        rootFragment.populatePlaybackMetaDataListeners(getMediaBrowserAdapter(), getMediaControllerAdapter());
    }

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.CATEGORY;
    }
}