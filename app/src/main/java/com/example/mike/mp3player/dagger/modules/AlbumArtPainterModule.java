package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mike.mp3player.client.AlbumArtPainter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumArtPainterModule {

    @Provides
    public AlbumArtPainter provideAlbumArtPainter(Context context, @Named("main") Handler handler) {
        RequestManager requestManager = Glide.with(context);
        return new AlbumArtPainter(requestManager, handler);
    }
}
