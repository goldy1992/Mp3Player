package com.example.mike.mp3player.client.activities;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.MyDrawerListener;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.ThemeSpinnerController;
import com.example.mike.mp3player.client.views.adapters.MyPagerAdapter;
import com.example.mike.mp3player.client.views.fragments.MainFrameFragment;
import com.example.mike.mp3player.client.views.fragments.SearchFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.FolderViewPagerFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.SongViewPagerFragment;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

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
    private ViewPager rootMenuItemsPager;
    private TabLayout tabLayout;
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
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.titleToolbar = findViewById(R.id.titleToolbar);
        this.navigationView = findViewById(R.id.nav_view);
        this.appBarLayout = findViewById(R.id.appbar);
        this.appBarLayout.addOnOffsetChangedListener((AppBarLayout app, int offset) -> {
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
        this.tabLayout.setupWithViewPager(rootMenuItemsPager);
        this.adapter = new MyPagerAdapter(getSupportFragmentManager());
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
        if (null != searchFragment && searchFragment.isAdded() && searchFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(searchFragment)
                    .commit();
        }
        Log.i(LOG_TAG, "hit resume");
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
                        .add(R.id.fragment_container, searchFragment, "SEARCH_FGMT")
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
            ChildViewPagerFragment childViewPagerFragment;
            switch (category) {
                case SONGS: childViewPagerFragment = new SongViewPagerFragment(category, id, getMediaActivityCompatComponent());
                    break;
                case FOLDERS: childViewPagerFragment = new FolderViewPagerFragment(category, id, getMediaActivityCompatComponent());
                    break;
                default:
                    childViewPagerFragment = null;
                    break;
            }
            if (null != childViewPagerFragment) {
                Handler handler = new Handler(getMainLooper());
                handler.post(() -> {
                    adapter.getPagerItems().put(category, childViewPagerFragment);
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
    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.MEDIA_ID;
    }

    /** {@inheritDoc} */
    @Override
    String getWorkerId() {
        return "MAIN_ACTVTY_WRKR";
    }
}