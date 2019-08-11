package com.example.mike.mp3player.client.views;

import android.support.v4.media.MediaBrowserCompat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.commons.MediaItemType;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private final Map<MediaItemType, MediaBrowserCompat.MediaItem> menuCategories = new TreeMap<>();
    private final Map<MediaItemType, ChildViewPagerFragment> pagerItems = new TreeMap<>();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        return pagerItems.size();
    }

    @Override
    public ChildViewPagerFragment getItem(int position) {
        MediaItemType category = getMediaItemTypeFromPosition(position);
        return pagerItems.get(category);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        ChildViewPagerFragment v = (ChildViewPagerFragment) object;
        return v.getView() == view;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        ArrayList<MediaItemType> categoryArrayList = new ArrayList<>(menuCategories.keySet());
        MediaItemType category = categoryArrayList.get(position);
        MediaBrowserCompat.MediaItem i = menuCategories.get(category);
        return i.getDescription().getTitle();
    }

    private MediaItemType getMediaItemTypeFromPosition(int position) {
        ArrayList<MediaItemType> categoryArrayList = new ArrayList<>(menuCategories.keySet());
        return categoryArrayList.get(position);
    }

    public Map<MediaItemType, ChildViewPagerFragment> getPagerItems() {
        return pagerItems;
    }

    public Map<MediaItemType, MediaBrowserCompat.MediaItem> getMenuCategories() {
        return menuCategories;
    }
}
