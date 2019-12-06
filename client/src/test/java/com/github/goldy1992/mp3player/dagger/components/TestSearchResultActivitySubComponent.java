package com.github.goldy1992.mp3player.dagger.components;

import com.github.goldy1992.mp3player.client.activities.SearchResultActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {})
public interface TestSearchResultActivitySubComponent extends TestMediaActivityCompatComponent  {

    void inject(SearchResultActivity searchResultActivityTest);
}