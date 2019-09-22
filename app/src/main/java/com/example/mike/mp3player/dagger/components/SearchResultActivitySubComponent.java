package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.client.activities.SearchResultActivity;
import com.example.mike.mp3player.dagger.modules.SearchResultAdapterModule;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(
        modules = {
                SearchResultAdapterModule.class}
)
public interface SearchResultActivitySubComponent {

    void inject(SearchResultActivity searchResultActivity);
}
