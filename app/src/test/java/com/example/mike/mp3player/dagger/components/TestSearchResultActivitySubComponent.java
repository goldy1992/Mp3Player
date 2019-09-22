package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.client.activities.SearchResultActivity;
import com.example.mike.mp3player.client.activities.SearchResultActivityInjectorTestImpl;

import dagger.Subcomponent;

@Subcomponent(modules = {})
public interface TestSearchResultActivitySubComponent extends TestMediaActivityCompatComponent  {

    void inject(SearchResultActivity searchResultActivityTest);
}