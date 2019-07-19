package com.example.mike.mp3player.client.views;

import android.support.v4.media.MediaBrowserCompat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.commons.library.Category;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private Map<Category, MediaBrowserCompat.MediaItem> menuCategories = new TreeMap<>();
    private Map<Category, ChildViewPagerFragment> pagerItems = new TreeMap<>();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        return pagerItems.size();
    }

    @Override
    public ChildViewPagerFragment getItem(int position) {
        Category category = getCategoryFromPosition(position);
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
        ArrayList<Category> categoryArrayList = new ArrayList<>(menuCategories.keySet());
        Category category = categoryArrayList.get(position);
        MediaBrowserCompat.MediaItem i = menuCategories.get(category);
        return i.getDescription().getTitle();
    }

    private Category getCategoryFromPosition(int position) {
        ArrayList<Category> categoryArrayList = new ArrayList<>(menuCategories.keySet());
        return categoryArrayList.get(position);
    }

    public Map<Category, ChildViewPagerFragment> getPagerItems() {
        return pagerItems;
    }

    public Map<Category, MediaBrowserCompat.MediaItem> getMenuCategories() {
        return menuCategories;
    }
}
