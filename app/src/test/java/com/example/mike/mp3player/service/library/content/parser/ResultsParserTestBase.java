package com.example.mike.mp3player.service.library.content.parser;

import android.support.v4.media.MediaBrowserCompat;

import org.robolectric.fakes.RoboCursor;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;


public abstract class ResultsParserTestBase {

    ResultsParser resultsParser;

    public void setup() {

    }

    public abstract void testGetType();

    abstract Object[][] createDataSet();

    List<MediaBrowserCompat.MediaItem> getResultsForProjection(String[] projection, String testPrefix) {
        RoboCursor cursor = new RoboCursor();
        cursor.setColumnNames(Arrays.asList(projection));
        cursor.setResults(createDataSet());
        return resultsParser.create(cursor, testPrefix);
    }
}
