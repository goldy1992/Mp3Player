package com.example.mike.mp3player.client.activities;

import android.view.MenuItem;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

public abstract class MediaActivityCompat extends AppCompatActivity {

    private MediaControllerAdapter mediaControllerAdapter;
    private MediaBrowserAdapter mediaBrowserAdapter;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
