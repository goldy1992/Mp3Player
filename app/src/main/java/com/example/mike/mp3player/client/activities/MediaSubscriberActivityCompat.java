package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.example.mike.mp3player.commons.Constants.PRE_SUBSCRIBED_MEDIA_ITEMS;

public abstract class MediaSubscriberActivityCompat extends MediaActivityCompat {

    private Map<LibraryRequest, List<MediaBrowserCompat.MediaItem>> preSubscribedItems;

    abstract SubscriptionType getSubscriptionType();

    @SuppressWarnings("unchecked")
    private Map<LibraryRequest, List<MediaBrowserCompat.MediaItem>> initMenuItems(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                Serializable serializable = bundle.getSerializable(PRE_SUBSCRIBED_MEDIA_ITEMS);
                if (serializable != null) {
                    return (Map<LibraryRequest, List<MediaBrowserCompat.MediaItem>>) serializable;
                }
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.preSubscribedItems = initMenuItems(getIntent());
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        setMediaControllerAdapter(new MediaControllerAdapter(this, getMediaBrowserAdapter().getMediaSessionToken(), getWorker().getLooper()));
        getMediaControllerAdapter().init();
        //setContentView(R.layout.activity_main);

        if (null != getPreSubscribedItems()) {
            for (LibraryRequest m : getPreSubscribedItems().keySet()) {
                if (m.getResultSize() < m.getTotalNumberOfChildren()) {
                    m.setNext();
                    getMediaBrowserAdapter().subscribe(m);
                }
            }
        }
    }

    public Map<LibraryRequest, List<MediaBrowserCompat.MediaItem>> getPreSubscribedItems() {
        return preSubscribedItems;
    }
}
