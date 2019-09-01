package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.activities.SearchResultActivityInjector;
import com.example.mike.mp3player.dagger.modules.SearchResultAdapterModule;
import com.example.mike.mp3player.dagger.scopes.ComponentScope;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(
        modules = {
                SearchResultAdapterModule.class}
)
public interface SearchResultActivitySubComponent {

    void inject(SearchResultActivityInjector searchResultActivityInjector);
}
