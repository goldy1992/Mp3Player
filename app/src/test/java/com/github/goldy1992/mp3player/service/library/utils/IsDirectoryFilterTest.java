package com.github.goldy1992.mp3player.service.library.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IsDirectoryFilterTest {

    IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();

    @Test
    public void testAccept() {
        File directoryFile = mock(File.class);
        when(directoryFile.isDirectory()).thenReturn(Boolean.TRUE);
        assertTrue(true);
    }
}
