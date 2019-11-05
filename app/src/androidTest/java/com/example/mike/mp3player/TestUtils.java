package com.example.mike.mp3player;

import com.google.android.material.tabs.TabLayout;

import static org.junit.Assert.assertEquals;

/**
 * This class will be used for utility methods in the automation framework.
 */
public final class TestUtils {

    public static void assertTabName(TabLayout tabLayout, int position, String expectedTabTitle) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        final String actualNameFirstTab = tab.getText().toString();
        assertEquals(expectedTabTitle, actualNameFirstTab);
    }
}
