package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.inputmethod.InputMethodManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserConnector;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerWrapper;
import com.example.mike.mp3player.client.views.MediaPlayerActionListener;
import com.example.mike.mp3player.client.views.fragments.MainActivityRootFragment;
import com.example.mike.mp3player.service.library.SongCollection;

import java.util.List;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.CATEGORY_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.CATEGORY_SONGS_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;

public class MainActivity extends MediaActivityCompat implements MediaPlayerActionListener, MediaBrowserConnectorCallback {
    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final int READ_REQUEST_CODE = 42;
    private MediaBrowserConnector mediaBrowserConnector;
    private MediaControllerWrapper<MainActivity> mediaControllerWrapper;
    private MainActivityRootFragment rootFragment;
    private List<MediaBrowserCompat.MediaItem> rootItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        MediaSessionCompat.Token token = (MediaSessionCompat.Token) extras.get(MEDIA_SESSION);
        initMediaBrowserService(token);
        List<MediaBrowserCompat.MediaItem> songs = getIntent().getExtras().getParcelableArrayList(CATEGORY_SONGS_ID);
        rootItems = getIntent().getExtras().getParcelableArrayList(CATEGORY_ROOT_ID);
        init(songs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mediaControllerWrapper != null) {
            setPlaybackState(mediaControllerWrapper.getCurrentPlaybackState());
        } else {
            mediaControllerWrapper = new MediaControllerWrapper<>(this, mediaBrowserConnector.getMediaSessionToken());
            mediaControllerWrapper.init();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void init(List<MediaBrowserCompat.MediaItem> songs) {
        setContentView(R.layout.activity_main);
        this.rootFragment = (MainActivityRootFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivityRootFragment);
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        this.rootFragment.setInputMethodManager(inputMethodManager);
        this.rootFragment.setActionListeners(this);
        this.rootFragment.getMainFrameFragment().getViewPagerFragment().initRootMenu(rootItems);
        this.rootFragment.initRecyclerView(songs, this);

        rootFragment.getView().setFocusableInTouchMode(true);
        rootFragment.getView().requestFocus();
    }

    public void onMediaBrowserServiceConnected(MediaSessionCompat.Token token) {
        if (this instanceof MainActivity) {
            this.mediaControllerWrapper = new MediaControllerWrapper<MainActivity>(this, token);
        }
        this.mediaControllerWrapper.init();
    }

    private void initMediaBrowserService(MediaSessionCompat.Token token) {
        mediaBrowserConnector = new MediaBrowserConnector(getApplicationContext(), this);
        mediaBrowserConnector.init(token);
    }

    private Intent createMediaPlayerActivityIntent() {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra(MEDIA_SESSION, mediaBrowserConnector.getMediaSessionToken());
        return intent;
    }

    @Override //MediaPlayerActionListener
    public void playSelectedSong(String songId) {
        Intent intent = createMediaPlayerActivityIntent();
        intent.putExtra(MEDIA_ID, songId);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override // MediaPlayerActionListener
    public void play() {
        mediaControllerWrapper.play();
    }

    @Override // MediaPlayerActionListener
    public void pause() {
        mediaControllerWrapper.pause();
    }

    @Override // MediaPlayerActionListener
    public void goToMediaPlayerActivity() {
        Intent intent = createMediaPlayerActivityIntent();
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override // MediaPlayerActionListener
    public void skipToNext() {
        mediaControllerWrapper.skipToNext();
    }

    @Override // MediaPlayerActionListener
    public void skipToPrevious() {
        mediaControllerWrapper.skipToPrevious();
    }

    @Override // MediaPlayerActionListener
    public void seekTo(int position) {
        mediaControllerWrapper.seekTo(position);
    }

    @Override // MediaActivityCompat
    public void setMetaData(MediaMetadataCompat metadata) { /* no need to update meta data in this class */ }

    @Override // MediaActivityCompat
    public void setPlaybackState(PlaybackStateCompat state) {
        rootFragment.setPlaybackState(state);
    }

    @Override // MediaActivityCompat
    public MediaControllerWrapper getMediaControllerWrapper() {
        return mediaControllerWrapper;
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
        //Log.i(LOG_TAG, "more children loaded");
    }

    @Override // MediaPlayerActionListener
    public void sendCustomAction(String customAction, Bundle args) {
        mediaControllerWrapper.getMediaControllerCompat().getTransportControls().sendCustomAction(customAction, args);
    }
}