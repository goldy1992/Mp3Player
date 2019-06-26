package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserAdapterModule {
    @Provides
    @Singleton
    public MediaBrowserAdapter provideMediaBrowserAdapter(Context context, Handler handler,
          SubscriptionType subscriptionType, MyConnectionCallback myConnectionCallback) {
        return new MediaBrowserAdapter(context, handler,
                subscriptionType, myConnectionCallback);
    }

    @Provides
    @Singleton
    public MyConnectionCallback provideMyConnectionCallback() {
        return new MyConnectionCallback();
    }
}
