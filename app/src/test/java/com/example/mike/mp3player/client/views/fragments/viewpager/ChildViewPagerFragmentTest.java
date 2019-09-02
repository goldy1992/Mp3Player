package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.views.fragments.FragmentTestBase;
import com.example.mike.mp3player.commons.MediaItemType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.TestUtils.createMediaItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ChildViewPagerFragmentTest extends FragmentTestBase<ChildViewPagerFragment> {

    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup(ChildViewPagerFragment.class, false);
        ChildViewPagerFragment childViewPagerFragment =
        ((ChildViewPagerFragment)fragment);
        childViewPagerFragment.init(MediaItemType.FOLDERS, "");
        childViewPagerFragment.setMediaBrowserAdapter(mediaBrowserAdapter);
        super.addFragmentToActivity();
    }

    @Test
    public void testItemSelected() {
        FragmentScenario.FragmentAction<ChildViewPagerFragment> action = this::itemSelected;
        performAction(action);
    }

    private void itemSelected(ChildViewPagerFragment fragment) {
        ChildViewPagerFragment spiedFragment = spy(fragment);
        final String id = "ID";
        final String title = "TITLE";
        final String description = "description";

        MediaBrowserCompat.MediaItem mediaItem =
                createMediaItem(id, title, description, MediaItemType.ROOT);
        spiedFragment.itemSelected(mediaItem);
        verify(spiedFragment).startActivity(any());

    }

}