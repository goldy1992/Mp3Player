package com.example.mike.mp3player.client.activities;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.MyDrawerListener;
import com.example.mike.mp3player.client.views.ThemeSpinnerController;
import com.example.mike.mp3player.client.views.adapters.MyPagerAdapter;
import com.example.mike.mp3player.client.views.fragments.SearchFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.FolderListFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.MediaItemListFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.SongListFragment;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.TreeSet;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByMediaItemType;
import static com.example.mike.mp3player.commons.Constants.ROOT_ITEM_TYPE;

public abstract class MainActivity extends MediaActivityCompat implements MediaBrowserResponseListener {

    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static final int READ_REQUEST_CODE = 42;

    private DrawerLayout drawerLayout;
    private Toolbar titleToolbar;
    private ViewPager2 rootMenuItemsPager;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;
    private MyPagerAdapter adapter;
    private ActionBar actionBar;
    private SearchFragment searchFragment;
    private AppBarLayout appBarLayout;
    private NavigationView navigationView;
    private MyDrawerListener myDrawerListener;

    @Override
    boolean initialiseView(@LayoutRes int layoutRes) {
        setContentView(layoutRes);
        this.searchFragment = new SearchFragment();
        this.setDrawerLayout(findViewById(R.id.drawer_layout));
        this.titleToolbar = findViewById(R.id.titleToolbar);
        this.navigationView = findViewById(R.id.nav_view);
        this.appBarLayout = findViewById(R.id.appbar);

        this.appBarLayout.addOnOffsetChangedListener((AppBarLayout app, int offset) -> {
            Log.i(LOG_TAG, "offset: " + offset + ", scroll range: " + app.getTotalScrollRange());
            if (null != app) {
                offset += app.getTotalScrollRange();
            }

            rootMenuItemsPager.setPadding(
                    rootMenuItemsPager.getPaddingLeft(),
                    rootMenuItemsPager.getPaddingTop(),
                    rootMenuItemsPager.getPaddingRight(),
                    offset);
        });

        this.rootMenuItemsPager = findViewById(R.id.rootItemsPager);
        this.tabLayout = findViewById(R.id.tabs);
        this.adapter = new MyPagerAdapter(getSupportFragmentManager(), getLifecycle());
        this.rootMenuItemsPager.setAdapter(adapter);
        tabLayoutMediator = new TabLayoutMediator(tabLayout, rootMenuItemsPager, adapter);
        tabLayoutMediator.attach();
        this.rootMenuItemsPager.setAdapter(this.adapter);
        this.setSupportActionBar(titleToolbar);
        this.actionBar= getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        if (null != mediaBrowserAdapter) {
            this.mediaBrowserAdapter.registerRootListener(this);
        }

        initNavigationView();
        this.drawerLayout.addDrawerListener(myDrawerListener);        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (null != getSearchFragment() && getSearchFragment().isAdded() && getSearchFragment().isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSearchFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                Log.i(LOG_TAG, "hit action search");
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, getSearchFragment(), "SEARCH_FGMT")
                        .addToBackStack(null)
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .commit();
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        super.onConnected();
        mediaBrowserAdapter.subscribeToRoot();
        initialiseView(R.layout.activity_main);
    }

    @VisibleForTesting
    public boolean onNavigationItemSelected(MenuItem menuItem) {
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
        ThemeSpinnerController themeSpinnerController = new ThemeSpinnerController(getApplicationContext(), spinner, this);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaBrowserCompat.MediaItem> children) {
        TreeSet<MediaBrowserCompat.MediaItem> rootItemsOrdered = new TreeSet<>(compareRootMediaItemsByMediaItemType);
        rootItemsOrdered.addAll(children);
        for (MediaBrowserCompat.MediaItem mediaItem : rootItemsOrdered) {
            String id = MediaItemUtils.getMediaId(mediaItem);
            Log.i(LOG_TAG, "media id: " + id);
            MediaItemType category = (MediaItemType) MediaItemUtils.getExtra(ROOT_ITEM_TYPE, mediaItem);
            MediaItemListFragment mediaItemListFragment;
            switch (category) {
                case SONGS: mediaItemListFragment = new SongListFragment(category, id, getMediaActivityCompatComponent());
                    break;
                case FOLDERS: mediaItemListFragment = new FolderListFragment(category, id, getMediaActivityCompatComponent());
                    break;
                default:
                    mediaItemListFragment = null;
                    break;
            }
            if (null != mediaItemListFragment) {
                Handler handler = new Handler(getMainLooper());
                handler.post(() -> {
                    adapter.getPagerItems().put(category, mediaItemListFragment);
                    adapter.getMenuCategories().put(category, mediaItem);
                    adapter.notifyDataSetChanged();});

            }
        }
    }

    @Inject
    public void setMyDrawerListener(MyDrawerListener myDrawerListener) {
        this.myDrawerListener = myDrawerListener;
    }

    @VisibleForTesting
    public MyPagerAdapter getAdapter() {
        return adapter;
    }

    /** {@inheritDoc} */
    @Override
    String getWorkerId() {
        return "MAIN_ACTVTY_WRKR";
    }

    @VisibleForTesting
    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @VisibleForTesting
    public SearchFragment getSearchFragment() {
        return searchFragment;
    }
}