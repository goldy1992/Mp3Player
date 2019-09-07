package com.example.mike.mp3player.dagger.modules;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.callbacks.connection.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.search.MySearchCallback;
import com.example.mike.mp3player.client.callbacks.subscription.GenericSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.scopes.ComponentScope;
import com.example.mike.mp3player.service.MediaPlaybackServiceInjector;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserAdapterModule {

    @ComponentScope
    @Provides
    public MediaBrowserAdapter provideMediaBrowserAdapter(MediaBrowserCompat mediaBrowser,
                                                          MyConnectionCallback myConnectionCallback,
                                                          GenericSubscriptionCallback mySubscriptionCallback,
                                                          MySearchCallback mySearchCallback) {
        return new MediaBrowserAdapter(mediaBrowser, myConnectionCallback, mySubscriptionCallback, mySearchCallback);
    }

    @Provides
    public MySearchCallback provideMySearchCallback() {
        return new MySearchCallback();
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
            return new MediaIdSubscriptionCallback(handler);
        }
        return null;
    }
}
