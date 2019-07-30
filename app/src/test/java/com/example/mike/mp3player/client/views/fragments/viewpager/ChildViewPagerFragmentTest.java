package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.views.fragments.FragmentTestBase;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.TestUtils.createMediaItem;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        LibraryObject libraryObject = new LibraryObject(Category.ROOT, "ROOT");
        ChildViewPagerFragment childViewPagerFragment =
        ((ChildViewPagerFragment)fragment);
        childViewPagerFragment.init(Category.FOLDERS, libraryObject);
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
                createMediaItem(id, title, description);
        spiedFragment.itemSelected(mediaItem);
        ArgumentCaptor<Intent> intentArgs = ArgumentCaptor.forClass(Intent.class);
        verify(spiedFragment).startActivity(intentArgs.capture());
        Intent intent = intentArgs.getValue();
        LibraryRequest libraryRequest = intent.getExtras().getParcelable(REQUEST_OBJECT);
        assertNotNull(libraryRequest);
        assertEquals(id, libraryRequest.getId());
        assertEquals(title, libraryRequest.getTitle());
    }

}