package com.example.mike.mp3player.client;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.activities.SplashScreenEntryActivity;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SERVICE_DATA;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;

public class StartupProcessor extends AsyncTask implements MediaBrowserConnectorCallback, PermissionGranted {
    private SplashScreenEntryActivity parent;
    private MediaBrowserConnector mediaBrowserConnector;
    private Thread splashScreenWaitThread;
    private PermissionsProcessor permissionsProcessor;
    private volatile boolean splashScreenFinishedDisplaying = false;
    private static final String LOG_TAG = "STARTUP_PROCESSOR";

    public StartupProcessor(SplashScreenEntryActivity splashScreenEntryActivity) {
        this.parent = splashScreenEntryActivity;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //Looper.prepare();
        splashScreenWaitThread = new Thread(() -> splashScreenRun());
        splashScreenWaitThread.start();
        permissionsProcessor = new PermissionsProcessor(parent, this);
        permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
        return null;
    }

    private synchronized void splashScreenRun() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
        }
        splashScreenFinishedDisplaying = true;
        notifyAll();
    }

    private synchronized void onProcessingComplete(Intent mainActivityIntent) {
        while (!splashScreenFinishedDisplaying) {
            try {
                wait(ONE_SECOND);
            } catch (InterruptedException ex) {
                String error = ExceptionUtils.getFullStackTrace(ex.fillInStackTrace());
                Log.e(LOG_TAG, error);
            }
        }
        parent.startMainActivity(mainActivityIntent);
    }

    private void initMediaBrowserService() {
        mediaBrowserConnector = new MediaBrowserConnector(parent.getApplicationContext(), this);
        mediaBrowserConnector.init(null);
    }

    @Override
    public synchronized void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
        Log.i(LOG_TAG, "children loaded");
        Intent mainActivityIntent = new Intent(parent.getApplicationContext(), MainActivity.class);
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>();
        childrenArrayList.addAll(children);
        if (null == options) {
            options = new Bundle();
        }
        options.putParcelableArrayList(MEDIA_SERVICE_DATA, childrenArrayList);
        options.putParcelable(MEDIA_SESSION, mediaBrowserConnector.getMediaSessionToken());
        mainActivityIntent.putExtras(options);
        onProcessingComplete(mainActivityIntent);
    }

      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        String permission = permissionsProcessor.getPermissionFromRequestCode(requestCode);

        if (null != permission) {
            if (permission.equals(WRITE_EXTERNAL_STORAGE)) {
                // Request for camera permission.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. Start camera preview Activity.
                    onPermissionGranted();
                }
            }
        } else {
            parent.finish();
        }
    }

    @Override
    public void onPermissionGranted() {
        Log.i(LOG_TAG, "permission granted");
        parent.runOnUiThread(() -> initMediaBrowserService());
    }
}
