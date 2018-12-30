package com.example.mike.mp3player.client;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.MyRecyclerView;
import com.example.mike.mp3player.client.view.PlayPauseButton;
import com.example.mike.mp3player.client.view.SearchSongView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.PLAYBACK_STATE;

public class MainActivity extends MediaActivityCompat implements ActivityCompat.OnRequestPermissionsResultCallback,
        SearchSongView.NewSearchFilterListener,
        SearchSongView.OnSearchExitListener,
        MyItemTouchListener.MyRecycleViewSelectListener {

    private MediaBrowserConnector mediaBrowserConnector;
    private MediaControllerWrapper<MainActivity> mediaControllerWrapper;
    private PermissionsProcessor permissionsProcessor;
    private DrawerLayout drawerLayout;
    private MyRecyclerView recyclerView;
    private PlayPauseButton playPauseButton;
    private ImageButton searchFilterButton;
    private SearchSongView searchSongView;
    private Toolbar playToolbar;
    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionsProcessor= new PermissionsProcessor(this);
        permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != playPauseButton && mediaControllerWrapper != null) {
            playPauseButton.updateState(mediaControllerWrapper.getPlaybackState());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void init() {
        initMediaBrowserService();
        inputMethodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(R.layout.activity_main);

        playPauseButton = findViewById(R.id.mainActivityPlayPauseButton);
        playPauseButton.setOnClickListener((View view) -> playPause(view));
        playToolbar = findViewById(R.id.playToolbar);
        playToolbar.setOnClickListener((View view) -> goToMediaPlayerActivity(view));
        searchFilterButton = findViewById(R.id.searchFilter);
        searchFilterButton.setOnClickListener((View view) -> onSearchFilterClick(view));
        this.searchSongView = (SearchSongView) getSupportFragmentManager().findFragmentById(R.id.searchSongViewFragment);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        MyDrawerListener myDrawerListener = new MyDrawerListener();
        drawerLayout.addDrawerListener(myDrawerListener);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((MenuItem menuItem) -> onNavigationItemSelected(menuItem));
        Toolbar toolbar = findViewById(R.id.titleToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    public void onMediaBrowserServiceConnected(MediaSessionCompat.Token token) {
        if (this instanceof MainActivity) {
            this.mediaControllerWrapper = new MediaControllerWrapper<MainActivity>(this, token);
        }
        this.mediaControllerWrapper.init(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void initRecyclerView(List<MediaBrowserCompat.MediaItem> songs) {
        this.recyclerView = findViewById(R.id.myRecyclerView);
        this.recyclerView.initRecyclerView(songs);
    }

    private static final int READ_REQUEST_CODE = 42;

    public void playSelectedSong(String songId) {
        Intent intent = createMediaPlayerActivityIntent();
        intent.putExtra(MEDIA_ID, songId);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

    public void goToMediaPlayerActivity(View view) {
        Intent intent = createMediaPlayerActivityIntent();
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void playPause(View view) {
        int pbState = mediaControllerWrapper.getPlaybackState();
        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            mediaControllerWrapper.pause();
            getPlayPauseButton().setPlayIcon();
        } else {
            mediaControllerWrapper.play();
            getPlayPauseButton().setPauseIcon();
        }
    }

    public PlayPauseButton getPlayPauseButton() {
        return playPauseButton;
    }

    public void setPlayPauseButton(PlayPauseButton playPauseButton) {
        this.playPauseButton = playPauseButton;
    }

    public void onSearchFilterClick(View view) {
        searchSongView.getView().bringToFront();
        searchSongView.onSearchStart(inputMethodManager);
        searchSongView.setActive(true);
        this.recyclerView.setTouchable(false);
    }

    @Override
    public void setMetaData(MediaMetadataCompat metadata) {

    }

    @Override
    public void setPlaybackState(PlaybackStateWrapper state) {
        final int newState = state.getPlaybackState().getState();
        if (playPauseButton.getState() != newState) {
            playPauseButton.updateState(newState);
        }
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
    public void onSearchExit() {
        drawerLayout.bringToFront();
        searchSongView.onSearchFinish(inputMethodManager);
        this.recyclerView.setTouchable(true);
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        // close drawer when item is tapped
        drawerLayout.closeDrawers();

        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true;
    }

    @Override
    public void onItemSelected(String id) {
        playSelectedSong(id);
    }

    @Override
    public void onNewSearchFilter(String filter) {
        MyViewAdapter adapter = (MyViewAdapter) recyclerView.getAdapter();
        adapter.getFilter().filter(filter.toString());
    }
}
