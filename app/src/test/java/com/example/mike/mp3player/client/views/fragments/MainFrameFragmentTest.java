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
import com.example.mike.mp3player.commons.library.Category;

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
        super.setup(MainFrameFragment.class);
    }

    @Test
    public void testOnOptionsItemSelectedOpenDrawer() {
        FragmentScenario.FragmentAction<MainFrameFragment> clickAndroidOptionMenu = this::clickAndroidOptionsMenu;
        performAction(clickAndroidOptionMenu);
    }

    @Test
    public void testOnOptionsItemSelectedFragmentNotEnabled() {
        FragmentScenario.FragmentAction<MainFrameFragment> onOptionsItemSelected = this::onOptionsItemSelectedNonEnabledFragment;
        performAction(onOptionsItemSelected);
    }

    @Test
    public void testOnOptionsItemSelectedFragmentEnabled() {
        FragmentScenario.FragmentAction<MainFrameFragment> onOptionsItemSelected = this::onOptionsItemSelectedEnabledFragment;
        performAction(onOptionsItemSelected);
    }

    @Test
    public void testNavigationItemSelected() {
        FragmentScenario.FragmentAction<MainFrameFragment> navigationViewSelected = this::navigationViewSelected;
        performAction(navigationViewSelected);
    }

    @Test
    public void testEnable() {
        FragmentScenario.FragmentAction<MainFrameFragment> enable = this::enable;
        performAction(enable);
    }

    @Test
    public void testDisable() {
        FragmentScenario.FragmentAction<MainFrameFragment> disable = this::disable;
        performAction(disable);
    }

    @Test
    public void testOnChildrenLoadedForRootCategory() {
        FragmentScenario.FragmentAction<MainFrameFragment> loadRootItems = this::onChildrenLoadedRootItems;
        performAction(loadRootItems);
    }

    private void onOptionsItemSelectedNonEnabledFragment(MainFrameFragment fragment) {
        try {
            FieldUtils.writeField(fragment, "enabled", false, true);
        } catch (IllegalAccessException ex) {
            ExceptionUtils.readStackTrace(ex);
            fail();
        }
        assertFalse(fragment.onOptionsItemSelected(null));
    }

    private void onChildrenLoadedRootItems(MainFrameFragment fragment) {
        final Provider<ChildViewPagerFragment> fragmentProviderSpied = spy(fragment.getChildFragmentProvider());
        fragment.setChildFragmentProvider(fragmentProviderSpied);
        final String parentId = Category.ROOT.name();
        final ArrayList<MediaBrowserCompat.MediaItem> children = new ArrayList<>();
        final Bundle options = new Bundle();

        for (Category category : Category.values()) {
            MediaBrowserCompat.MediaItem mediaItem = TestUtils.createMediaItem(category.name(), category.getTitle(), category.getDescription());
            children.add(mediaItem);
        }
        final int expectedNumOfFragmentsCreated = Category.values().length;
        fragment.onChildrenLoaded(parentId, children, options);
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

    private void enable(MainFrameFragment fragment) {
        try {
            DrawerLayout drawerLayoutSpy = spy(fragment.getDrawerLayout());
            FieldUtils.writeField(fragment, "drawerLayout", drawerLayoutSpy, true);
            fragment.enable();
            boolean enabled = (boolean) FieldUtils.readField(fragment, "enabled", true);
            assertTrue(enabled);
            verify(drawerLayoutSpy, times(1)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } catch (IllegalAccessException ex) {
            Log.e(LOG_TAG, ExceptionUtils.readStackTrace(ex));
            fail();
        }
    }

    private void disable(MainFrameFragment fragment) {
        try {
            DrawerLayout drawerLayoutSpy = spy(fragment.getDrawerLayout());
            FieldUtils.writeField(fragment, "drawerLayout", drawerLayoutSpy, true);
            fragment.disable();
            boolean enabled = (boolean) FieldUtils.readField(fragment, "enabled", true);
            assertFalse(enabled);
            verify(drawerLayoutSpy, times(1)).closeDrawers();
            verify(drawerLayoutSpy, times(1)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } catch (IllegalAccessException ex) {
            Log.e(LOG_TAG, ExceptionUtils.readStackTrace(ex));
            fail();
        }
    }
}
