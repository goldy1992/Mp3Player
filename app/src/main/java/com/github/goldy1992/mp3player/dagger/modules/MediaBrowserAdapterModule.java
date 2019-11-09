package com.github.goldy1992.mp3player.dagger.modules;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter;
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback;
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback;
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserAdapterModule {

    @ComponentScope
    @Provides
    public MediaBrowserAdapter provideMediaBrowserAdapter(MediaBrowserCompat mediaBrowser,
                                                          MyConnectionCallback myConnectionCallback,
                                                          MediaIdSubscriptionCallback mySubscriptionCallback,
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
    public MediaBrowserCompat provideMediaBrowserCompat(Context context, ComponentName componentName,
                                                        MyConnectionCallback myConnectionCallback) {
        return new MediaBrowserCompat(context, componentName, myConnectionCallback, null);
    }

    @Provides
    public MediaIdSubscriptionCallback provideGenericSubscriptionCallback(
            @Named("worker") Handler handler) {
               return new MediaIdSubscriptionCallback(handler);

    }
}
