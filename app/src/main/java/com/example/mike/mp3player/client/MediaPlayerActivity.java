package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.service.MediaPlaybackService;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaPlayerActivity extends AppCompatActivity {
    private View playPauseButton;
    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MyControllerCallback myControllerCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myControllerCallback = new MyControllerCallback();
        mConnectionCallbacks = new MyConnectionCallback(mMediaBrowser, this, myControllerCallback);
        // ...
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MediaPlaybackService.class),
                mConnectionCallbacks,
                null); // optional Bundle
        setContentView(R.layout.activity_media_player);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowser.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
        if (MediaControllerCompat.getMediaController(MediaPlayerActivity.this) != null) {
            MediaControllerCompat.getMediaController(MediaPlayerActivity.this).unregisterCallback(myControllerCallback);
        }
        mMediaBrowser.disconnect();

    }

    public View getPlayPauseButton() {
        return playPauseButton;
    }

    public void setPlayPauseButton(View playPauseButton) {
        this.playPauseButton = playPauseButton;
    }
}