package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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

public class MainActivity extends MediaActivityCompat
    implements  MediaPlayerActvityRequester,
                MediaBrowserConnectorCallback {

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final int READ_REQUEST_CODE = 42;

    private MediaBrowserAdapter mediaBrowserAdapter;
    private MediaControllerAdapter mediaControllerAdapter;

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
        if (mediaControllerAdapter != null) {
            setPlaybackState(mediaControllerAdapter.getCurrentPlaybackState());
        }
        // If it is null it will initialised when the MediaBrowserAdapter has connected
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initMediaBrowserService() {
        mediaBrowserAdapter = new MediaBrowserAdapter(getApplicationContext(), this);
        mediaBrowserAdapter.init();
    }

    private Intent createMediaPlayerActivityIntent() {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
        intent.putExtra(MEDIA_SESSION, mediaBrowserAdapter.getMediaSessionToken());
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

    @Override // MediaActivityCompat
    public void setMetaData(MediaMetadataCompat metadata) { /* no need to update meta data in this class */ }

    @Override // MediaActivityCompat
    public void setPlaybackState(PlaybackStateCompat state) {
        rootFragment.setPlaybackState(state);
    }

    @Override // MediaActivityCompat
    public MediaControllerAdapter getMediaControllerAdapter() {
        return mediaControllerAdapter;
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        this.mediaControllerAdapter = new MediaControllerAdapter(this, mediaBrowserAdapter.getMediaSessionToken());
        this.mediaControllerAdapter.init();
        setContentView(R.layout.activity_main);
        this.rootFragment = (MainActivityRootFragment) getSupportFragmentManager().findFragmentById(R.id.mainActivityRootFragment);
        this.rootFragment.init(inputMethodManager, this, mediaBrowserAdapter, mediaControllerAdapter, menuItems);
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionSuspended() { /* TODO: implement onConnectionSuspended */   }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() {  /* TODO: implement onConnectionFailed */  }

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