package com.example.mike.mp3player.service.library.content;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SearchQueryParserTest {

    private static SearchQueryParser searchQueryParser;

    @BeforeClass
    public static void setupClass() {
        searchQueryParser = new SearchQueryParser();
    }
    @Test
    public void parseQuery() {
        final String testQuery = "río";
        final String result = searchQueryParser.parseQuery(testQuery);
        final String expected = "rio";
        assertEquals(expected, result);

    }
}