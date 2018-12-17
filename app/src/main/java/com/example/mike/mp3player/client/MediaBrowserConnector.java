package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.MySubscriptionCallback;
import com.example.mike.mp3player.service.MediaPlaybackService;

public class MediaBrowserConnector {

    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MySubscriptionCallback mySubscriptionCallback;
    private Context context;
    private final MainActivity activity;
    private MediaSessionCompat.Token mediaSessionToken;

    public MediaBrowserConnector(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void init(MediaSessionCompat.Token token) {
            mConnectionCallbacks = new MyConnectionCallback(this);
            // Create MediaBrowserServiceCompat
            mMediaBrowser = new MediaBrowserCompat(activity.getApplicationContext(),
                    new ComponentName(activity, MediaPlaybackService.class),
                    mConnectionCallbacks,
                    null);

        this.mySubscriptionCallback = activity instanceof MainActivity ?
                new MySubscriptionCallback((MainActivity) activity) : null;
        if (token == null ) {
            getmMediaBrowser().connect();
        } else{
            onConnected(token);
        }
    }

    public void onConnected(MediaSessionCompat.Token token) {
        if (token == null) {
            token = mMediaBrowser.getSessionToken();
        }
        this.mediaSessionToken = token;
        mMediaBrowser.subscribe(mMediaBrowser.getRoot(), mySubscriptionCallback);
        activity.onMediaBrowserServiceConnected(token);
    }

    public MediaBrowserCompat getmMediaBrowser() {
        return mMediaBrowser;
    }

    public void disconnect() {
        getmMediaBrowser().disconnect();
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mediaSessionToken;
    }


}
