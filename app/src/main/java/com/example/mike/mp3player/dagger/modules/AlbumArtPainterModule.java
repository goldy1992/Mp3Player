package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Module;

@Module
public class AlbumArtPainterModule {

    @FragmentScope
    public AlbumArtPainter provideAlbumArtPainter(Context context) {
        return new AlbumArtPainter(context);
    }
}
