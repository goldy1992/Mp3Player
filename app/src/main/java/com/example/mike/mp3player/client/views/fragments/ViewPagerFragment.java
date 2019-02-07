package com.example.mike.mp3player.client.views.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryId;

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

import static com.example.mike.mp3player.commons.Constants.FOLDER_CHILDREN;
import static com.example.mike.mp3player.commons.Constants.FOLDER_NAME;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;
import static com.example.mike.mp3player.commons.MediaItemUtils.orderMediaItemSetByCategory;

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

    public void initRootMenu(Map<MediaItem, List<MediaItem>> items, MediaBrowserAdapter mediaBrowserAdapter,
                             MediaControllerAdapter mediaControllerAdapter, MediaPlayerActvityRequester mediaPlayerActvityRequester) {
        mediaBrowserAdapter.registerListener(this);
        List<MediaItem> rootItems = orderMediaItemSetByCategory(items.keySet());
        for (MediaItem i : rootItems) {
            Category category = LibraryConstructor.getCategoryFromMediaItem(i);
            ViewPageFragment viewPageFragment = ViewPageFragment.createAndInitialiseViewPageFragment(category, items.get(i), mediaBrowserAdapter, mediaControllerAdapter, mediaPlayerActvityRequester);
            adapter.pagerItems.put(category, viewPageFragment);
            adapter.menuCategories.put(category, i);
            adapter.notifyDataSetChanged();
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
        LibraryId libraryId = LibraryConstructor.parseId(parentId);
        Log.i(LOG_TAG, "more children loaded main activity with parent id " + libraryId);
        if (null != libraryId && null != libraryId.getCategory() && libraryId.getId() != null) {

            /* TODO: add logic to distribute children to the correct menu fragment */
            int currentFragmentId = this.rootMenuItemsPager.getCurrentItem();
            ViewPageFragment f = adapter.getItem(currentFragmentId);
            Intent intent =  new Intent(context, FolderActivity.class);
            intent.putExtra(PARENT_ID, parentId);
            String folderName = libraryId.getExtra(FOLDER_NAME);
            if (folderName  != null) {
                intent.putExtra(FOLDER_NAME, folderName);
            }
            intent.putParcelableArrayListExtra(FOLDER_CHILDREN, children);
            startActivity(intent);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        Map<Category, MediaItem> menuCategories = new HashMap<>();
        Map<Category, ViewPageFragment> pagerItems = new HashMap<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return pagerItems.size();
        }

        @Override
        public ViewPageFragment getItem(int position) {
        Category category = getCategoryFromPosition(position);
            return pagerItems.get(category);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            ViewPageFragment v = (ViewPageFragment) object;
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
