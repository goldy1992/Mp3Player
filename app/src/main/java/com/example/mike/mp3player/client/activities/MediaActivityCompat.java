package com.example.mike.mp3player.client.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

public abstract class MediaActivityCompat extends AppCompatActivity implements MediaBrowserConnectorCallback {

    private MediaControllerAdapter mediaControllerAdapter;
    private MediaBrowserAdapter mediaBrowserAdapter;
    private static final String LOG_TAG = "MEDIA_ACTIVITY_COMPAT";
    void initMediaBrowserService() {
        setMediaBrowserAdapter(new MediaBrowserAdapter(getApplicationContext(), this));
        getMediaBrowserAdapter().init();
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
}
