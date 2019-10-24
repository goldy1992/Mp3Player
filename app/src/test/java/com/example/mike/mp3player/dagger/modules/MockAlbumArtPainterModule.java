package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.AlbumArtPainter;

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
