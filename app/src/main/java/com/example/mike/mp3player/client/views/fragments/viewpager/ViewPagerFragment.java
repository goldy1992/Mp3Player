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

import java.util.ArrayList;
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
    private MediaControllerAdapter mediaControllerAdapter;
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

    public void init(MediaBrowserAdapter mediaBrowserAdapter, MediaControllerAdapter mediaControllerAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mediaBrowserAdapter.registerListener(Category.ROOT, this);
    }

//    public void initRootMenu(Map<MediaItem, List<MediaItem>> items, MediaBrowserAdapter mediaBrowserAdapter,
//                             MediaControllerAdapter mediaControllerAdapter) {
//        mediaBrowserAdapter.registerListener(this);

//        for (MediaItem i : rootItemsOrdered) {

//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void enable() {}
    public void disable() {}

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children, @NonNull Bundle options, Context context) {
        LibraryRequest libraryObject = (LibraryRequest) options.get(REQUEST_OBJECT);
        TreeSet<MediaItem> rootItemsOrdered = new TreeSet<>(compareRootMediaItemsByCategory);
        rootItemsOrdered.addAll(children);
        for (MediaItem mediaItem : rootItemsOrdered) {
            Category category = LibraryConstructor.getCategoryFromMediaItem(mediaItem);
            GenericViewPageFragment viewPageFragment = null;
            switch (category) {
                case SONGS:
//                    viewPageFragment = SongViewPageFragment.createAndInitialiseViewPageFragment(new LibraryRequest(category, null), items.get(i), mediaBrowserAdapter, mediaControllerAdapter);
                        viewPageFragment = new SongViewPageFragment();
                    break;
                case FOLDERS:
//                    viewPageFragment = FolderViewPageFragment.createAndInitialiseFragment(items.get(i), mediaBrowserAdapter, mediaControllerAdapter);
                    viewPageFragment = new FolderViewPageFragment();
                    break;
                default: break;
            }
            adapter.pagerItems.put(category, viewPageFragment);
            adapter.menuCategories.put(category, mediaItem);
            adapter.notifyDataSetChanged();
        }
//        Log.i(LOG_TAG, "more children loaded main activity with parent id " + libraryObject);
//        if (null != libraryObject) {
//
//            /* TODO: add logic to distribute children to the correct menu fragment */
//            int currentFragmentId = this.rootMenuItemsPager.getCurrentItem();
//            GenericViewPageFragment f = adapter.getItem(currentFragmentId);
//            f.onChildrenLoaded(libraryObject, children);
//        }
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
