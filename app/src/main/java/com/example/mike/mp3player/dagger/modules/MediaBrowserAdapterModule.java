package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Looper;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserAdapterModule {
    @Provides
    @Singleton
    public MediaBrowserAdapter provideMediaBrowserAdapter(Context context, MediaBrowserConnectorCallback mediaBrowserConnectorCallback, Looper looper, SubscriptionType subscriptionType) {
        return new MediaBrowserAdapter(context, mediaBrowserConnectorCallback, looper, subscriptionType);
    }
}
