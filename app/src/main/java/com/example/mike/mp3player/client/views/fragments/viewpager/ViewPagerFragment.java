package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.commons.library.LibraryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public class ViewPagerFragment extends Fragment implements MediaBrowserResponseListener {

    private ViewPager rootMenuItemsPager;
    private PagerTabStrip pagerTabStrip;
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

    public void initRootMenu(Map<LibraryResponse, List<MediaItem>> items) {

        TreeSet<MediaItem> rootItemsOrdered = new TreeSet<>(compareRootMediaItemsByCategory);
        for (LibraryResponse id : items.keySet()) {
            rootItemsOrdered.add(id.getMediaItem());
        }

        for (MediaItem i : rootItemsOrdered) {
            Category category = LibraryConstructor.getCategoryFromMediaItem(i);
            GenericViewPageFragment viewPageFragment = null;
            switch (category) {
                case SONGS:
                    viewPageFragment = SongViewPageFragment.createAndInitialiseViewPageFragment(new LibraryRequest(category, null), items.get(i));
                    break;
                case FOLDERS:
                    viewPageFragment = FolderViewPageFragment.createAndInitialiseFragment(items.get(i));
                    break;
                default: break;
            }
            adapter.pagerItems.put(category, viewPageFragment);
            adapter.menuCategories.put(category, i);
            adapter.notifyDataSetChanged();
        }
    }

    public void populatePlaybackMetaDataListeners(MediaBrowserAdapter mediaBrowserAdapter, MediaControllerAdapter mediaControllerAdapter) {
        for(GenericViewPageFragment genericViewPageFragment : adapter.pagerItems.values()) {
            genericViewPageFragment.populatePlaybackMetaDataListeners(mediaBrowserAdapter, mediaControllerAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void enable() {}
    public void disable() {}

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children, @NonNull Bundle options, Context context) {
        LibraryRequest libraryRequest = (LibraryRequest) options.get(REQUEST_OBJECT);
        Log.i(LOG_TAG, "more children loaded main activity with parent id " + libraryRequest);
        if (null != libraryRequest && null != libraryRequest.getCategory() && libraryRequest.getId() != null) {
            /* TODO: add logic to distribute children to the correct menu fragment */
            int currentFragmentId = this.rootMenuItemsPager.getCurrentItem();
            GenericViewPageFragment f = adapter.getItem(currentFragmentId);
            f.onChildrenLoaded(libraryRequest, children);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        Map<Category, MediaItem> menuCategories = new TreeMap<>();
        Map<Category, GenericViewPageFragment> pagerItems = new TreeMap<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return pagerItems.size();
        }

        @Override
        public GenericViewPageFragment getItem(int position) {
        Category category = getCategoryFromPosition(position);
            return pagerItems.get(category);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            GenericViewPageFragment v = (GenericViewPageFragment) object;
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
