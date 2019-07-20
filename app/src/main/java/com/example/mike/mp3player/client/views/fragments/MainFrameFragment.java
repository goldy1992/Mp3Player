package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.MyDrawerListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.views.MyPagerAdapter;
import com.example.mike.mp3player.client.views.ThemeSpinnerController;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

public class MainFrameFragment extends Fragment  implements MediaBrowserResponseListener {

    private DrawerLayout drawerLayout;
    private Toolbar titleToolbar;
    private ViewPager rootMenuItemsPager;
    private TabLayout tabLayout;
    private Provider<ChildViewPagerFragment> childFragmentProvider;
    private MediaBrowserAdapter mediaBrowserAdapter;
    private MyPagerAdapter adapter;
    private static final String LOG_TAG = "VIEW_PAGER_FRAGMENT";
    private NavigationView navigationView;
    private MyDrawerListener myDrawerListener;
    private boolean enabled = true;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main_frame, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        this.drawerLayout = view.findViewById(R.id.drawer_layout);
        this.titleToolbar = view.findViewById(R.id.titleToolbar);
        this.navigationView = view.findViewById(R.id.nav_view);
        this.rootMenuItemsPager = view.findViewById(R.id.rootItemsPager);
        this.tabLayout = view.findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(rootMenuItemsPager);
        this.adapter = new MyPagerAdapter(getFragmentManager());
        this.rootMenuItemsPager.setAdapter(this.adapter);

        /* TODO: consider different implementation of this functionality */
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();

            activity.setSupportActionBar(titleToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        if (null != mediaBrowserAdapter) {
            this.mediaBrowserAdapter.registerListener(Category.ROOT.name(), this);
        }
        initNavigationView();
        this.drawerLayout.addDrawerListener(myDrawerListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (enabled) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    getDrawerLayout().openDrawer(GravityCompat.START);
                    return true;
                default: break;
            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }


    private boolean onNavigationItemSelected(MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        // close drawer when item is tapped
        getDrawerLayout().closeDrawers();

        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true;
    }

    private void initNavigationView() {
        this.navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        Spinner spinner = (Spinner) getNavigationView().getMenu().findItem(R.id.themes_menu_item).getActionView();
        ThemeSpinnerController themeSpinnerController = new ThemeSpinnerController(getContext(), spinner, getActivity());
    }


    public void enable() {
        this.enabled = true;
        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void disable() {
        getDrawerLayout().closeDrawers();
        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.enabled = false;

    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
        TreeSet<MediaBrowserCompat.MediaItem> rootItemsOrdered = new TreeSet<>(compareRootMediaItemsByCategory);
        rootItemsOrdered.addAll(children);
        for (MediaBrowserCompat.MediaItem mediaItem : rootItemsOrdered) {
            String id = MediaItemUtils.getMediaId(mediaItem);
            Log.i(LOG_TAG, "media id: " + id);
            Category category = Category.valueOf(id);
            LibraryObject libraryObject = new LibraryObject(category, id);
            ChildViewPagerFragment childViewPagerFragment = childFragmentProvider.get();
            childViewPagerFragment.init(category, libraryObject);
            adapter.getPagerItems().put(category, childViewPagerFragment);
            adapter.getMenuCategories().put(category, mediaItem);
            adapter.notifyDataSetChanged();
        }
    }


    @Inject
    public void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Inject
    public void setMyDrawerListener(MyDrawerListener myDrawerListener) {
        this.myDrawerListener = myDrawerListener;
    }

    private void initialiseDependencies() {
        FragmentActivity activity = getActivity();
        if (null != activity && activity instanceof MediaActivityCompat) {
            MediaActivityCompat mediaPlayerActivity = (MediaActivityCompat) getActivity();
            mediaPlayerActivity.getMediaActivityCompatComponent()
                    .mainFrameFragmentSubcomponent()
                    .inject(this);
        }
    }

    @Inject
    public void setChildFragmentProvider(Provider<ChildViewPagerFragment> childFragmentProvider) {
        this.childFragmentProvider = childFragmentProvider;
    }

    @VisibleForTesting
    public MyPagerAdapter getAdapter() {
        return adapter;
    }

    @VisibleForTesting
    public Provider<ChildViewPagerFragment> getChildFragmentProvider() {
        return childFragmentProvider;
    }
}
