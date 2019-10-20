package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;

import com.example.mike.mp3player.client.AlbumArtPainter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockAlbumArtPainterModule {


    @Provides
    public AlbumArtPainter provideAlbumArtPainter() {
        return mock(AlbumArtPainter.class);
    }
}
