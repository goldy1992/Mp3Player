package com.example.mike.mp3player.client;

import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MySeekerMediaControllerCallback;
import com.example.mike.mp3player.client.view.PlayPauseButton;
import com.example.mike.mp3player.client.view.SeekerBar;
import com.example.mike.mp3player.client.view.TimeCounter;
import com.example.mike.mp3player.commons.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import static com.example.mike.mp3player.commons.Constants.PLAYLIST;
import static com.example.mike.mp3player.commons.Constants.PLAY_ALL;
import static com.example.mike.mp3player.commons.Constants.TIMESTAMP;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaPlayerActivity extends AppCompatActivity {

    private final String STOP = "Stop";
    private MyMediaControllerCallback myMediaControllerCallback;
    private MySeekerMediaControllerCallback mySeekerMediaControllerCallback;
    private MediaControllerCompat mediaControllerCompat;
    private Uri selectedUri;
    private String mediaId;
    private TextView artist;
    private TextView track;
    private PlayPauseButton playPauseButton;
    private SeekerBar seekerBar;
    private TimeCounter counter;
    private final String LOG_TAG = "MEDIA_PLAYER_ACTIVITY";
    private MediaSessionCompat.Token token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        this.playPauseButton = this.findViewById(R.id.playPauseButton);
        TextView counterView = this.findViewById(R.id.timer);
        this.counter = new TimeCounter(counterView);
        this.seekerBar = this.findViewById(R.id.seekBar);
        this.artist = this.findViewById(R.id.artistName);
        this.track = this.findViewById(R.id.trackName);
        this.seekerBar.setTimeCounter(counter);

        if (getIntent() != null && getIntent().getExtras() != null) {
            token = (MediaSessionCompat.Token) getIntent().getExtras().get(Constants.MEDIA_SESSION);
            mediaId = (String) getIntent().getExtras().get(Constants.MEDIA_ID);
        }
        if (token != null) {
            try {
                mediaControllerCompat = new MediaControllerCompat(getApplicationContext(), token);
                myMediaControllerCallback = new MyMediaControllerCallback(this);
                mySeekerMediaControllerCallback = new MySeekerMediaControllerCallback(seekerBar);
                mediaControllerCompat.registerCallback(myMediaControllerCallback);

                setMediaControllerCompat(mediaControllerCompat);
                seekerBar.setMediaController(mediaControllerCompat);
                if (null != mediaId) {
                    // Display the initial state
                    Bundle extras = new Bundle();
                    extras.putString(PLAYLIST, PLAY_ALL);
                    mediaControllerCompat.getTransportControls().prepareFromMediaId(mediaId, extras);
                } else {
                    setPlaybackState(mediaControllerCompat.getPlaybackState());
                    setMetaData(mediaControllerCompat.getMetadata());
                }
            } catch (RemoteException e) {

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getIntent() != null && getIntent().getExtras() != null) {
            token = (MediaSessionCompat.Token) getIntent().getExtras().get(Constants.MEDIA_SESSION);
            mediaId = (String) getIntent().getExtras().get(Constants.MEDIA_ID);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaControllerCompat != null) {
            mediaControllerCompat.unregisterCallback(myMediaControllerCallback);
            mediaControllerCompat = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //saveState();
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void playPause(View view)
    {
        int pbState = getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            getMediaControllerCompat().getTransportControls().pause();
            getPlayPauseButton().setPlayIcon();
        } else {
            getMediaControllerCompat().getTransportControls().play();
            getPlayPauseButton().setPauseIcon();
        }
    }

    public void skipToNext(View view) {
        getMediaControllerCompat().getTransportControls().skipToNext();
    }

    public void skipToPrevious(View view) {
        getMediaControllerCompat().getTransportControls().skipToPrevious();
    }

    public void stop(View view) {
        int pbState = getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING ||
                pbState == PlaybackStateCompat.STATE_STOPPED ) {
            getMediaControllerCompat().getTransportControls().stop();
        } // if
    }

    private int getPlaybackState() {
        return mediaControllerCompat.getPlaybackState().getState();
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

    private void retrieveState() {
        try {
            File f = new File(getApplicationContext().getCacheDir(), "mediaPlayerState");
            if (null != f) {
                return;
            }
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

    public TextView getArtist() {
        return artist;
    }

    public void setArtist(String artist) {

        this.artist.setText(getString(R.string.ARTIST_NAME) + artist);
    }

    public TextView getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track.setText(getString(R.string.TRACK_NAME) + track);
    }

    public void setMetaData(MediaMetadataCompat metaData) {
        getCounter().setDuration(metaData.getLong(MediaMetadata.METADATA_KEY_DURATION));
        setArtist(metaData.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        setTrack(metaData.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        mySeekerMediaControllerCallback.onMetadataChanged(metaData);
    }

    public void setPlaybackState(PlaybackStateCompat playbackState) {
        this.getMediaController().getExtras().putLong(TIMESTAMP, System.currentTimeMillis());
        getPlayPauseButton().updateState(playbackState);
        getCounter().updateState(playbackState);
        mySeekerMediaControllerCallback.onPlaybackStateChanged(playbackState);
    }
}