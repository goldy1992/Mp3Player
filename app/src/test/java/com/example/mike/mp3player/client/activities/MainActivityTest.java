package com.example.mike.mp3player.client.activities;

import android.support.v4.media.MediaBrowserCompat;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.views.fragments.SearchFragment;
import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private ActivityController<TestMainActivity> mainActivityTestActivityController;
    private MainActivity mainActivity;

    @Before
    public void setup() {
        mainActivityTestActivityController = Robolectric.buildActivity(TestMainActivity.class).setup();
        this.mainActivity = mainActivityTestActivityController.get();
     }

    @After
    public void tearDown() {
        mainActivityTestActivityController.destroy();
    }

    @Test
    public void testOnItemSelected() {
        MenuItem menuItem = mock(MenuItem.class);
        boolean result = mainActivity.onOptionsItemSelected(menuItem);
        assertFalse(result);
    }

    @Test
    public void testOnItemSelectedHomeButton() {
        final MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        boolean result = mainActivity.onOptionsItemSelected(menuItem);
        assertTrue(result);
    }

    // new tests
    @Test
    public void testOnOptionsItemSelectedOpenDrawer() {
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        DrawerLayout drawerLayoutSpy = spy(mainActivity.getDrawerLayout());
        mainActivity.setDrawerLayout(drawerLayoutSpy);
        mainActivity.onOptionsItemSelected(menuItem);
        verify(drawerLayoutSpy, times(1)).openDrawer(GravityCompat.START);
    }

    @Test
    public void testOnOptionsItemSelectedSearch() {
        final SearchFragment searchFragment = mainActivity.getSearchFragment();
        // assert the search fragment is NOT already added
        assertFalse(searchFragment.isAdded());
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(R.id.action_search);

        // select the search option item
        mainActivity.onOptionsItemSelected(menuItem);

        // assert the search fragment IS now added
        assertTrue(searchFragment.isAdded());
        // post test remove the added fragment
        mainActivity.getSupportFragmentManager().
                beginTransaction().remove(mainActivity.getSearchFragment())
                .commit();
    }


    @Test
    public void testNavigationItemSelected() {
        MenuItem menuItem = mock(MenuItem.class);
        DrawerLayout drawerLayoutSpy = spy(mainActivity.getDrawerLayout());
        mainActivity.setDrawerLayout(drawerLayoutSpy);
        mainActivity.onNavigationItemSelected(menuItem);
        verify(menuItem, times(1)).setChecked(true);
        verify(drawerLayoutSpy, times(1)).closeDrawers();
    }

    @Test
    public void testOnChildrenLoadedForRootCategory() {
        final String parentId = "parentId";
        final ArrayList<MediaBrowserCompat.MediaItem> children = new ArrayList<>();
        final Set<MediaItemType> rootItemsSet = MediaItemType.PARENT_TO_CHILD_MAP.get(MediaItemType.ROOT);

        for (MediaItemType category : rootItemsSet) {
            MediaBrowserCompat.MediaItem mediaItem =
                    new MediaItemBuilder("id1")
                            .setTitle(category.getTitle())
                            .setDescription(category.getDescription())
                            .setRootItemType(category)
                            .setMediaItemType(MediaItemType.ROOT)
                            .build();
            children.add(mediaItem);
        }
        final int expectedNumOfFragmentsCreated = rootItemsSet.size();
        mainActivity.onChildrenLoaded(parentId, children);
        int numberOfChildFragments = mainActivity.getAdapter().getCount();
        assertEquals(expectedNumOfFragmentsCreated, numberOfChildFragments);
    }

}