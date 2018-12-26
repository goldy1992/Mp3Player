package com.example.mike.mp3player.client;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.PlayPauseButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.PLAYBACK_STATE;

public class MainActivity extends MediaActivityCompat implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_WRITE_STORAGE = 0;
    private MediaBrowserConnector mediaBrowserConnector;
    private MediaControllerWrapper<MainActivity> mediaControllerWrapper;
    private PermissionsProcessor permissionsProcessor;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private PlayPauseButton playPauseButton;
    private Toolbar playToolbar;

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
        if (null != playPauseButton && mediaControllerWrapper != null) {
            playPauseButton.updateState(mediaControllerWrapper.getPlaybackState());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    public void init() {
        initMediaBrowserService();
        setContentView(R.layout.activity_main);
        playPauseButton = findViewById(R.id.mainActivityPlayPauseButton);
        playPauseButton.setOnClickListener((View view) -> playPause(view));
        playToolbar = findViewById(R.id.playToolbar);
        playToolbar.setOnClickListener((View view) -> goToMediaPlayerActivity(view));
        this.drawerLayout = findViewById(R.id.drawer_layout);

        MyDrawerListener myDrawerListener = new MyDrawerListener();
        drawerLayout.addDrawerListener(myDrawerListener);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                 });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void initRecyclerView(List<MediaBrowserCompat.MediaItem> songs) {
        this.recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        MyViewAdapter myViewAdapter = new MyViewAdapter(songs);
        recyclerView.setAdapter(myViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new MyItemTouchListener(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myViewAdapter.notifyDataSetChanged();
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

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void initMediaBrowserService() {
        mediaBrowserConnector = new MediaBrowserConnector(getApplicationContext(), this);
        mediaBrowserConnector.init(null);
        saveState();
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

    private void saveState()
    {
        if (0 == 0) {
            return;
        }
        try {
            File fileToCache = new File(getApplicationContext().getCacheDir(), "mediaPlayerState");
            if (fileToCache.exists())
            {
                fileToCache.delete();
            }
            fileToCache.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(fileToCache);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            //objectOut.writeObject(this.selectedUri);
            objectOut.writeObject(null);
            objectOut.close();
            fileOut.close();

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public PlayPauseButton getPlayPauseButton() {
        return playPauseButton;
    }

    public void setPlayPauseButton(PlayPauseButton playPauseButton) {
        this.playPauseButton = playPauseButton;
    }

    @Override
    public void setMetaData(MediaMetadataCompat metadata) {

    }

    @Override
    public void setPlaybackState(PlaybackStateWrapper state) {

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
}
