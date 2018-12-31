package com.example.mike.mp3player.client;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.inputmethod.InputMethodManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.MainFrameFragment;
import com.example.mike.mp3player.client.view.MediaPlayerActionListener;
import com.example.mike.mp3player.client.view.MyRecyclerView;
import com.example.mike.mp3player.client.view.PlayPauseButton;
import com.example.mike.mp3player.client.view.SongFilterFragment;
import com.example.mike.mp3player.client.view.SongSearchActionListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.PLAYBACK_STATE;

public class MainActivity extends MediaActivityCompat implements ActivityCompat.OnRequestPermissionsResultCallback,
        SongSearchActionListener,
        MediaPlayerActionListener {

    private MediaBrowserConnector mediaBrowserConnector;
    private MediaControllerWrapper<MainActivity> mediaControllerWrapper;
    private PermissionsProcessor permissionsProcessor;
    private InputMethodManager inputMethodManager;

    private MainFrameFragment mainFrameFragment;
    private SongFilterFragment songFilterFragment;

    private static final String LOG_TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionsProcessor= new PermissionsProcessor(this);
        permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mediaControllerWrapper != null) {
            PlayPauseButton playPauseButton = mainFrameFragment.getPlayToolBarFragment().getPlayPauseButton();
            playPauseButton.updateState(mediaControllerWrapper.getPlaybackState());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void init() {
        initMediaBrowserService();
        inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(R.layout.activity_main);
        this.mainFrameFragment = (MainFrameFragment) getSupportFragmentManager().findFragmentById(R.id.mainFrameFragment);
        this.mainFrameFragment.getPlayToolBarFragment().setMediaPlayerActionListener(this);
        this.mainFrameFragment.getTitleBarFragment().setSongSearchActionListener(this);
        this.songFilterFragment = (SongFilterFragment) getSupportFragmentManager().findFragmentById(R.id.searchSongViewFragment);
        this.songFilterFragment.setSongSearchActionListener(this);

        Toolbar titleToolbar = mainFrameFragment.getTitleBarFragment().getTitleToolbar();
        setSupportActionBar(titleToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        }

    public void initRecyclerView(List<MediaBrowserCompat.MediaItem> songs) {
        mainFrameFragment.initRecyclerView(songs, this);
    }

    public void onMediaBrowserServiceConnected(MediaSessionCompat.Token token) {
        if (this instanceof MainActivity) {
            this.mediaControllerWrapper = new MediaControllerWrapper<MainActivity>(this, token);
        }
        this.mediaControllerWrapper.init(null);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//      //  getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
    private static final int READ_REQUEST_CODE = 42;

    @Override
    public void playSelectedSong(String songId) {
        Intent intent = createMediaPlayerActivityIntent();
        intent.putExtra(MEDIA_ID, songId);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void play() {
        mediaControllerWrapper.play();
    }

    @Override
    public void pause() {
        mediaControllerWrapper.pause();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void initMediaBrowserService() {
        mediaBrowserConnector = new MediaBrowserConnector(getApplicationContext(), this);
        mediaBrowserConnector.init(null);
    }

    private Intent createMediaPlayerActivityIntent() {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra(MEDIA_SESSION, mediaBrowserConnector.getMediaSessionToken());
        intent.putExtra(PLAYBACK_STATE, mediaControllerWrapper.getCurrentPlaybackState());
        return intent;
    }

    @Override
    public void goToMediaPlayerActivity() {
        Intent intent = createMediaPlayerActivityIntent();
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void setMetaData(MediaMetadataCompat metadata) {

    }

    @Override
    public void setPlaybackState(PlaybackStateWrapper state) {
        final int newState = state.getPlaybackState().getState();
        PlayPauseButton playPauseButton = mainFrameFragment.getPlayToolBarFragment().getPlayPauseButton();
        playPauseButton.updateState(newState);
    }

    @Override
    public MediaControllerWrapper getMediaControllerWrapper() {
        return mediaControllerWrapper;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        String permission = permissionsProcessor.getPermissionFromRequestCode(requestCode);

        if (null != permission) {
            if (permission.equals(WRITE_EXTERNAL_STORAGE)) {
                // Request for camera permission.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. Start camera preview Activity.
                    init();
                }
            }
        }
        else {
            finish();
        }
    }

    @Override
    public void onStartSearch() {
        songFilterFragment.getView().bringToFront();
        songFilterFragment.onSearchStart(inputMethodManager);
        songFilterFragment.setActive(true);
        MyRecyclerView recyclerView = mainFrameFragment.getRecyclerView();
        recyclerView.setTouchable(false);
    }

    @Override
    public void onFinishSearch() {
        mainFrameFragment.getView().bringToFront();
        songFilterFragment.onSearchFinish(inputMethodManager);
        MyRecyclerView recyclerView = mainFrameFragment.getRecyclerView();
        recyclerView.setTouchable(true);
    }

    @Override
    public void onNewSearchFilter(String filter) {
        mainFrameFragment.getRecyclerView().filter(filter);
    }
}
