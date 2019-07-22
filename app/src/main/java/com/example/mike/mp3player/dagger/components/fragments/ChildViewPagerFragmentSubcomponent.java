package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.dagger.modules.AlbumArtPainterModule;
import com.example.mike.mp3player.dagger.modules.MyRecycleViewModule;

import dagger.BindsInstance;
import dagger.Subcomponent;


@Subcomponent(modules = {MyRecycleViewModule.class,
        AlbumArtPainterModule.class})
public interface ChildViewPagerFragmentSubcomponent {

    void inject(ChildViewPagerFragment childViewPagerFragment);

    @Subcomponent.Factory
    interface Factory {
        ChildViewPagerFragmentSubcomponent create(
                                                  @BindsInstance Category category,
                                                  @BindsInstance LibraryObject libraryObject,
                                                  @BindsInstance MyGenericItemTouchListener.ItemSelectedListener listener);
    }
}
