package com.example.mike.mp3player.service.library.content.retriever;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.text.Normalizer;

import static com.example.mike.mp3player.commons.MediaItemType.SONG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class SongsRetrieverTest extends ContentResolverRetrieverTestBase<SongsRetriever> {

    @Before
    public void setup() {
        super.setup();
        this.retriever = spy(new SongsRetriever(contentResolver, resultsParser, searchDatabase));
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(SONG, retriever.getType());
    }

    @Test
    public void testNormalise() {
        final String RIO = "ríp";
      //  String result = Normalizer.normalize(RIO, Normalizer.Form.NFD);
      //  result = result.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        String result = StringUtils.stripAccents(RIO);
        System.out.println("original: " + RIO);
        System.out.println("result: " + result);
    }

}