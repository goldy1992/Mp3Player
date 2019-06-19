package com.example.mike.mp3player.client.views.fragments;

import android.util.Log;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.SongSearchActionListener;
import com.example.mike.mp3player.client.views.fragments.viewpager.ViewPagerFragment;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MainFrameFragmentTest extends FragmentTestBase<MainFrameFragment> {
    private static final String LOG_TAG = "MAIN_FRM_FRGMT_TST";
    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    @Mock
    private MediaControllerAdapter mediaControllerAdapter;
    @Mock
    private SongSearchActionListener songSearchActionListener;
    @Before
    public void setup() {
        super.setup(MainFrameFragment.class);
        FragmentScenario.FragmentAction<MainFrameFragment> init = this::initFragment;
        fragmentScenario.onFragment(init);
    }

    @Test
    public void testOnOptionsItemSelectedOpenDrawer() {
        FragmentScenario.FragmentAction<MainFrameFragment> clickAndroidOptionMenu = this::clickAndroidOptionsMenu;
        fragmentScenario.onFragment(clickAndroidOptionMenu);
    }

    @Test
    public void testOnOptionsItemSelectedFragmentNotEnabled() {
        FragmentScenario.FragmentAction<MainFrameFragment> onOptionsItemSelected = this::onOptionsItemSelectedNonEnabledFragment;
        fragmentScenario.onFragment(onOptionsItemSelected);
    }

    @Test
    public void testOnOptionsItemSelectedFragmentEnabled() {
        FragmentScenario.FragmentAction<MainFrameFragment> onOptionsItemSelected = this::onOptionsItemSelectedEnabledFragment;
        fragmentScenario.onFragment(onOptionsItemSelected);
    }

    @Test
    public void testNavigationItemSelected() {
        FragmentScenario.FragmentAction<MainFrameFragment> navigationViewSelected = this::navigationViewSelected;
        fragmentScenario.onFragment(navigationViewSelected);
    }

    @Test
    public void testEnable() {
        FragmentScenario.FragmentAction<MainFrameFragment> enable = this::enable;
        fragmentScenario.onFragment(enable);
    }

    @Test
    public void testDisable() {
        FragmentScenario.FragmentAction<MainFrameFragment> disable = this::disable;
        fragmentScenario.onFragment(disable);
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

    private void initFragment(MainFrameFragment fragment) {
        doNothing().when(mediaBrowserAdapter).registerListener(any(Object.class), any(MediaBrowserResponseListener.class));
        fragment.init(songSearchActionListener, mediaBrowserAdapter, mediaControllerAdapter);
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
            ViewPagerFragment viewPagerFragmentSpy = spy(fragment.getViewPagerFragment());
            FieldUtils.writeField(fragment, "drawerLayout", drawerLayoutSpy, true);
            FieldUtils.writeField(fragment, "viewPagerFragment", viewPagerFragmentSpy, true);
            fragment.enable();
            boolean enabled = (boolean) FieldUtils.readField(fragment, "enabled", true);
            assertTrue(enabled);
            verify(viewPagerFragmentSpy, times(1)).enable();
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
