package com.example.mike.mp3player.client.activities;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.MainFrameFragment;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

public abstract class MainActivity extends MediaActivityCompat {

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final int READ_REQUEST_CODE = 42;
    private MainFrameFragment rootFragment;
    private InputMethodManager inputMethodManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    boolean initialiseView(@LayoutRes int layoutRes) {
        setContentView(layoutRes);
        this.rootFragment = (MainFrameFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivityRootFragment);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selected = rootFragment.onOptionsItemSelected(item);
        return selected || super.onOptionsItemSelected(item);
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        super.onConnected();
        initialiseView(R.layout.activity_main);
        LibraryRequest libraryRequest = new LibraryRequest(Category.ROOT, Category.ROOT.name());
        getMediaBrowserAdapter().subscribe(libraryRequest);
    }

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.MEDIA_ID;
    }

    /** {@inheritDoc} */
    @Override
    String getWorkerId() {
        return "MAIN_ACTVTY_WRKR";
    }
}