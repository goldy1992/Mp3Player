package com.github.goldy1992.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.io.File;

import static com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class FolderActivityTest {

    private ActivityController<FolderActivityInjectorTestImpl> scenario;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = new Intent(context, FolderActivityInjectorTestImpl.class);
        File folder = new File("/a/b/xyz");
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
            .setLibraryId("xyz")
            .setDirectoryFile(folder)
            .build();
        intent.putExtra(MEDIA_ITEM, mediaItem);
        this.scenario = Robolectric.buildActivity(FolderActivityInjectorTestImpl.class, intent).setup();
    }

    @Test
    public void testOnBackPressed() {
        scenario.get().onBackPressed();
        assertTrue(scenario.get().isFinishing());
    }

}