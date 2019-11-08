package com.github.goldy1992.mp3player.client.views.adapters;

import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MyPagerAdapter extends FragmentStateAdapter implements TabLayoutMediator.OnConfigureTabCallback {

    private final Map<MediaItemType, MediaBrowserCompat.MediaItem> menuCategories = new TreeMap<>();
    private final Map<MediaItemType, MediaItemListFragment> pagerItems = new TreeMap<>();

    public MyPagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
    }


    private MediaItemType getMediaItemTypeFromPosition(int position) {
        ArrayList<MediaItemType> categoryArrayList = new ArrayList<>(menuCategories.keySet());
        return categoryArrayList.get(position);
    }

    public Map<MediaItemType, MediaItemListFragment> getPagerItems() {
        return pagerItems;
    }

    public Map<MediaItemType, MediaBrowserCompat.MediaItem> getMenuCategories() {
        return menuCategories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MediaItemType category = getMediaItemTypeFromPosition(position);
        return pagerItems.get(category);
    }

    @Override
    public int getItemCount() {
        return pagerItems.size();
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        ArrayList<MediaItemType> categoryArrayList = new ArrayList<>(menuCategories.keySet());
        MediaItemType category = categoryArrayList.get(position);
        MediaBrowserCompat.MediaItem i = menuCategories.get(category);
        tab.setText(i.getDescription().getTitle());
    }
}
