package com.github.goldy1992.mp3player.dagger.modules;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback;
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaBrowserCompatModule {

    @Provides
    public MySearchCallback provideMySearchCallback() {
        return new MySearchCallback();
    }


    @Provides
    public MediaBrowserCompat provideMediaBrowserCompat(Context context, ComponentName componentName,
                                                        MyConnectionCallback myConnectionCallback) {
        return new MediaBrowserCompat(context, componentName, myConnectionCallback, null);
    }

}
