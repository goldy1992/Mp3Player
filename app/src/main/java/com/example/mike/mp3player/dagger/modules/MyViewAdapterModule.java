package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.views.MyFolderViewAdapter;
import com.example.mike.mp3player.client.views.MyGenericRecycleViewAdapter;
import com.example.mike.mp3player.client.views.MySongViewAdapter;
import com.example.mike.mp3player.commons.library.Category;

import dagger.Module;
import dagger.Provides;

@Module
public class MyViewAdapterModule {

    @Provides
    public MyGenericRecycleViewAdapter provideRecycleViewAdapter(Category category) {
        switch (category) {
            case SONGS: return new MySongViewAdapter();
            case FOLDERS: return new MyFolderViewAdapter();
            default: return null;
        }

    }
}
