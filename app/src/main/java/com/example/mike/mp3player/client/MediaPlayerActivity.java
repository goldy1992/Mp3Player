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

    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MyMediaControllerCallback myMediaControllerCallback = new MyMediaControllerCallback();
    private MediaControllerCompat mediaControllerCompat;
    private Uri selectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.selectedUri = (Uri)  getIntent().getExtras().get("uri");
        mConnectionCallbacks = new MyConnectionCallback(this, myMediaControllerCallback);

        // ...
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MediaPlaybackService.class),
                mConnectionCallbacks,
                null);
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
        getmMediaBrowser().disconnect();
        if (getMediaControllerCompat() != null) {
            getMediaControllerCompat().unregisterCallback(myMediaControllerCallback);
        }
        getmMediaBrowser().disconnect();

    }


    public void playPause(View view)
    {
        Button playPauseButton = (Button)view.findViewById(R.id.playPauseButton);
        int pbState = getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            getMediaControllerCompat().getTransportControls().pause();
            playPauseButton.setText("Play");
        } else if (pbState == PlaybackStateCompat.STATE_NONE || pbState == PlaybackStateCompat.STATE_STOPPED
                ) {
            getMediaControllerCompat().getTransportControls().playFromUri(selectedUri, null);
            playPauseButton.setText("Pause");
        } else {
            getMediaControllerCompat().getTransportControls().playFromUri(selectedUri, null);
            playPauseButton.setText("Pause");
        }

    }

    public void stop(View view)
    {
        int pbState = getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING ||
                pbState == PlaybackStateCompat.STATE_STOPPED )
        {
            getMediaControllerCompat().getTransportControls().stop();
        } // if
    }

    private int getPlaybackState()
    {
        return mediaControllerCompat.getPlaybackState().getState();
    }

    public MediaBrowserCompat getmMediaBrowser() {
        return mMediaBrowser;
    }

    public void setmMediaBrowser(MediaBrowserCompat mMediaBrowser) {
        this.mMediaBrowser = mMediaBrowser;
    }

    public MediaControllerCompat getMediaControllerCompat() {
        return mediaControllerCompat;
    }

    public void setMediaControllerCompat(MediaControllerCompat mediaControllerCompat) {
        this.mediaControllerCompat = mediaControllerCompat;
    }
}