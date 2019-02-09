package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.views.fragments.MainActivityRootFragment;
import com.example.mike.mp3player.commons.library.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mike.mp3player.commons.Constants.CATEGORY_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.MediaItemUtils.getMediaId;

public class MainActivity extends MediaActivityCompat implements MediaPlayerActvityRequester {

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final int READ_REQUEST_CODE = 42;
    private MainActivityRootFragment rootFragment;
    private InputMethodManager inputMethodManager;
    private Map<MediaItem, List<MediaItem>> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.menuItems = initMenuItems(getIntent().getExtras());
        initMediaBrowserService();
        this.inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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



    private Intent createMediaPlayerActivityIntent() {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra(MEDIA_SESSION, getMediaBrowserAdapter().getMediaSessionToken());
        return intent;
    }

    @Override
    public void playSelectedSong(String songId) {
        Intent intent = createMediaPlayerActivityIntent();
        intent.putExtra(MEDIA_ID, songId);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void goToMediaPlayerActivity() {
        Intent intent = createMediaPlayerActivityIntent();
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selected = rootFragment.getMainFrameFragment().onOptionsItemSelected(item);
        return selected || super.onOptionsItemSelected(item);
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        setMediaControllerAdapter(new MediaControllerAdapter(this, getMediaBrowserAdapter().getMediaSessionToken()));
        getMediaControllerAdapter().init();
        setContentView(R.layout.activity_main);
        this.rootFragment = (MainActivityRootFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivityRootFragment);
        this.rootFragment.init(inputMethodManager, this, getMediaBrowserAdapter(), getMediaControllerAdapter(), menuItems);
    }

    private Map<MediaItem, List<MediaItem>> initMenuItems(Bundle extras) {
        Map<MediaItem, List<MediaItem>> toReturn = new HashMap<>();

        List<MediaItem> root = extras.getParcelableArrayList(CATEGORY_ROOT_ID);
        for (MediaItem item : root) {
            List<MediaItem> children = extras.getParcelableArrayList(Category.valueOf(getMediaId(item)).name());
            if (children != null) {
                toReturn.put(item, children);
            }
        }
        return toReturn;
    }
}