package com.github.goldy1992.mp3player.service.library.utils;

import java.io.File;
import java.io.FilenameFilter;

public class IsDirectoryFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return new File(dir, name).isDirectory();
    }
}
