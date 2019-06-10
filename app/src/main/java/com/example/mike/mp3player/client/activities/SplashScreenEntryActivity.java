package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.PermissionGranted;
import com.example.mike.mp3player.client.PermissionsProcessor;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import org.apache.commons.lang.exception.ExceptionUtils;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;

public class SplashScreenEntryActivity extends MediaBrowserCreatorActivityCompat
    implements  MediaBrowserConnectorCallback,
                PermissionGranted {

    private static final String LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY";
    private static final long WAIT_TIME = 3000L;
    private PermissionsProcessor permissionsProcessor;
    private volatile boolean splashScreenFinishedDisplaying = false;
    private volatile boolean permissionGranted = false;
    private Intent mainActivityIntent;
    public static final int APP_TERMINATED = 0x78;
    private Thread thread = new Thread(() -> init());

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.NOTIFY_ALL;
    }

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        initialiseView(R.layout.splash_screen);
        thread.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    private synchronized void splashScreenRun() {
    //    Log.i(LOG_TAG, "splashscreen run");
        try {
            wait(WAIT_TIME);
        } catch (InterruptedException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
            Thread.currentThread().interrupt();
        } finally {
            setSplashScreenFinishedDisplaying(true);
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

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        onProcessingComplete();
        Log.i(LOG_TAG, "hit on connected");

    }

    @Override// MediaBrowserConnectorCallback
    public void onConnectionSuspended() {    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() {
        Log.i(LOG_TAG, "connection failed");
    }

    private synchronized void onProcessingComplete() {
        Log.i(LOG_TAG, "processing complete");
        while (!isSplashScreenFinishedDisplaying() || !isPermissionGranted()) {
            try {
                wait(ONE_SECOND);
            } catch (InterruptedException ex) {
                String error = ExceptionUtils.getFullStackTrace(ex.fillInStackTrace());
                Log.e(LOG_TAG, error);
                Thread.currentThread().interrupt();
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_TERMINATED) {
            if (getMediaBrowserAdapter() != null) {
                getMediaBrowserAdapter().disconnect();
            }
            finish();
        }
    }

    @Override
    public void onPermissionGranted() {
        Log.i(LOG_TAG, "permission granted");
        setPermissionGranted(true);
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

    @VisibleForTesting
    public Thread getThread() {
        return thread;
    }

    @VisibleForTesting
    public boolean isSplashScreenFinishedDisplaying() {
        return splashScreenFinishedDisplaying;
    }

    @VisibleForTesting
    public boolean isPermissionGranted() {
        return permissionGranted;
    }
    @VisibleForTesting
    public void setSplashScreenFinishedDisplaying(boolean splashScreenFinishedDisplaying) {
        this.splashScreenFinishedDisplaying = splashScreenFinishedDisplaying;
    }
    @VisibleForTesting
    public void setPermissionGranted(boolean permissionGranted) {
        this.permissionGranted = permissionGranted;
    }
}
