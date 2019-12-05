package com.github.goldy1992.mp3player.client.dagger.modules;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserCompatModule {


    @Provides
    public MediaBrowserCompat provideMediaBrowserCompat(Context context, ComponentName componentName,
                                                        MyConnectionCallback myConnectionCallback) {
        return new MediaBrowserCompat(context, componentName, myConnectionCallback, null);
    }

}
