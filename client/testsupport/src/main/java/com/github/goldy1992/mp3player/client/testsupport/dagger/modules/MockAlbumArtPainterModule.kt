package com.github.goldy1992.mp3player.client.testsupport.dagger.modules

import com.github.goldy1992.mp3player.client.AlbumArtPainter
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
class MockAlbumArtPainterModule {
    @Provides
    fun provideAlbumArtPainter(): AlbumArtPainter {
        return Mockito.mock(AlbumArtPainter::class.java)
    }
}