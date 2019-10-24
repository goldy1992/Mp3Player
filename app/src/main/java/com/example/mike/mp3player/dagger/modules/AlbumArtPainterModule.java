package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mike.mp3player.client.AlbumArtPainter;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumArtPainterModule {

    @Provides
    public AlbumArtPainter provideAlbumArtPainter(Context context) {
        RequestManager requestManager = Glide.with(context);
        return new AlbumArtPainter(context, requestManager);
    }
}
