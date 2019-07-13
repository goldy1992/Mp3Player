package com.example.mike.mp3player.dagger.modules;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.CategorySubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.GenericSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.NotifyAllSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.scopes.ComponentScope;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MediaPlaybackServiceInjector;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserAdapterModule {

    @ComponentScope
    @Provides
    public MediaBrowserAdapter provideMediaBrowserAdapter(MediaBrowserCompat mediaBrowser,
                                                          MyConnectionCallback myConnectionCallback,
                                                          GenericSubscriptionCallback mySubscriptionCallback) {
        return new MediaBrowserAdapter(mediaBrowser, myConnectionCallback, mySubscriptionCallback);
    }

    @Provides
    public MyConnectionCallback provideMyConnectionCallback(MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        return new MyConnectionCallback(mediaBrowserConnectorCallback);
    }

    @Provides
    public ComponentName provideComponentName(Context context) {
        return new ComponentName(context, MediaPlaybackServiceInjector.class);
    }

    @Provides
    public MediaBrowserCompat provideMediaBrowserCompat(Context context, ComponentName componentName,
                                                        MyConnectionCallback myConnectionCallback, Handler handler) {
        return new MediaBrowserCompat(context, componentName, myConnectionCallback, null);
    }

    @Provides
    public GenericSubscriptionCallback provideGenericSubscriptionCallback(
            SubscriptionType subscriptionType,
            Handler handler) {
        if (null != subscriptionType) {
            switch (subscriptionType) {
                case CATEGORY: return new CategorySubscriptionCallback(handler);
                case MEDIA_ID: return new MediaIdSubscriptionCallback(handler);
                default: return new NotifyAllSubscriptionCallback(handler);
            }
        }
        return null;
    }
}
