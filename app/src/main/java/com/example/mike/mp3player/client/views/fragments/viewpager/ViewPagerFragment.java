package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment.createViewPageFragment;
import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

public class ViewPagerFragment extends Fragment implements MediaBrowserResponseListener {

    private ViewPager rootMenuItemsPager;
    private PagerTabStrip pagerTabStrip;
    private MediaBrowserAdapter mediaBrowserAdapter;
    private MyPagerAdapter adapter;
    private static final String LOG_TAG = "VIEW_PAGER_FRAGMENT";
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

    public void init(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.mediaBrowserAdapter.registerListener(Category.ROOT.name(), this);
    }

    public void enable() {}
    public void disable() {}

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children, @NonNull Bundle options, Context context) {
        TreeSet<MediaItem> rootItemsOrdered = new TreeSet<>(compareRootMediaItemsByCategory);
        rootItemsOrdered.addAll(children);
        for (MediaItem mediaItem : rootItemsOrdered) {
            String id = MediaItemUtils.getMediaId(mediaItem);
            Log.i(LOG_TAG, "media id: " + id);
            Category category = Category.valueOf(id);
            LibraryObject libraryObject = new LibraryObject(category, id);
            ChildViewPagerFragment viewPageFragment = createViewPageFragment(category, libraryObject, mediaBrowserAdapter);
            adapter.pagerItems.put(category, viewPageFragment);
            adapter.menuCategories.put(category, mediaItem);
            adapter.notifyDataSetChanged();
        }

    }

    @VisibleForTesting
    public void setMyPageAdapter(MyPagerAdapter myPageAdapter) {
        this.adapter = myPageAdapter;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        Map<Category, MediaItem> menuCategories = new TreeMap<>();
        Map<Category, ChildViewPagerFragment> pagerItems = new TreeMap<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm, RESUME_ONLY_CURRENT_FRAGMENT);
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
            MediaItem i = menuCategories.get(category);
            return i.getDescription().getTitle();
        }

        private Category getCategoryFromPosition(int position) {
            ArrayList<Category> categoryArrayList = new ArrayList<>(menuCategories.keySet());
            return categoryArrayList.get(position);
        }
    }
}
