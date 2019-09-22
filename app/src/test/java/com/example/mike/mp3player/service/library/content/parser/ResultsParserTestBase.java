package com.example.mike.mp3player.service.library.content.parser;

import android.support.v4.media.MediaBrowserCompat;

import org.robolectric.fakes.RoboCursor;

import java.util.Arrays;
import java.util.List;

public abstract class ResultsParserTestBase {

    ResultsParser resultsParser;

    public void setup() {

    }

    public abstract void testGetType();

    abstract Object[][] createDataSet();

    List<MediaBrowserCompat.MediaItem> getResultsForProjection(String[] projection, String testPrefix) {
        RoboCursor cursor = new RoboCursor();
        List<String> columns = Arrays.asList(projection);
        cursor.setColumnNames(columns);
        cursor.setResults(createDataSet());
        return resultsParser.create(cursor, testPrefix);
    }
}
