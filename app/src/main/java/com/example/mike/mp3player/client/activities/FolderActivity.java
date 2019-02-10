package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.SongViewPageFragment;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.List;

import androidx.appcompat.widget.Toolbar;

import static com.example.mike.mp3player.commons.Constants.FOLDER_CHILDREN;
import static com.example.mike.mp3player.commons.Constants.FOLDER_NAME;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

public class FolderActivity extends MediaActivityCompat implements MediaPlayerActvityRequester {

    private static final String LOG_TAG = "FOLDER_ACTIVITY";
    private SongViewPageFragment viewPageFragment;
    private List<MediaBrowserCompat.MediaItem> mediaItems;
    private String folderName;
    private LibraryId parentId;
    private PlayToolBarFragment playToolBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMediaBrowserService();
        Intent intent = getIntent();
        this.folderName= intent.getStringExtra(FOLDER_NAME);
        this.mediaItems = intent.getParcelableArrayListExtra(FOLDER_CHILDREN);
        this.parentId = intent.getParcelableExtra(PARENT_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getMediaControllerAdapter() != null) {
            getMediaControllerAdapter().updateUiState();
            //       setPlaybackState(mediaControllerAdapter.getCurrentPlaybackState());
        }
        // If it is null it will initialised when the MediaBrowserAdapter has connected
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void goToMediaPlayerActivity() {

    }

    @Override
    public void playSelectedSong(String songId) {
        Log.i(LOG_TAG, "calling song: " + songId);
    }

    @Override
    public void onConnected() {
        setMediaControllerAdapter(new MediaControllerAdapter(this, getMediaBrowserAdapter().getMediaSessionToken()));
        getMediaControllerAdapter().init();
        setContentView(R.layout.activity_folder);
        this.viewPageFragment = SongViewPageFragment.createAndInitialiseViewPageFragment(parentId, mediaItems,getMediaBrowserAdapter(), getMediaControllerAdapter());
        this.playToolBarFragment = PlayToolBarFragment.createAndInitialisePlayToolbarFragment(getMediaControllerAdapter(), this, false);
        getSupportFragmentManager().beginTransaction().add(R.id.songListFragment, viewPageFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.playToolbarFragment, playToolBarFragment).commitNow();
        playToolBarFragment.displayButtons();


        Toolbar toolbar = findViewById(R.id.titleToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.FOLDER_NAME, this.folderName));
        getMediaControllerAdapter().updateUiState();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
