package com.example.mike.mp3player.client.views.fragments;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.views.fragments.viewpager.ViewPagerFragment;
import com.google.android.material.navigation.NavigationView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

/**
 * Likely to replace this with MainFrameFragmentTest2 in the future
 */
@RunWith(RobolectricTestRunner.class)
public class MainFrameFragmentTest {

    private MainFrameFragment mainFrameFragment;
    @Mock
    private DrawerLayout drawerLayout;
    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    @Mock
    private TitleBarFragment titleBarFragment;
    @Mock
    private PlayToolBarFragment playToolBarFragment;
    @Mock
    private ViewPagerFragment viewPagerFragment;
    @Mock
    private NavigationView navigationView;
    @Mock
    private FragmentManager childFragmentManager;

    @Before
    public void setup() throws IllegalAccessException {
//        FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class).get();
//        activity.getSupportFragmentManager().beginTransaction().add(mainFrameFragment, "main_fragment").commit();
//        MockitoAnnotations.initMocks(this);
//        LayoutInflater layoutInflater = mock(LayoutInflater.class);
//        this.mainFrameFragment.onCreateView(layoutInflater, null, null);
//        FragmentManager fragmentManager = mainFrameFragment.getChildFragmentManager();
//   //     FieldUtils.writeField(mainFrameFragment, "mChildFragmentManager", childFragmentManager, true);
//        View view = mock(View.class);
//        when(view.findViewById(R.id.drawer_layout)).thenReturn(drawerLayout);
//        when(view.findViewById(R.id.nav_view)).thenReturn(navigationView);
//        when(childFragmentManager.findFragmentById(R.id.playToolbarFragment)).thenReturn(playToolBarFragment);
//        when(childFragmentManager.findFragmentById(R.id.viewPagerFragment)).thenReturn(viewPagerFragment);
//        when(childFragmentManager.findFragmentById(R.id.titleBarFragment)).thenReturn(titleBarFragment);
//        mainFrameFragment.onViewCreated(view, null);
    }

    @Test
    public void testEnable() {
        assertTrue(true);
    }

}