package com.github.goldy1992.mp3player.dagger.modules;

import com.github.goldy1992.mp3player.client.AlbumArtPainter;

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
