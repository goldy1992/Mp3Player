package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.PlayPauseButton;
import com.example.mike.mp3player.client.view.SeekerBar;
import com.example.mike.mp3player.client.view.TimeCounter;
import com.example.mike.mp3player.service.MediaPlaybackService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaPlayerActivity extends AppCompatActivity {


    private final String STOP = "Stop";

    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MyMediaControllerCallback myMediaControllerCallback;
    private MySeekerMediaControllerCallback mySeekerMediaControllerCallback;
    private MediaControllerCompat mediaControllerCompat;
    private Uri selectedUri;
    private PlayPauseButton playPauseButton;
    private SeekerBar seekerBar;
    private TimeCounter counter;
    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        this.playPauseButton = this.findViewById(R.id.playPauseButton);
        TextView counterView = this.findViewById(R.id.timer);
        this.counter = new TimeCounter(counterView);
        this.seekerBar = this.findViewById(R.id.seekBar);
        this.seekerBar.setTimeCounter(counter);
        if (getIntent() != null && getIntent().getExtras() != null) {
            this.setSelectedUri((Uri) getIntent().getExtras().get("uri"));
        }
        myMediaControllerCallback = new MyMediaControllerCallback(this);
        mySeekerMediaControllerCallback = new MySeekerMediaControllerCallback(seekerBar);
        mConnectionCallbacks = new MyConnectionCallback(this, seekerBar, myMediaControllerCallback, mySeekerMediaControllerCallback);

        // ...
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MediaPlaybackService.class),
                mConnectionCallbacks,
                null);
        saveState();
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

    @Override
    public void onPause() {
        super.onPause();
        saveState();

        //   onSaveInstanceState();
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveState();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void playPause(View view)
    {
        int pbState = getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            getMediaControllerCompat().getTransportControls().pause();
            getPlayPauseButton().setTextPlay();
        } else
        {
            getMediaControllerCompat().getTransportControls().play();
            getPlayPauseButton().setTextPause();
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

    public MediaControllerCompat getMediaControllerCompat() {
        return mediaControllerCompat;
    }

    public void setMediaControllerCompat(MediaControllerCompat mediaControllerCompat) {
        this.mediaControllerCompat = mediaControllerCompat;
    }

    public Uri getSelectedUri() {
        return this.selectedUri;
    }

    public void setSelectedUri(Uri selectedUri) {
        this.selectedUri = selectedUri;
    }

    public PlayPauseButton getPlayPauseButton() {
        return playPauseButton;
    }

    public TimeCounter getCounter() {
        return counter;
    }

    private void saveState()
    {
        try {
            File fileToCache = new File(getApplicationContext().getCacheDir(), "mediaPlayerState");
            if (fileToCache.exists())
            {
                fileToCache.delete();
            }
            fileToCache.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(fileToCache);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this.selectedUri);
            objectOut.close();
            fileOut.close();

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void retrieveState() {
        try {
            File f = new File(getApplicationContext().getCacheDir(), "mediaPlayerState");
            FileInputStream fileInputStream = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.selectedUri = (Uri) objectInputStream.readObject();
            getApplicationContext().deleteFile("mediaPlayerState");
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}