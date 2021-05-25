package com.github.goldy1992.mp3player.client.dagger.modules

import android.support.v4.media.MediaBrowserCompat.MediaItem
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@InstallIn(ActivityComponent::class)
@Module
class MockAlbumArtPainterModule {

    @ActivityScoped
    @Provides
    fun provideAlbumArtPainter(): AlbumArtPainter {
        val albumArtPainter : AlbumArtPainter = mock<AlbumArtPainter>()
        val preloader :  RecyclerViewPreloader<MediaItem> = mock<RecyclerViewPreloader<MediaItem>>()
        whenever(albumArtPainter.createPreloader(any())).thenReturn(preloader)
        return albumArtPainter
    }
}