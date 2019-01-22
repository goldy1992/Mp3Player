package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserConnector;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.PermissionGranted;
import com.example.mike.mp3player.client.PermissionsProcessor;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SERVICE_DATA;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;

public class SplashScreenEntryActivity extends AppCompatActivity implements MediaBrowserConnectorCallback, PermissionGranted {
    private static final String LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY";

    private PermissionsProcessor permissionsProcessor;
    private volatile boolean splashScreenFinishedDisplaying;
    private MediaBrowserConnector mediaBrowserConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.splash_screen);
      //  Log.i(LOG_TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
       // Log.i(LOG_TAG, "onresume");
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
            Thread.sleep(5000L);
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
                    onPermissionGranted();
                }
            }
        } else {
            finish();
        }
    }

    public void initMediaBrowserService() {
       // Log.i(LOG_TAG, "init media browser service");
        mediaBrowserConnector = new MediaBrowserConnector(getApplicationContext(), this);
        mediaBrowserConnector.init(null);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
       // Log.i(LOG_TAG, "children loaded");
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
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

    private synchronized void onProcessingComplete(Intent mainActivityIntent) {
        //Log.i(LOG_TAG, "processing complete");
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
       // Log.i(LOG_TAG, "start main activity");
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void onPermissionGranted() {
        Runnable r = new Thread(() -> initMediaBrowserService());
        runOnUiThread(r);
    }

    public void init() {
        //Log.i(LOG_TAG, "init");
        permissionsProcessor = new PermissionsProcessor(this, this);
        permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
        Thread splashScreenWaitThread = new Thread(() -> splashScreenRun());
        splashScreenWaitThread.start();
}
}
