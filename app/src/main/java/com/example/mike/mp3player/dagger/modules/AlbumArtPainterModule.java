package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import javax.inject.Named;

import dagger.Module;

@Module
public class AlbumArtPainterModule {

    @FragmentScope
    public AlbumArtPainter provideAlbumArtPainter(Context context, @Named("main") Handler handler) {
        return new AlbumArtPainter(context, handler);
    }
}
