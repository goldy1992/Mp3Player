package com.example.mike.mp3player.client.activities;

import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MenuItem;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

public abstract class MediaActivityCompat extends AppCompatActivity implements MediaBrowserConnectorCallback {

    private final String WORKER_ID = getClass().toString();
    private MediaControllerAdapter mediaControllerAdapter;
    private MediaBrowserAdapter mediaBrowserAdapter;
    private static final String LOG_TAG = "MEDIA_ACTIVITY_COMPAT";
    private HandlerThread worker;

    void initialiseView(@LayoutRes int layoutId) {
        setContentView(layoutId);
    }

    void initMediaBrowserService(SubscriptionType subscriptionType) {
        setMediaBrowserAdapter(new MediaBrowserAdapter(getApplicationContext(), this, getWorker().getLooper()));
        getMediaBrowserAdapter().init(subscriptionType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        worker = new HandlerThread(WORKER_ID);
        getWorker().start();
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionSuspended() { /* TODO: implement onConnectionSuspended */
        Log.i(LOG_TAG, "connection suspended");}

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() {  /* TODO: implement onConnectionFailed */
        Log.i(LOG_TAG, "connection failed");}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getWorker().quitSafely();
    }


    public final MediaControllerAdapter getMediaControllerAdapter() {
        return mediaControllerAdapter;
    }
    public final void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }
    public final MediaBrowserAdapter getMediaBrowserAdapter() {
        return mediaBrowserAdapter;
    }

    public final void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    public HandlerThread getWorker() {
        return worker;
    }
}
