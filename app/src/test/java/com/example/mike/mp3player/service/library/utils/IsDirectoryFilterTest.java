package com.example.mike.mp3player.service.library.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsDirectoryFilterTest {

    IsDirectoryFilter IsDirectoryFilter = new IsDirectoryFilter();

    @Test
    public void testAccept() {
        File directoryFile = mock(File.class);
        when(directoryFile.isDirectory()).thenReturn(Boolean.TRUE);
        //assertTrue(IsDirectoryFilter.accept())
    }
}
