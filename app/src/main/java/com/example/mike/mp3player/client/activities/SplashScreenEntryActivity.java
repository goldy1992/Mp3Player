package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.StartupProcessor;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_SERVICE_DATA;

public class SplashScreenEntryActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY";
    private StartupProcessor startupProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.splash_screen);
        startupProcessor = new StartupProcessor(this);
        Object[] objects = new Object[0];
        startupProcessor.execute(objects);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void startMainActivity(Intent mainActivityIntent) {
        startActivity(mainActivityIntent);
        finish();
    }

    @Override // ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        startupProcessor.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
