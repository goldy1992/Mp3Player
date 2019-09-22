package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.client.activities.SearchResultActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {})
public interface TestSearchResultActivitySubComponent extends TestMediaActivityCompatComponent  {

    void inject(SearchResultActivity searchResultActivityTest);
}