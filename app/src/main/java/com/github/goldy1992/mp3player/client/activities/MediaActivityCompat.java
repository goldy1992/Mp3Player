package com.github.goldy1992.mp3player.client.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter;
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.commons.Constants.THEME;

public abstract class MediaActivityCompat extends AppCompatActivity implements MediaBrowserConnectorCallback {
    private static final String LOG_TAG = "MEDIA_ACTIVITY_COMPAT";
    /** MediaBrowserAdapter */
    protected MediaBrowserAdapter mediaBrowserAdapter;
    /** MediaControllerAdapter */
    protected MediaControllerAdapter mediaControllerAdapter;
    /** Dependencies **/
    private MediaActivityCompatComponent mediaActivityCompatComponent;
    /** Thread used to deal with none UI tasks */
    private HandlerThread worker;

    /** @return The unique name of the HandlerThread used by the activity */
    abstract String getWorkerId();
    /** Utility method used to initialise the dependencies set up by Dagger2. DOES NOT need to be
     * called by sub class */
    abstract void initialiseDependencies();

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        this.mediaControllerAdapter.setMediaToken(mediaBrowserAdapter.getMediaSessionToken());
    }

    public final MediaBrowserAdapter getMediaBrowserAdapter() {
        return mediaBrowserAdapter;
    }

    @Inject
    public final void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaControllerAdapter.disconnect();
        mediaBrowserAdapter.disconnect();
        worker.quitSafely();
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionSuspended() { /* TODO: implement onConnectionSuspended */
        Log.i(LOG_TAG, "connection suspended");}

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() {  /* TODO: implement onConnectionFailed */
        Log.i(LOG_TAG, "connection failed");
    }

    abstract boolean initialiseView(@LayoutRes int layoutId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(THEME, MODE_PRIVATE);
        setTheme(settings.getInt(THEME, R.style.AppTheme_Blue));
        mediaBrowserAdapter.init();
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

    public final MediaControllerAdapter getMediaControllerAdapter() { return mediaControllerAdapter; }

    @Inject
    public final void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @Inject
    public final void setWorker(HandlerThread thread) {
        this.worker = thread;
    }

    public void setMediaActivityCompatComponent(MediaActivityCompatComponent mediaActivityCompatComponent) {
        this.mediaActivityCompatComponent = mediaActivityCompatComponent;
    }
    /** @return the mediaActivityCompatComponent **/
    public MediaActivityCompatComponent getMediaActivityCompatComponent() {
        return mediaActivityCompatComponent;
    }
}
