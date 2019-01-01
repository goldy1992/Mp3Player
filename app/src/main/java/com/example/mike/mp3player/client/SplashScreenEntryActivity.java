package com.example.mike.mp3player.client;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.mike.mp3player.R;

import org.apache.commons.lang.exception.ExceptionUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.text.PrecomputedTextCompat;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;

public class SplashScreenEntryActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY";
    private Thread splashScreenWaitThread;
    private PermissionsProcessor permissionsProcessor;
    private volatile boolean splashScreenFinishedDisplaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionsProcessor= new PermissionsProcessor(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.splash_screen);
        BackgroundProcessing backgroundProcessing = new BackgroundProcessing();
        backgroundProcessing.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public synchronized void init() {
        while (!splashScreenFinishedDisplaying) {
            try {
            wait(ONE_SECOND);
            } catch (InterruptedException ex) {
                String error = ExceptionUtils.getFullStackTrace(ex.fillInStackTrace());
                Log.e(LOG_TAG, error);
            }
        }

        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }


    @Override // ActivityCompat.OnRequestPermissionsResultCallback
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
                    return;
                }
            }
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
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


    private class BackgroundProcessing extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            splashScreenWaitThread = new Thread(() -> splashScreenRun());
            splashScreenWaitThread.start();
            permissionsProcessor.requestPermission(WRITE_EXTERNAL_STORAGE);
            return null;
        }
    } // BackgroundProcessing class


}
