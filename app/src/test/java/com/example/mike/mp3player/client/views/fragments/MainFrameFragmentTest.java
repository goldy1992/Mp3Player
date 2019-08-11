package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.TestUtils;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.client.Category;
import com.example.mike.mp3player.commons.MediaItemType;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.inject.Provider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MainFrameFragmentTest extends FragmentTestBase<MainFrameFragment> {
    private static final String LOG_TAG = "MAIN_FRM_FRGMT_TST";

    @Before
    public void setup() {
        super.setup(MainFrameFragment.class, true);
    }

    @Test
    public void testOnOptionsItemSelectedOpenDrawer() {
        FragmentScenario.FragmentAction<MainFrameFragment> clickAndroidOptionMenu = this::clickAndroidOptionsMenu;
        performAction(clickAndroidOptionMenu);
    }


    @Test
    public void testNavigationItemSelected() {
        FragmentScenario.FragmentAction<MainFrameFragment> navigationViewSelected = this::navigationViewSelected;
        performAction(navigationViewSelected);
    }


    @Test
    public void testOnChildrenLoadedForRootCategory() {
        FragmentScenario.FragmentAction<MainFrameFragment> loadRootItems = this::onChildrenLoadedRootItems;
        performAction(loadRootItems);
    }

    private void onChildrenLoadedRootItems(MainFrameFragment fragment) {
        final Provider<ChildViewPagerFragment> fragmentProviderSpied = spy(fragment.getChildFragmentProvider());
        fragment.setChildFragmentProvider(fragmentProviderSpied);
        final String parentId = "parentId";
        final ArrayList<MediaBrowserCompat.MediaItem> children = new ArrayList<>();

        for (MediaItemType category : MediaItemType.values()) {
            MediaBrowserCompat.MediaItem mediaItem = TestUtils.createMediaItem(category.name(), category.getTitle(), category.getDescription(), category);
            children.add(mediaItem);
        }
        final int expectedNumOfFragmentsCreated = Category.values().length;
        fragment.onChildrenLoaded(parentId, children);
        verify(fragmentProviderSpied, times(expectedNumOfFragmentsCreated)).get();
    }

    private void onOptionsItemSelectedEnabledFragment(MainFrameFragment fragment) {
        try {
            FieldUtils.writeField(fragment, "enabled", true, true);
        } catch (IllegalAccessException ex) {
            ExceptionUtils.readStackTrace(ex);
            fail();
        }
        MenuItem menuItem = mock(MenuItem.class);
        assertFalse(fragment.onOptionsItemSelected(menuItem));
    }
    private void clickAndroidOptionsMenu(MainFrameFragment fragment) {
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        DrawerLayout drawerLayoutSpy = spy(fragment.getDrawerLayout());
        try {
            FieldUtils.writeField(fragment, "drawerLayout", drawerLayoutSpy, true);
        } catch (IllegalAccessException ex) {
            ExceptionUtils.readStackTrace(ex);
            fail();
        }
        fragment.onOptionsItemSelected(menuItem);
        verify(drawerLayoutSpy, times(1)).openDrawer(GravityCompat.START);
    }

    private void navigationViewSelected(MainFrameFragment fragment) {
        MenuItem menuItem = mock(MenuItem.class);
        DrawerLayout drawerLayoutSpy = spy(fragment.getDrawerLayout());
        try {

            FieldUtils.writeField(fragment, "drawerLayout", drawerLayoutSpy, true);
            Class[] classes = {MenuItem.class};
            Method m  = fragment.getClass().getDeclaredMethod("onNavigationItemSelected", classes);
            m.setAccessible(true);
            m.invoke(fragment, menuItem);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
            Log.e(LOG_TAG, ExceptionUtils.readStackTrace(ex));
            fail();
        }
        verify(menuItem, times(1)).setChecked(true);
        verify(drawerLayoutSpy, times(1)).closeDrawers();
    }


}
