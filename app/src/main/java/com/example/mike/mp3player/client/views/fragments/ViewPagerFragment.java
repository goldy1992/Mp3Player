package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerFragment extends Fragment {

    private ViewPager rootMenuItemsPager;
    private PagerTabStrip pagerTabStrip;
    private MyPagerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_view_pager, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        rootMenuItemsPager = view.findViewById(R.id.rootItemsPager);
        pagerTabStrip = view.findViewById(R.id.pagerTabStrip);
        adapter = new MyPagerAdapter(getFragmentManager());
        rootMenuItemsPager.setAdapter(adapter);
    }

    public void initRootMenu(List<MediaBrowserCompat.MediaItem> items) {
        for (MediaBrowserCompat.MediaItem i : items) {
            Category category = LibraryConstructor.getCategoryFromMediaItem(i);
            adapter.pagerItems.put(category, new ViewPageFragment());
            adapter.notifyDataSetChanged();
        }
    }

    public void enable() {}
    public void disable() {}


    private class MyPagerAdapter extends FragmentPagerAdapter {

        List<Category> menuCategories = new ArrayList<>();
        Map<Category, Fragment> pagerItems = new HashMap<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return pagerItems.size();
        }

        @Override
        public Fragment getItem(int position) {
            return pagerItems.get(menuCategories.get(position));
        }

        @Override
        public Object instantiateItem (ViewGroup container,
                                       int position) {
            return null;
        }

        @Override
        public void destroyItem (ViewGroup container,
                                 int position,
                                 Object object) {
            return;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            Category category = menuCategories.get(position);
            return category.name();
        }

    }
}
