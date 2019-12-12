package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides


@Module
class MockAlbumArtPainterModule {
    @Provides
    fun provideAlbumArtPainter(): AlbumArtPainter {
        return mock<AlbumArtPainter>() as AlbumArtPainter
    }
}