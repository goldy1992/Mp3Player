package com.example.mike.mp3player.client;

import android.content.Intent;
import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mike.mp3player.R;
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

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaPlayerActivity extends MediaActivityCompat {

    private final String STOP = "Stop";
    private MediaControllerWrapper<MediaPlayerActivity> mediaControllerWrapper;
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
        initView();
        retrieveIntentInfo(getIntent());

        if (token != null) {
            this.mediaControllerWrapper = new MediaControllerWrapper<MediaPlayerActivity>(this, token);
            mediaControllerWrapper.init();

            if (playNewSong()) {
                // Display the initial state
                Bundle extras = new Bundle();
                extras.putString(PLAYLIST, PLAY_ALL);
                mediaControllerWrapper.prepareFromMediaId(mediaId, extras);
            } else {
                setPlaybackState(mediaControllerWrapper.getPlaybackStateAsCompat());
                setMetaData(mediaControllerWrapper.getMetaData());
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
        mediaControllerWrapper.disconnect();
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
        int pbState = mediaControllerWrapper.getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            mediaControllerWrapper.pause();
            getPlayPauseButton().setPlayIcon();
        } else {
            mediaControllerWrapper.play();
            getPlayPauseButton().setPauseIcon();
        }
    }

    public void skipToNext(View view) {
        mediaControllerWrapper.skipToNext();
    }

    public void skipToPrevious(View view) {
        mediaControllerWrapper.skipToPrevious();
    }

    public void stop(View view) {
        int pbState = mediaControllerWrapper.getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING ||
                pbState == PlaybackStateCompat.STATE_STOPPED ) {
            mediaControllerWrapper.stop();
        } // if
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

    private void initView() {
        setContentView(R.layout.activity_media_player);
        this.playPauseButton = this.findViewById(R.id.playPauseButton);
        TextView counterView = this.findViewById(R.id.timer);
        this.counter = new TimeCounter(counterView);
        this.seekerBar = this.findViewById(R.id.seekBar);
        this.seekerBar.init();
        this.seekerBar.setTimeCounter(counter);
        this.seekerBar.setParentActivity(this);
        this.artist = this.findViewById(R.id.artistName);
        this.track = this.findViewById(R.id.trackName);
    }

    private void retrieveIntentInfo(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            token = (MediaSessionCompat.Token) getIntent().getExtras().get(Constants.MEDIA_SESSION);
            mediaId = (String) getIntent().getExtras().get(Constants.MEDIA_ID);
        }
    }

    @Override
    public void setMetaData(MediaMetadataCompat metaData) {
        getCounter().setDuration(metaData.getLong(MediaMetadata.METADATA_KEY_DURATION));
        setArtist(metaData.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        setTrack(metaData.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        seekerBar.getMySeekerMediaControllerCallback().onMetadataChanged(metaData);
    }

    @Override
    public void setPlaybackState(PlaybackStateCompat playbackState) {
        getPlayPauseButton().updateState(playbackState);
        getCounter().updateState(playbackState);
        seekerBar.getMySeekerMediaControllerCallback().onPlaybackStateChanged(playbackState);
    }

    @Override
    public MediaControllerWrapper getMediaControllerWrapper() {
       return mediaControllerWrapper;
    }

    private boolean playNewSong() {
        return null != mediaId;
    }
}