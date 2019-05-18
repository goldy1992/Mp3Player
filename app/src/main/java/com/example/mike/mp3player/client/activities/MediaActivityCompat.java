package com.example.mike.mp3player.client.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.MenuItem;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.mike.mp3player.commons.Constants.THEME;

public abstract class MediaActivityCompat extends AppCompatActivity  {

    private final String WORKER_ID = getClass().toString();
    private MediaControllerAdapter mediaControllerAdapter;
    private static final String LOG_TAG = "MEDIA_ACTIVITY_COMPAT";
    private HandlerThread worker;

    abstract boolean initialiseView(@LayoutRes int layoutId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(THEME, MODE_PRIVATE);
        setTheme(settings.getInt(THEME, R.style.AppTheme_Blue));
        worker = new HandlerThread(WORKER_ID);
        getWorker().start();
    }

    public void initialiseMediaControllerAdapter(MediaSessionCompat.Token token) {
        if (null == this.mediaControllerAdapter) {
            this.mediaControllerAdapter = new MediaControllerAdapter(this, token, worker.getLooper());
        } else {
            this.mediaControllerAdapter.setMediaToken(token);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getWorker().quitSafely();
    }

    public final MediaControllerAdapter getMediaControllerAdapter() {
        return mediaControllerAdapter;
    }
    public final void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    public HandlerThread getWorker() {
        return worker;
    }
}
