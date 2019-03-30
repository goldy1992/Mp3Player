package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.PermissionGranted;
import com.example.mike.mp3player.client.PermissionsProcessor;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.commons.library.LibraryResponse;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;
import static com.example.mike.mp3player.commons.Constants.PRE_SUBSCRIBED_MEDIA_ITEMS;
import static com.example.mike.mp3player.commons.Constants.RESPONSE_OBJECT;

public class SplashScreenEntryActivity extends MediaSubscriberActivityCompat
    implements  MediaBrowserConnectorCallback,
                MediaBrowserResponseListener,
                PermissionGranted {

    private static final String LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY";
    private PermissionsProcessor permissionsProcessor;
    private volatile boolean splashScreenFinishedDisplaying;
    private Intent mainActivityIntent;
    private int numberOfItemsToSubscribeTo;
    private int numberOfItemsReceived = 0;
    private static final long SPLASH_SCREEN_DISPLAY_TIME = 3000L;
    private static final int APP_TERMINATED = 0x78;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseView(R.layout.splash_screen);
        mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        preSubscribedItems = new HashMap<>();
        Thread thread = new Thread(() -> init());
        thread.start();

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



    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options, Context context) {
       Log.i(LOG_TAG, "children loaded: " + parentId);
        LibraryResponse parentLibraryRequest = (LibraryResponse) options.get(RESPONSE_OBJECT);

       if (null == parentLibraryRequest) {
           return;
       }

        if (isRoot(parentLibraryRequest)) {
            numberOfItemsToSubscribeTo = children.size();

            for (MediaBrowserCompat.MediaItem item : children) {
                Category c = LibraryConstructor.getCategoryFromMediaItem(item);
                LibraryRequest libraryRequest = new LibraryRequest(c, c.name());
                getMediaBrowserAdapter().subscribe(libraryRequest);
            }
            return;
        }

        if (parentLibraryRequest.getCategory() != null) {
            preSubscribedItems.put(parentLibraryRequest, children);
            numberOfItemsReceived++;
        }
        if (numberOfItemsReceived >= numberOfItemsToSubscribeTo) {
            onProcessingComplete(mainActivityIntent);
        }
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        super.onConnected();
        Log.i(LOG_TAG, "hit on connected");
        for (Category category : Category.values()) {
            getMediaBrowserAdapter().registerListener(category, this);
        }
        LibraryRequest libraryRequest = new LibraryRequest(Category.ROOT, Category.ROOT.name());
        getMediaBrowserAdapter().subscribe(libraryRequest);
    }

    @Override// MediaBrowserConnectorCallback
    public void onConnectionSuspended() {    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() { }

    private synchronized void onProcessingComplete(Intent mainActivityIntent) {
        for (Category category : Category.values()) {
            getMediaBrowserAdapter().unregisterListener(category, this);
        }
        mainActivityIntent.putExtra(PRE_SUBSCRIBED_MEDIA_ITEMS, preSubscribedItems);
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
            getMediaBrowserAdapter().disconnect();
            finish();
        }
    }

    @Override
    public void onPermissionGranted() {
        Log.i(LOG_TAG, "permission granted");
        Runnable r = new Thread(() -> initMediaBrowserService(getSubscriptionType()));
        runOnUiThread(r);
    }

    public void init() {
        Log.i(LOG_TAG, "reset");
        permissionsProcessor = new PermissionsProcessor(this, this);
        permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
        Thread splashScreenWaitThread = new Thread(() -> splashScreenRun());
        splashScreenWaitThread.start();
    }

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.NOTIFY_ALL;
    }

    private boolean isRoot(LibraryObject libraryObject) {
        return getMediaBrowserAdapter().getRootId().equals(libraryObject.getId());
    }
}
