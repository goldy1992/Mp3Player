package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.service.MediaPlaybackService;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaPlayerActivity extends AppCompatActivity {
    private Button playPauseButton;
    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MyControllerCallback myControllerCallback = new MyControllerCallback();
    private MediaControllerCompat mediaController;
    private Uri selectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.selectedUri = (Uri)  getIntent().getExtras().get("uri");
        mConnectionCallbacks = new MyConnectionCallback(this, myControllerCallback);
        // ...
        // Create MediaBrowserServiceCompat
        setmMediaBrowser(new MediaBrowserCompat(this,
                new ComponentName(this, MediaPlaybackService.class),
                mConnectionCallbacks,
                null)); // optional Bundle

        setContentView(R.layout.activity_media_player);
    }

    @Override
    public void onStart() {
        super.onStart();
        getmMediaBrowser().connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
        if (MediaControllerCompat.getMediaController(MediaPlayerActivity.this) != null) {
            MediaControllerCompat.getMediaController(MediaPlayerActivity.this).unregisterCallback(myControllerCallback);
        }
        getmMediaBrowser().disconnect();

    }

    public Button getPlayPauseButton() {
        return playPauseButton;
    }

    public void setPlayPauseButton(Button playPauseButton) {
        this.playPauseButton = playPauseButton;
    }

    public void playPause(View view)
    {
        int pbState = getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            MediaControllerCompat.getMediaController(this).getTransportControls().pause();
            playPauseButton.setText("Play");
        } else if (pbState == PlaybackStateCompat.STATE_NONE || pbState == PlaybackStateCompat.STATE_STOPPED
                ) {
            MediaControllerCompat.getMediaController(this).getTransportControls().playFromUri(selectedUri, null);
            playPauseButton.setText("Pause");
        } else {
            MediaControllerCompat.getMediaController(this).getTransportControls().play();
            playPauseButton.setText("Pause");
        }

    }

    public void stop(View view)
    {
        int pbState = getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING ||
                pbState == PlaybackStateCompat.STATE_STOPPED )
        {
            MediaControllerCompat.getMediaController(this).getTransportControls().stop();
        } // if
    }

    private int getPlaybackState()
    {
        return MediaControllerCompat.getMediaController(MediaPlayerActivity.this).getPlaybackState().getState();
    }

    public MediaBrowserCompat getmMediaBrowser() {
        return mMediaBrowser;
    }

    public void setmMediaBrowser(MediaBrowserCompat mMediaBrowser) {
        this.mMediaBrowser = mMediaBrowser;
    }
}