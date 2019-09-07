package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.views.adapters.SearchResultAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchResultAdapterModule {

    @Provides
    public SearchResultAdapter provideSearchResultAdapter(AlbumArtPainter albumArtPainter) {
        return new SearchResultAdapter(albumArtPainter);
    }
}
