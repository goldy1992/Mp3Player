package com.github.goldy1992.mp3player.client.dagger.components;

import com.github.goldy1992.mp3player.client.activities.SearchResultActivity;
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface SearchResultActivitySubComponent {

    void inject(SearchResultActivity searchResultActivity);
}
