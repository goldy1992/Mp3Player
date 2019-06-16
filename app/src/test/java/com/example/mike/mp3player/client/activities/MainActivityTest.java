package com.example.mike.mp3player.client.activities;


import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.MainActivityRootFragment;
import com.example.mike.mp3player.client.views.fragments.MainFrameFragment;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private ActivityController<MainActivity> activityController;
    private MainActivity mainActivity;
    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    @Mock
    private MediaControllerAdapter mediaControllerAdapter;

    @Before
    public void setup() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        this.activityController = Robolectric.buildActivity(MainActivity.class);
        this.mainActivity = spy(activityController.get());
        FieldUtils.writeField(activityController, "component", this.mainActivity, true);
        doReturn(mediaBrowserAdapter).when(mainActivity).makeMediaBrowserAdapter(any(), any(), any(), any());
        doReturn(mediaControllerAdapter).when(mainActivity).makeMediaControllerAdapter(any(), any());
        doReturn(true).when(mainActivity).initialiseView(R.layout.activity_main);
        this.activityController.create().start().resume();
    }

    @Test
    public void testGetSubscriptionType() {
        SubscriptionType expectedSubscriptionType = SubscriptionType.MEDIA_ID;
        SubscriptionType actualSubscriptionType = mainActivity.getSubscriptionType();
        assertEquals(expectedSubscriptionType, actualSubscriptionType);
    }

    @Test
    public void testCreateOptionsMenu() {
        Menu menu = mock(Menu.class);
        boolean result = this.mainActivity.onCreateOptionsMenu(menu);
        assertFalse(result);
    }

    @Test
    public void testInitialiseView() throws IllegalAccessException {
        this.mainActivity = spy(activityController.get());
        FieldUtils.writeField(activityController, "component", this.mainActivity, true);

        MainActivityRootFragment mainFrameFragment = mock(MainActivityRootFragment.class);
        FragmentManager fragmentManager = mock(FragmentManager.class);
        doNothing().when(mainActivity).setContentView(R.layout.activity_main);
        when(mainActivity.getSupportFragmentManager()).thenReturn(fragmentManager);
        when(fragmentManager.findFragmentById(R.id.mainActivityRootFragment)).thenReturn(mainFrameFragment);
        assertTrue(mainActivity.initialiseView(R.layout.activity_main));
    }

    @Test
    public void testOnConnected() {
        mainActivity.onConnected();
        verify(mediaBrowserAdapter, times(1)).subscribe(any(LibraryRequest.class));
    }

    @Test
    public void testOnItemSelected() throws IllegalAccessException {
        MainActivityRootFragment mainActivityRootFragment = mock(MainActivityRootFragment.class);
        MainFrameFragment mainFrameFragment = mock(MainFrameFragment.class);
        when(mainActivityRootFragment.getMainFrameFragment()).thenReturn(mainFrameFragment);
        FieldUtils.writeField(mainActivity, "rootFragment", mainActivityRootFragment, true);
        MenuItem menuItem = mock(MenuItem.class);
        when(mainFrameFragment.onOptionsItemSelected(menuItem)).thenReturn(true);
        assertTrue(mainActivity.onOptionsItemSelected(menuItem));
    }

    @Test
    public void testOnItemSelectedHomeButton() throws IllegalAccessException {
        MainActivityRootFragment mainActivityRootFragment = mock(MainActivityRootFragment.class);
        MainFrameFragment mainFrameFragment = mock(MainFrameFragment.class);
        when(mainActivityRootFragment.getMainFrameFragment()).thenReturn(mainFrameFragment);
        FieldUtils.writeField(mainActivity, "rootFragment", mainActivityRootFragment, true);
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        when(mainFrameFragment.onOptionsItemSelected(menuItem)).thenReturn(false);
        assertTrue(mainActivity.onOptionsItemSelected(menuItem));
    }
}