package com.github.goldy1992.mp3player.dagger.modules;

import android.os.Handler;

import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.views.adapters.SearchResultAdapter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchResultAdapterModule {

    @Provides
    public SearchResultAdapter provideSearchResultAdapter(AlbumArtPainter albumArtPainter, @Named("main") Handler mainHandler ) {
        return new SearchResultAdapter(albumArtPainter, mainHandler);
    }
}