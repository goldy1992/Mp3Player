package com.github.goldy1992.mp3player.client.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter;
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.client.PermissionGranted;
import com.github.goldy1992.mp3player.client.PermissionsProcessor;
import com.github.goldy1992.mp3player.dagger.components.DaggerMediaActivityCompatComponent;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;
import com.github.goldy1992.mp3player.dagger.components.SplashScreenEntryActivityComponent;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.github.goldy1992.mp3player.commons.Constants.ONE_SECOND;

public class SplashScreenEntryActivity extends AppCompatActivity
    implements  MediaBrowserConnectorCallback,
                PermissionGranted {

    private MediaBrowserAdapter mediaBrowserAdapter;
    private static final String LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY";
    private static final long WAIT_TIME = 3000L;
    private PermissionsProcessor permissionsProcessor;
    private volatile boolean splashScreenFinishedDisplaying = false;
    private volatile boolean permissionGranted = false;
    private Intent mainActivityIntent;
    public static final int APP_TERMINATED = 0x78;
    private Thread thread = new Thread(() -> init());

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }

        initialiseDependencies();
        super.onCreate(savedInstanceState);
        // TODO: have this injected so that a test implementation can be provided
        mainActivityIntent = new Intent(getApplicationContext(), getMainActivityClass());
        setContentView(R.layout.splash_screen);
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
            Log.e(LOG_TAG, ExceptionUtils.getMessage(ex.fillInStackTrace()));
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
        boolean permissionIsGranted = false;
        if (permissions.length > 0 && grantResults.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (WRITE_EXTERNAL_STORAGE.equals(permissions[i]) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionIsGranted = true;
                }
            }
        }
        if (permissionIsGranted) {
            onPermissionGranted();
        } else {
            Toast.makeText(getApplicationContext(), "Permission denied, please enable Storage permissions in settings in order to uer the app", Toast.LENGTH_LONG).show();
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
                String error = ExceptionUtils.getMessage(ex.fillInStackTrace());
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
            if (mediaBrowserAdapter != null) {
                mediaBrowserAdapter.disconnect();
            }
            finish();
        }
    }

    @Override
    public void onPermissionGranted() {
        Log.i(LOG_TAG, "permission granted");
        setPermissionGranted(true);
        Runnable r = new Thread(() -> mediaBrowserAdapter.connect());
        runOnUiThread(r);
    }

    public void init() {
        Log.i(LOG_TAG, "reset");
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

    @Inject
    public void setPermissionsProcessor(PermissionsProcessor permissionsProcessor) {
        this.permissionsProcessor = permissionsProcessor;
    }

    @Inject
    public void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    private void initialiseDependencies() {
        MediaActivityCompatComponent component = DaggerMediaActivityCompatComponent
            .factory()
            .create(getApplicationContext(),"SPSH_SCRN_ACTVTY_WRKR", this);
        SplashScreenEntryActivityComponent splashScreenEntryActivityComponent =
                component.splashScreenEntryActivity().create(this, this);
        splashScreenEntryActivityComponent.inject(this);
    }

    Class<?> getMainActivityClass() {
        return MainActivityInjector.class;
    }
}
