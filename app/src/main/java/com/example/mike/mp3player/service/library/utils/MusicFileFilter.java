package com.example.mike.mp3player.service.library.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MusicFileFilter implements FilenameFilter {
    IsDirectoryFilter isDirectoryFilter = new IsDirectoryFilter();

    private final String MP3 = ".mp3";
    private final String WMA = ".wma";
    private final String WAV = ".wav";
    private final String MP2 = ".mp2";
    private final String AAC = ".aac";
    private final String AC3 = ".ac3";
    private final String AU = ".au";
    private final String OGG = ".ogg";
    private final String FLAC = ".flac";

    private final List<String> MUSIC_EXTENSIONS = Arrays.asList(MP3, WMA, WAV, MP2, AAC, AC3, AU, OGG, FLAC);

    @Override
    public boolean accept(File dir, String name) {
        name = name.toLowerCase(Locale.getDefault());
        if (!isDirectoryFilter.accept(dir, name)){
            for (String ext: MUSIC_EXTENSIONS) {
                if (name.endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }
}
