package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.PermissionGranted;
import com.example.mike.mp3player.client.PermissionsProcessor;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryId;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.CATEGORY_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_RANGE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

public class SplashScreenEntryActivity extends AppCompatActivity
    implements  MediaBrowserConnectorCallback,
                MediaBrowserResponseListener,
                PermissionGranted {

    private static final String LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY";
    private PermissionsProcessor permissionsProcessor;
    private volatile boolean splashScreenFinishedDisplaying;
    private MediaBrowserAdapter mediaBrowserAdapter;
    private Intent mainActivityIntent;
    private int numberOfItemsToSubscribeTo;
    private int numberOfItemsReceived = 0;
    private static final long SPLASH_SCREEN_DISPLAY_TIME = 3000L;
    private static final int APP_TERMINATED = 0x78;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        Thread thread = new Thread(() -> init());
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.splash_screen);
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
       Log.i(LOG_TAG, "onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private synchronized void splashScreenRun() {
    //    Log.i(LOG_TAG, "splashscreen run");
        try {
            Thread.sleep(SPLASH_SCREEN_DISPLAY_TIME);
        } catch (InterruptedException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
        } finally {
            splashScreenFinishedDisplaying = true;
            notifyAll();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
     //   Log.i(LOG_TAG, "permission result");
        String permission = permissionsProcessor.getPermissionFromRequestCode(requestCode);
        if (null != permission) {
            if (permission.equals(WRITE_EXTERNAL_STORAGE)) {
                // Request for camera permission.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. Start camera preview Activity.
                    Log.i(LOG_TAG, "permission granted from request");
                    onPermissionGranted();
                }
            }
        } else {
            finish();
        }
    }

    public void initMediaBrowserService() {
        Log.i(LOG_TAG, "reset media browser service");
        mediaBrowserAdapter = new MediaBrowserAdapter(getApplicationContext(), this, getMainLooper());
        mediaBrowserAdapter.init();
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options, Context context) {
       Log.i(LOG_TAG, "children loaded: " + parentId);
       LibraryId parentLibraryId = (LibraryId) options.get(PARENT_ID);
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>();
        if (isRoot(parentLibraryId)) {
            childrenArrayList.addAll(children);
            numberOfItemsToSubscribeTo = childrenArrayList.size();
            for (MediaBrowserCompat.MediaItem item : childrenArrayList) {
                Category c = LibraryConstructor.getCategoryFromMediaItem(item);
                LibraryId libraryId = new LibraryId(c, c.name());
                mediaBrowserAdapter.subscribe(libraryId, DEFAULT_RANGE);
            }
            if (null == options) {
                options = new Bundle();
            }
            options.putParcelableArrayList(CATEGORY_ROOT_ID, childrenArrayList);
            options.putParcelable(MEDIA_SESSION, mediaBrowserAdapter.getMediaSessionToken());
            mainActivityIntent.putExtras(options);
            return;
        }

        if (parentLibraryId.getCategory() != null) {
            childrenArrayList.addAll(children);
            mainActivityIntent.putParcelableArrayListExtra(parentLibraryId.getCategory().name(), childrenArrayList);
            numberOfItemsReceived++;
        }
        if (numberOfItemsReceived >= numberOfItemsToSubscribeTo) {
            onProcessingComplete(mainActivityIntent);
        }
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        Log.i(LOG_TAG, "hit on connected");
        for (Category category : Category.values()) {
            mediaBrowserAdapter.registerListener(category, this);
        }
        LibraryId libraryId = new LibraryId(Category.ROOT, Category.ROOT.name());
        mediaBrowserAdapter.subscribe(libraryId, null);
    }

    @Override// MediaBrowserConnectorCallback
    public void onConnectionSuspended() {    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() { }

    private synchronized void onProcessingComplete(Intent mainActivityIntent) {
        for (Category category : Category.values()) {
            mediaBrowserAdapter.unregisterListener(category, this);
        }
 //       mediaBrowserAdapter.getmMediaBrowser().disconnect();
        Log.i(LOG_TAG, "processing complete");
        while (!splashScreenFinishedDisplaying) {
            try {
                wait(ONE_SECOND);
            } catch (InterruptedException ex) {
                String error = ExceptionUtils.getFullStackTrace(ex.fillInStackTrace());
                Log.e(LOG_TAG, error);
            }
        }
        startMainActivity(mainActivityIntent);
    }

    private void startMainActivity(Intent mainActivityIntent) {
        Log.i(LOG_TAG, "start main activity");
        startActivityForResult(mainActivityIntent, APP_TERMINATED);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == APP_TERMINATED) {
            mediaBrowserAdapter.disconnect();
            finish();
        }
    }

    @Override
    public void onPermissionGranted() {
        Log.i(LOG_TAG, "permission granted");
        Runnable r = new Thread(() -> initMediaBrowserService());
        runOnUiThread(r);
    }

    public void init() {
        Log.i(LOG_TAG, "reset");
        permissionsProcessor = new PermissionsProcessor(this, this);
        permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
        Thread splashScreenWaitThread = new Thread(() -> splashScreenRun());
        splashScreenWaitThread.start();
    }

    private boolean isRoot(LibraryId id) {
        return mediaBrowserAdapter.getRootId().equals(id.getId());
    }
}
