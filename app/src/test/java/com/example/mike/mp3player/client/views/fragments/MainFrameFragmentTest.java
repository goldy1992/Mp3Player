package com.example.mike.mp3player.client.views.fragments;

import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.commons.MediaItemBuilder;
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
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
        fragment.onChildrenLoaded(parentId, children);
        int numberOfChildFragments = fragment.getAdapter().getCount();
        assertEquals(expectedNumOfFragmentsCreated, numberOfChildFragments);
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
