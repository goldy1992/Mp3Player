package com.github.goldy1992.mp3player.dagger.components;

import com.github.goldy1992.mp3player.client.activities.SearchResultActivity;
import com.github.goldy1992.mp3player.dagger.modules.SearchResultAdapterModule;
import com.github.goldy1992.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(
        modules = {
                SearchResultAdapterModule.class}
)
public interface SearchResultActivitySubComponent {

    void inject(SearchResultActivity searchResultActivity);
}
