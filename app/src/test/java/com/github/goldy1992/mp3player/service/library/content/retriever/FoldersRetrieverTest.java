package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;
import com.github.goldy1992.mp3player.service.library.search.Folder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.File;
import java.util.List;

import static android.os.Looper.getMainLooper;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static com.github.goldy1992.mp3player.commons.MediaItemType.FOLDER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class FoldersRetrieverTest extends ContentResolverRetrieverTestBase<FoldersRetriever> {


    @Mock
    FolderResultsParser resultsParser;

    @Captor
    ArgumentCaptor<List<Folder>> captor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.retriever = spy(new FoldersRetriever(contentResolver, resultsParser, handler));
    }

    @Test
    public void testGetChildren() {
        this.contentRequest = new ContentRequest("x", "y", "z");
        final String directoryPath = "/a/b/c";
        final String directoryName = "c";
        // use mock file to avoid different OS path styles
        File file = mock(File.class);
        when(file.getAbsolutePath()).thenReturn(directoryPath);
        when(file.getName()).thenReturn(directoryName);
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
                .setFile(file)
                .build();
        expectedResult.add(mediaItem);
        when(contentResolver.query(EXTERNAL_CONTENT_URI, retriever.getProjection(), null, null, null)).thenReturn(cursor);
        when(resultsParser.create(cursor, contentRequest.getMediaIdPrefix())).thenReturn(expectedResult);
        /* IN ORDER for the database update code to be hit, there needs to be difference in file
        numbers to call it. This is a flaw in the design and will be addressed in another issue */


        List<MediaBrowserCompat.MediaItem> result = retriever.getChildren(contentRequest);
        // call remaining looper messages
        shadowOf(getMainLooper()).idle();

        // assert results are the expected ones
        assertEquals(expectedResult, result);
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(FOLDER, retriever.getType());
    }
}