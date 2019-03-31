package com.example.mike.mp3player.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.commons.library.LibraryResponse;
import com.example.mike.mp3player.commons.library.PreSubscribedMediaItemsHolder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.PRE_SUBSCRIBED_MEDIA_ITEMS;

public abstract class MediaSubscriberActivityCompat extends MediaActivityCompat {

    PreSubscribedMediaItemsHolder preSubscribedItems;
    abstract SubscriptionType getSubscriptionType();

    @SuppressWarnings("unchecked")
    private PreSubscribedMediaItemsHolder initMenuItems(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                return bundle.getParcelable(PRE_SUBSCRIBED_MEDIA_ITEMS);
            }
        }
        return new PreSubscribedMediaItemsHolder();
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
            for (LibraryResponse m : getPreSubscribedItems().getKeySet()) {
                if (m.getResultSize() < m.getTotalNumberOfChildren()) {
                    m.setNext();
                    getMediaBrowserAdapter().subscribe(m);
                }
            }
        }
    }

    public PreSubscribedMediaItemsHolder getPreSubscribedItems() {
        return preSubscribedItems;
    }

}
