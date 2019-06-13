package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.commons.library.Category;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.mike.mp3player.TestUtils.createRootItem;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ViewPagerFragmentTest {

    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    @Mock
    private ViewPagerFragment.MyPagerAdapter myPagerAdapter;

    private FragmentScenario<ViewPagerFragment> viewPagerFragmentScenario;
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.viewPagerFragmentScenario = FragmentScenario.launch(ViewPagerFragment.class);
        FragmentScenario.FragmentAction<ViewPagerFragment> initFragment = (ViewPagerFragment fragment) -> callInitFragment(fragment);
        viewPagerFragmentScenario.onFragment(initFragment);
    }

    @Test
    public void enable() {
    }

    @Test
    public void disable() {
    }

    /**
     * GIVEN: that all root items are given in a list as a parameter to the onChildrenLoaded() method
     * WHEN: onChildrenLoaded is called
     * THEN: 1) the data set is changed once for every category
 *          2) the pagerItems and menuCategories sets contain each of the categories
     */
    @Test
    public void testOnChildrenLoaded() {
        FragmentScenario.FragmentAction<ViewPagerFragment> onLoadChildren =
                (ViewPagerFragment fragment) -> onLoadChildren(fragment);
        viewPagerFragmentScenario.onFragment(onLoadChildren);
    }

    private void callInitFragment(ViewPagerFragment fragment) {
        fragment.init(mediaBrowserAdapter);
        myPagerAdapter.pagerItems = new HashMap<>();
        myPagerAdapter.menuCategories = new HashMap<>();
        fragment.setMyPageAdapter(myPagerAdapter);
    }

    private void onLoadChildren(ViewPagerFragment fragment) {
        final int expectedNumberOfDataSetChanges = Category.values().length;
        String parent = createRootItem(Category.ROOT).getMediaId();
        Bundle options = new Bundle();
        ArrayList<MediaItem> children = new ArrayList<>();
        for (Category c : Category.values()) {
            children.add(createRootItem(c));
        }
        fragment.onChildrenLoaded(parent, children, options,  context);
        verify(myPagerAdapter, times(expectedNumberOfDataSetChanges)).notifyDataSetChanged();
        for (Category c : Category.values()) {
            assertTrue(myPagerAdapter.pagerItems.containsKey(c));
            assertTrue(myPagerAdapter.menuCategories.containsKey(c));
        }
    }
}